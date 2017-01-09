package com.example.user.airscort_agriculture.Activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.user.airscort_agriculture.Services.AlarmReciever;
import com.example.user.airscort_agriculture.DB.DataAccess;
import com.example.user.airscort_agriculture.Interfaces.MapInterface;
import com.example.user.airscort_agriculture.Fragments.MapFragment;
import com.example.user.airscort_agriculture.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/*
this activity show to user his scanned fields on map and he can follow after drone's location during the scanning
 */
public class CurrentMissionActivity extends AppCompatActivity implements MapInterface {
    private MapFragment map;
    private FrameLayout frameLayout;
    private LatLng homePoint;
    private ArrayList<String> fieldsName;
    private ArrayList<LatLng> pathFrame;
    private ArrayList<LatLng> fullPath;
    private DataAccess dataAccess;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor spEditor;
    private AlarmReciever alarm;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_mission);
        alarm=new AlarmReciever();
        sharedPreferences= getSharedPreferences(getString(R.string.user_details), MODE_PRIVATE);
        spEditor=sharedPreferences.edit();
        dataAccess=new DataAccess(this);
        homePoint = dataAccess.getHomePoint();
        frameLayout = (FrameLayout) findViewById(R.id.current_mission_map);

        map = new MapFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(frameLayout.getId(), map, "MAP_FRAGMENT_CURRENT_MISSION")
                .commit();
        map.setMapInterface(this);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Current mission");     // set title for activity
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //floating button for stop scanning
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertForStoping(view);
            }
        });
    }

    public void alertForStoping(View v) {
            new AlertDialog.Builder(this)                                          //dialog to confirm the stopping
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(getString(R.string.stop_scanning))
                    .setMessage(getString(R.string.confiem_delete_scanning))
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {       //stop scanning
                            stopScanning();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
    }

    public void stopScanning(){
        dataAccess.deleteScanning();
        alarm.cancelAlarm(CurrentMissionActivity.this);
        Intent intent = new Intent(CurrentMissionActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        Intent intent=new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    //implements map fragment
    public void finishCreateMap() {
        map.addHomeMarker(homePoint);                              //add home station icon
        fieldsName = dataAccess.getFieldsFromScanning();          //add fields
        for (int i = 0; i < fieldsName.size(); i++) {            //show fields on the map
            pathFrame = dataAccess.getFramePath(fieldsName.get(i));
            fullPath = dataAccess.getDronePath(fieldsName.get(i));
            map.drawPolygon(pathFrame, Color.WHITE);
            map.drawDronePath(fullPath);
        }
        map.addDroneMarker(dataAccess.getDroneLocation());        //show drone icon

        //TODO: add location update each ___ time
    }

    public String getMode(){
        return "";
    }

    public String getFieldName(){
        return "";
    }

    public void setFullDronePath(ArrayList<LatLng> arr){
    }

    public ArrayList<LatLng> getFullDronePath(){
        return null;
    }

    public void clearFullDronePath(){}

    public ArrayList<LatLng> getPathFrame(){return null;}

    public void clearPathFrame(){}

    public LatLng getHomePoint(){return homePoint;}

    public void chooseField(LatLng position){}

}
