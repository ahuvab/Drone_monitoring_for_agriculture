package com.example.user.airscort_agriculture.Classes;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;

/*
This class represents the drone's path .
Finds the path and draw on the map
 */
public class DronePath {

    private ArrayList<LatLng> path;                       //drone path
    private Resolution resolutionClass;
    private final double LATITUDE_SPACE = 0.00001;
    private double longtitude_space;                      //the width between the scanning lines according to scanning resolution
    private final double MIN_LONGITUDE = -180.0;
    private final double MAX_LONGITUDE = 180.0;
    private final double MIN_LATITUDE = -90.0;
    private final double MAX_LATITUDE = 90.0;
    private GoogleMap mGoogleMap;
    private ArrayList<LatLng> points;
    private  double minLng;
    private double maxLng;
    private double minLat;
    private double maxLat;
    private LatLng minLngPoint, maxLngPoint;

    public DronePath() {
        path = new ArrayList<>();
        points = new ArrayList<>();
        resolutionClass=new Resolution();
        minLng = MAX_LONGITUDE;                              //update min and max points
        maxLng = MIN_LONGITUDE;
        minLat = MAX_LATITUDE;
        maxLat = MIN_LATITUDE;
    }

    /* find drone path given field frame and scanning resolution */
    public ArrayList<LatLng> findDronePath(ArrayList<LatLng> field, String resolution) {
        path.clear();
        points.clear();
        minLng = MAX_LONGITUDE;                              //update min and max points
        maxLng = MIN_LONGITUDE;
        minLat = MAX_LATITUDE;
        maxLat = MIN_LATITUDE;
        findfMinAndMaxPoint(field);

        boolean lastState = false;
        longtitude_space=resolutionClass.longtitudeSpace(resolution);
        path.add(minLngPoint);                              //add the most left point to drone path

        for (double lng=minLng; lng < maxLng; lng += longtitude_space) {
            for (double lat=minLat; lat < maxLat; lat += LATITUDE_SPACE) {           //from down to up
                addLat(lat,lng,lastState, -1);
            }
            lng += longtitude_space;
            for (double lat = maxLat; lat > minLat; lat -= LATITUDE_SPACE) {        //from up to down
                addLat(lat,lng,lastState, 1);
            }
        }
        path.add(maxLngPoint);                              //add the most right point to drone path
        return path;
    }

    /* add to drone path latitude */
    private void addLat(double lat, double lng ,boolean lastState, int up){
        LatLng currentLocation=new LatLng(lat,lng);
        boolean isInBounds = contains(currentLocation, points);      //if the given point is in the field area
        if (lastState != isInBounds) {
            while (contains(currentLocation,points)!=isInBounds){
                lat -= (up * 0.000001);
                currentLocation=new LatLng(lat,lng);
            }
            if(!contains(currentLocation,points)){
                lat+=(up * 0.000001);
            }
            lastState = isInBounds;
            path.add(currentLocation);
        }
    }


    /* find the minimum and maximum longtitude and langtitude in field */
    public void findfMinAndMaxPoint(ArrayList<LatLng> field){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng point : field) {
            builder.include(point);
            points.add(new LatLng(point.latitude, point.longitude));

            // Take Min and Max longitude and latitude
            double latitude = point.latitude;
            double longitude = point.longitude;

            if (latitude < minLat) {
                minLat = latitude;
            }
            if (latitude > maxLat) {
                maxLat = latitude;
            }
            if (longitude < minLng) {
                minLng = longitude;
                minLngPoint=point;
            }
            if (longitude > maxLng) {
                maxLng = longitude;
                maxLngPoint=point;
            }
        }
        LatLngBounds bounds = builder.build();
    }


    public double getminLat(){
        return minLat;
    }
    public double getmaxLat(){
        return maxLat;
    }
    public double getminLng(){
        return minLng;
    }
    public double getmaxLng(){
        return maxLng;
    }


    /* draw  the drone path on google map */
    public Polyline drawRout(ArrayList<LatLng> fullpPath,GoogleMap map, int lineColor) {
        mGoogleMap = map;
        PolylineOptions polyLine = new PolylineOptions();

        polyLine.addAll(fullpPath);
        polyLine.width(5);
        polyLine.color(lineColor);

        return mGoogleMap.addPolyline(polyLine);
    }

    /* Return true if the given point is contained inside the boundary */
    public boolean contains(LatLng test, ArrayList<LatLng> points) {
        int i;
        int j;
        boolean result = false;
        for (i = 0, j = points.size() - 1; i < points.size(); j = i++) {
            if ((points.get(i).longitude > test.longitude) != (points.get(j).longitude > test.longitude) &&
                    (test.latitude < (points.get(j).latitude - points.get(i).latitude) * (test.longitude - points.get(i).longitude) /
                            (points.get(j).longitude - points.get(i).longitude) + points.get(i).latitude)) {
                result = !result;
            }
        }
        return result;
    }

    /* find latitude on line between 2 given points and longtitude  */
    public double findLatitudeOnPolygon(double longitude,LatLng l1, LatLng l2) {
        double slope=findSlopeLine(l1,l2);

        //Straight equation: y-y1=m(x=x1) =>  y=mx+c => x=(y-c)/m
        double c=(slope*(-l1.latitude))+l1.longitude;
        double latitude=(longitude-c)/slope;
        return latitude;
    }


    /* find the slope of line given 2 points */
    public double findSlopeLine(LatLng l1, LatLng l2){
        double slope=(l2.longitude-l1.longitude)/(l2.latitude-l1.latitude);
        return slope;
    }


    public boolean onPolygonEdge(LatLng point, ArrayList<LatLng> points) {

        if (PolyUtil.isLocationOnPath(point, points,true, 0.000001)) {
            return true;
        }
        return false;
    }

    public ArrayList<LatLng> getDronePath() {
        return path;
    }

    public void deletePath(){
        if(path.size()>0) {
            path.clear();
        }
    }

    /* calculate the distance of drone path */
    public float fieldDistance(ArrayList<LatLng> dronePath){
        float dis=0;
        for(int i=1; i<dronePath.size(); i++){
            dis+= SphericalUtil.computeDistanceBetween(dronePath.get(i - 1), dronePath.get(i));
        }
        return dis;
    }
}
