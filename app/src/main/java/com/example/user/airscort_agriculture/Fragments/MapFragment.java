package com.example.user.airscort_agriculture.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;


import com.example.user.airscort_agriculture.Classes.DronePath;
import com.example.user.airscort_agriculture.Classes.FreeDrawView;
import com.example.user.airscort_agriculture.Classes.PointInFloat;
import com.example.user.airscort_agriculture.Interfaces.FreeDrawInterface;
import com.example.user.airscort_agriculture.Interfaces.MapInterface;
import com.example.user.airscort_agriculture.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;


import java.util.ArrayList;
import java.util.List;

/*
This fragment implements a google map.
 */
public class MapFragment extends Fragment implements FreeDrawInterface, GoogleMap.OnMapClickListener,GoogleMap.OnMarkerClickListener,
                                                      GoogleMap.OnMarkerDragListener{

    private ImageButton returnHome;
    private MapView mMapView;
    private GoogleMap googleMap;
    private String markerTitle;
    private int pointsCounter;
    private MapInterface mMapInterface;
    private final String WAYPOINT_DRAW="waypoint_draw";
    private final String CONTINNU_DRAW="continu_draw";
    private final String EDIT_FIELDS="Edit";
    private DronePath dronePath;
    private FreeDrawView mFreeDrawView;
    private boolean isFreeDraw, isPathReady;
    private final String RESOLUTION="medium";          //Default resolution
    private final int MAX_FIELD_SIZE=2000000;          //the maximum size of field to scan

    public void MapFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = (MapView)v.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately
        googleMap = mMapView.getMap();
        mFreeDrawView =(FreeDrawView)v.findViewById(R.id.view);
        mFreeDrawView.setVisibility(View.INVISIBLE);
        mFreeDrawView.setFreeDrawInterface(this);
        markerTitle="";
        pointsCounter=0;                             //count points in field.
        isFreeDraw=false;
        isPathReady=false;

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //button for return to home point in the map
        returnHome=(ImageButton)v.findViewById(R.id.returnHomeButton);
        returnHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LatLng position=mMapInterface.getHomePoint();
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 14));
            }
        });

        // place map to home point
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(mMapInterface.getHomePoint()).zoom(14).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
        googleMap.setMyLocationEnabled(true);

        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMarkerDragListener(this);
        dronePath = new DronePath();
        mMapInterface.finishCreateMap();
        return v;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        switch(mMapInterface.getMode()) {
            case WAYPOINT_DRAW:
                mMapInterface.getPathFrame().add(latLng);
                googleMap.clear();
                drawLine();
                break;
            case EDIT_FIELDS:
                mMapInterface.chooseField(latLng);
                break;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if( marker.getPosition().longitude!=mMapInterface.getHomePoint().longitude &&     //if the marker is not home point marker
                marker.getPosition().latitude!=mMapInterface.getHomePoint().latitude) {
            //if marker had click, draw a drone path inside the frame path
            if (mMapInterface.getMode().equals(WAYPOINT_DRAW)) {
                if(!ifFieldTooBig()){
                    drawPolygonWithMarker(mMapInterface.getPathFrame(),Color.WHITE);
                    dronePath.findDronePath(mMapInterface.getPathFrame(), RESOLUTION);
                    dronePath.drawRout(getDronePath(), googleMap, Color.GREEN);
                    isPathReady=true;
                    mMapInterface.setFullDronePath(getDronePath());
                    Toast.makeText(getContext(), getString(R.string.edit_explain), Toast.LENGTH_LONG).show();
                }
                else{
                    deletePath();
                }
            }
        }
        return true;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        if(mMapInterface.getMode().equals(WAYPOINT_DRAW) || (mMapInterface.getMode().equals(CONTINNU_DRAW))||
                mMapInterface.getMode().equals(EDIT_FIELDS)) {
            markerTitle = marker.getTitle();
        }
        else{
           //TODO not draggable markers
        }
    }

    @Override
    public void onMarkerDrag(Marker marker) {}

    @Override
    public void onMarkerDragEnd(Marker marker) {
        if(mMapInterface.getMode().equals(WAYPOINT_DRAW) || (mMapInterface.getMode().equals(CONTINNU_DRAW)) ||
                mMapInterface.getMode().equals(EDIT_FIELDS)) {
            if (marker != null) {
                marker.remove();
            }
            LatLng oldPosition=new LatLng(0,0);
            int i;
            for (i = 0; i < mMapInterface.getPathFrame().size(); i++) {          //change the marker position
                if (i==Integer.parseInt(markerTitle)) {
                    marker = googleMap.addMarker(new MarkerOptions().position(marker.getPosition()));
                    oldPosition=(mMapInterface.getPathFrame().get(i));
                    mMapInterface.getPathFrame().set(i, marker.getPosition());
                    break;
                }
            }
            if(ifFieldTooBig()){
                mMapInterface.getPathFrame().set(i, oldPosition);
            }
            googleMap.clear();
            if(mMapInterface.getMode().equals(WAYPOINT_DRAW) || (mMapInterface.getMode().equals(CONTINNU_DRAW))) {
                drawPolygonWithMarker(mMapInterface.getPathFrame(), Color.WHITE);
                mMapInterface.setFullDronePath(dronePath.findDronePath(mMapInterface.getPathFrame(), RESOLUTION));
                dronePath.drawRout(getDronePath(), googleMap, Color.GREEN);
            }
            else{                                                 //if edit mode- draw only the frame path
                drawPolygonWithMarker(mMapInterface.getPathFrame(),Color.GREEN);
                mMapInterface.setFullDronePath(dronePath.findDronePath(mMapInterface.getPathFrame(), RESOLUTION));
            }
            addHomeMarker(mMapInterface.getHomePoint());
            mMapInterface.setFullDronePath(getDronePath());
        }
    }

    public void drawLine(){
        if(mMapInterface.getPathFrame().size()>=1){                    //add marker for each point in field
            googleMap.addMarker(new MarkerOptions().position(mMapInterface.getPathFrame().get(0))
                    .title(pointsCounter+"")).setDraggable(true);
            pointsCounter++;
        }
        if( mMapInterface.getPathFrame().size()>=2) {                 //if the field has 2 point and more- draw lines beween points
            for (int i = 1; i< mMapInterface.getPathFrame().size(); i++) {
                googleMap.addMarker(new MarkerOptions().position(mMapInterface.getPathFrame().get(i))
                        .title(pointsCounter+"")).setDraggable(true);
                pointsCounter++;
                PolylineOptions options = new PolylineOptions()
                        .add( mMapInterface.getPathFrame().get(i - 1)).add( mMapInterface.getPathFrame().get(i))
                        .color(Color.WHITE).width(5);
                googleMap.addPolyline(options);
            }
        }
        addHomeMarker(mMapInterface.getHomePoint());
    }

    /* draw polygon on map givan arraylist of point and color. each point has marker */
    public void drawPolygonWithMarker(List<LatLng> path, int color) {
        PolygonOptions rectOptions = new PolygonOptions();
        for(int i=0; i<path.size(); i++){
            rectOptions.add(path.get(i));
            googleMap.addMarker(new MarkerOptions().position(path.get(i)).title(i+"")).setDraggable(true);
        }
        rectOptions.strokeColor(color);
        rectOptions.strokeWidth(5);
        googleMap.addPolygon(rectOptions);
    }

    /* draw polygon on map givan arraylist of point and color. */
    public void drawPolygon(List<LatLng> path, int color) {
        PolygonOptions rectOptions = new PolygonOptions();
        rectOptions.addAll(path);
        rectOptions.strokeColor(color);
        rectOptions.strokeWidth(4);
        googleMap.addPolygon(rectOptions);
    }


    public void addHomeMarker(LatLng point){
        Bitmap marker =  BitmapFactory.decodeResource(getResources(), (R.drawable.home_marker2));
        double droneHomeMarkerScale = 0.08;
        int newWidth = (int) (marker.getWidth()  * droneHomeMarkerScale);
        int newHeight = (int) (marker.getHeight() * droneHomeMarkerScale);
        Bitmap bitmap = Bitmap.createScaledBitmap(marker, newWidth, newHeight, true);

        if( googleMap != null ) {
             googleMap.addMarker(new MarkerOptions()
                     .position(point).title("Home")
                     .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                     .anchor(0.5f, 0.9f));
        }
    }

    public void addDroneMarker(LatLng point){
        Bitmap marker =  BitmapFactory.decodeResource(getResources(), (R.drawable.drone8));
        double droneHomeMarkerScale = 1.4;
        int newWidth = (int) (marker.getWidth()  * droneHomeMarkerScale);
        int newHeight = (int) (marker.getHeight() * droneHomeMarkerScale);
        Bitmap bitmap = Bitmap.createScaledBitmap(marker, newWidth, newHeight, true);

        if( googleMap != null ) {
            googleMap.addMarker(new MarkerOptions()
                    .position(point).title("Current_Location")
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                    .anchor(0.5f, 0.9f));
        }
    }
    public void addMarker(LatLng point){
        googleMap.addMarker(new MarkerOptions().position(point));
    }

    public void resetPointsCounter(){
        pointsCounter=0;
    }
    public void drawFree(){
        mFreeDrawView.setVisibility(View.VISIBLE);
    }

    public void stopDrawFree() {
        mFreeDrawView.cleanCanvas();
        mFreeDrawView.setVisibility(View.INVISIBLE);
    }

    /* checkte size of the field*/
    public boolean ifFieldTooBig(){
        if (SphericalUtil.computeArea(mMapInterface.getPathFrame()) > MAX_FIELD_SIZE) {     //if the field is too big
            Toast.makeText(getContext(), getString(R.string.field_too_big), Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    public boolean getIsPathReady(){
        return isPathReady;
    }

    public GoogleMap getMap(){
        return googleMap;
    }

    public void deletePath(){
        mMapInterface.clearPathFrame();
        isPathReady=false;
        mMapInterface.clearFullDronePath();
        dronePath.deletePath();
        googleMap.clear();
        addHomeMarker(mMapInterface.getHomePoint());
    }

    /* cancel the last drawing */
    public void undo(){
        if(!isFreeDraw) {     //in waypoint mode delete the last point
            int size = mMapInterface.getPathFrame().size();
            if (size >= 1) {
                isPathReady=false;
                mMapInterface.getPathFrame().remove(size - 1);
                pointsCounter--;
                googleMap.clear();
                drawLine();
            }
        }
        else{                 //in continu mode delete all path
            deletePath();
            drawFree();
        }
    }

    public void setIsFreeDraw(boolean free){
        isFreeDraw=free;
    }

    public ArrayList<LatLng> getDronePath(){
        return dronePath.getDronePath();
    }

    /* draw drone path on map */
    public void drawDronePath(ArrayList<LatLng> arr){
        dronePath.drawRout(arr,googleMap,Color.GREEN);
    }

    /* return the field's distance in meters*/
    public float fieldDistance(ArrayList<LatLng> path){
        return dronePath.fieldDistance(path);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public void setMapInterface( MapInterface m ) {
        mMapInterface=m;
    }

    //implements FreeDrawInterface

    //convert area from view to map
    public void convertPixelToLatLng(ArrayList<PointInFloat> pathInPixels){
        for(int i=0; i<pathInPixels.size(); i++){
           LatLng latLng = googleMap.getProjection().
                    fromScreenLocation(new Point((int) pathInPixels.get(i).getX(), (int) pathInPixels.get(i).getY()));
            mMapInterface.getPathFrame().add(latLng);
        }
    }

    //invisible view and draw free path on the map
    public void drawOnMapFragment(){
        if(!ifFieldTooBig()){
            isFreeDraw = true;
            mFreeDrawView.setVisibility(View.INVISIBLE);
            mFreeDrawView.cleanCanvas();
            drawPolygonWithMarker(mMapInterface.getPathFrame(), Color.WHITE);

            dronePath.findDronePath(mMapInterface.getPathFrame(), RESOLUTION);
            dronePath.drawRout(getDronePath(), googleMap, Color.GREEN);
            isPathReady=true;
            mMapInterface.setFullDronePath(getDronePath());
            Toast.makeText(getContext(), getString(R.string.edit_explain), Toast.LENGTH_LONG).show();
        }
        else{
            deletePath();
        }
    }
}

