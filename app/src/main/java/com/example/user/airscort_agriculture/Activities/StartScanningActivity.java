package com.example.user.airscort_agriculture.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.airscort_agriculture.Services.AlarmReciever;
import com.example.user.airscort_agriculture.DB.DataAccess;
import com.example.user.airscort_agriculture.Classes.DronePath;
import com.example.user.airscort_agriculture.Classes.Resolution;
import com.example.user.airscort_agriculture.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;

/*
This activity display to user the estimated scanning time
 */
public class StartScanningActivity extends AppCompatActivity {

    private TextView startScanning, estimatedTime, time;
    private int timeScanning;
    private DataAccess dataAccess;
    private DronePath dronePath;
    private ImageView drone;
    private Button trackButton;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor spEditor;
    private ArrayList<String> fieldsToScan;                     //names of scanned fields
    private ArrayList<LatLng> uniteFields;                      //unite the points of scanned fields to one array
    private ActionBar actionBar;
    private Resolution resolution;
    private LatLng homePoint;
    private String scanningResolution;                         //scanning resolution
    private int highF;                                         //The Flying height
    private double scanningWidth;                              //scanning width acoording to resolution
    private final double LNG_FOR_METER = 0.000008983;             //the distance in longtitude for  each meter
    private final double LAT_FOR_METER = 0.000009044;             //the distance in latitude for  each meter
    private final int METER_IN_TEN_MINUTES = 4800;               //meters for each flight until the loading
    private final double DRONE_SPEED = 28.8;                   //28 km/h is a drone speed
    private final double LANDING_SPEED = 1.8;                   // 1.8 km/h is a drone landing speed
    private final double TAKINGOFF_SPEED = 9;              // 9 km/h is a drone taking off speed
    private final int FLIGHT_TIME = 10;                //each 10 miniutes the drone have to go back station to load
    private final int CHARGING_TIME = 45;                //45 minutes is time to charge the drone battery
    private int hours, minutes;
    private AlarmReciever alarm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_scanning);
        sharedPreferences= getSharedPreferences(getString(R.string.user_details), MODE_PRIVATE);
        spEditor=sharedPreferences.edit();
        dataAccess = new DataAccess(this);
        dronePath = new DronePath();
        resolution = new Resolution();
        actionBar = getSupportActionBar();
        actionBar.setTitle("Start scanning");     // set title for activity
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        uniteFields = new ArrayList<>();
        homePoint = dataAccess.getHomePoint();
        fieldsToScan = dataAccess.getFieldsFromScanning();
        scanningResolution = dataAccess.getResolutionFromScanning();
        highF = resolution.highForResolution(scanningResolution);
        scanningWidth = resolution.longtitudeSpace(scanningResolution);

        startScanning = (TextView) findViewById(R.id.textView);
        estimatedTime = (TextView) findViewById(R.id.textView2);
        time = (TextView) findViewById(R.id.time);
        drone = (ImageView) findViewById(R.id.drone);
        alarm=new AlarmReciever();
        timeScanning = calculateTotalScaningTime();

        //show user the estimated scanning time
        hours=0;
        minutes=0;
        if(timeScanning>60){
            hours=timeScanning/60;
            minutes=timeScanning%60;
        }
        if(hours==0){
            time.setText(timeScanning + " minutes");
        }
        else{
            if(minutes!=0) {
                time.setText(hours + " hours + " + minutes + " minutes");
            }
            else{
                time.setText(hours + " hours");
            }
        }

        //if the track button had press- go to current scanning to follow after the progress.
        trackButton = (Button) findViewById(R.id.track);
        trackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date=dataAccess.getDateFromScanning();
                if(date.equals("")){
                    Toast.makeText(StartScanningActivity.this, getString(R.string.no_current_scanning), Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent(StartScanningActivity.this, CurrentMissionActivity.class);
                    startActivity(intent);
                }
            }
        });
        setTimeForAlarm();

        //animation for drone
        drone.setBackgroundResource(R.drawable.spin_animation);
        AnimationDrawable frameAnimation = (AnimationDrawable) drone.getBackground();
        // Start the animation (looped playback by default).
        frameAnimation.start();
    }

    /* calculate the estimated scanning time in millisecond and start alarm */
    public void setTimeForAlarm(){
        long nowUtc=System.currentTimeMillis();
        long milliesForTiming =nowUtc+(hours*60*60*1000)+(minutes*60*1000);
        dataAccess.addScanningTime(milliesForTiming);
        alarm.setAlarm(StartScanningActivity.this, milliesForTiming);
    }

    /*
    calculate estimated scanning time according to:
    taking Off and landing time for each part of flight, 10 minutes of scanning until the next loading, 45 minutes of charging
    and back and forth from stop point to home point
     */
    public int calculateTotalScaningTime() {
            uniteFieldsToScan();                                          //unite all points of fields to scan in one array list
            double distance = 0;                                          //couting the passing distance
            double disToHomePoint;                                       //distance from a given point to home point

        double time = calculateTime(highF, TAKINGOFF_SPEED);                   //Calculates the time of first take-off
        time+=calculateTime(distanceBetweenPoints(homePoint, uniteFields.get(0)),DRONE_SPEED);  //add time from home to first point
        int stops = 1;                                                     //how many stops for loading were
        float totalDistance = dronePath.fieldDistance(uniteFields);        //get distance in meter of all scanning fields
        LatLng stopPoint = uniteFields.get(0);
        int i = 1;
        while (distance < totalDistance && i!=uniteFields.size()) {
            for (; i < uniteFields.size(); i++) {
                distance += distanceBetweenPoints(stopPoint, uniteFields.get(i));
                if (distance > (stops * METER_IN_TEN_MINUTES)) {              //the stop point is between these points
                    distance -= distanceBetweenPoints(stopPoint, uniteFields.get(i));
                    stopPoint = findTheStopPoint(uniteFields.get(i - 1), uniteFields.get(i), distance, stops);   //find the stop point

                    distance += distanceBetweenPoints(uniteFields.get(i - 1), stopPoint);
                    stops++;
                    disToHomePoint = distanceBetweenPoints(stopPoint, homePoint);    //calculate distance from stop point to home
                    time += (calculateTime(disToHomePoint, DRONE_SPEED) * 2);        //add time to go station and back
                    time += FLIGHT_TIME;                                           //add 10 minutes that drone past
                    time += calculateTime(highF, LANDING_SPEED);                    //add time of Landing
                    time+= CHARGING_TIME;                                            //add charging time
                    time += calculateTime(highF, TAKINGOFF_SPEED);                  //add time of taking off
                    break;
                } else {
                    stopPoint = uniteFields.get(i);
                }
            }
        }
        time+=calculateTime((totalDistance-((stops-1)* METER_IN_TEN_MINUTES)),DRONE_SPEED);     //add the flight time for part of METER_IN_TEN_MINUTES
        //add time from last point to home
        time+=calculateTime(distanceBetweenPoints(uniteFields.get(uniteFields.size()-1), homePoint),DRONE_SPEED);
        time += calculateTime(highF, LANDING_SPEED);                              //add time for last landing
        return (int) time;
    }

    /* unite all points of fields to scan to one array list */
    public void uniteFieldsToScan() {
        for (int i = 0; i < fieldsToScan.size(); i++) {
            uniteFields.addAll(dataAccess.getDronePath(fieldsToScan.get(i)));
        }
    }

    /* return distance in meter between 2 given points */
    public double distanceBetweenPoints(LatLng p1, LatLng p2) {
        return SphericalUtil.computeDistanceBetween(p1, p2);
    }

    /* find point between 2 points that drone have to go from it for loading */
    public LatLng findTheStopPoint(LatLng p1, LatLng p2, double distance, int stops) {
        double disToStop = (stops * METER_IN_TEN_MINUTES) - distance;
        LatLng stopPoint;
        if (p2.longitude == p1.longitude ) {                         //if the stop point is in latitude
            double addLat = disToStop * LAT_FOR_METER;               //convert the distance and add accordance latitude
            if (p1.latitude > p2.latitude) {                                   //the way is from up to down
                stopPoint = new LatLng(p1.latitude - addLat, p1.longitude);
            } else {                                                          //the way is from down to up
                stopPoint = new LatLng(p1.latitude + addLat, p1.longitude);
            }
        } else {
            double addLon = disToStop * LNG_FOR_METER;               //convert the distance and add accordance lontitude
            double lat;
            if(p1.longitude > p2.longitude){                                 //the way is from right to left
                lat = dronePath.findLatitudeOnPolygon(p1.longitude - addLon, p1, p2);  //find latitude according to given longtitude
                stopPoint = new LatLng(lat, p1.longitude-addLon);
            }

            else{                                                           //the way is from left to right
                lat = dronePath.findLatitudeOnPolygon(p1.longitude + addLon, p1, p2);  //find latitude according to given longtitude
                stopPoint = new LatLng(lat, p1.longitude+addLon);
            }
        }
        return stopPoint;
    }

    /* Calculates the time- given distance and speed */
    public double calculateTime(double dis, double speed) {
        return ((dis / 1000) / speed) * 60;    //dis/1000 for convert to km
    }
}