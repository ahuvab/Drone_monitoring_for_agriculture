package com.example.user.airscort_agriculture.Interfaces;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public interface MapInterface {

    public String getMode();        //mode for editor: draw, edit, delete, show, save

    public String getFieldName();

    public void setFullDronePath(ArrayList<LatLng> arr);    //set drone path

    public ArrayList<LatLng> getFullDronePath();            //get drone path

    public void clearFullDronePath();                       //clear drone path

    public ArrayList<LatLng> getPathFrame();                //get frame path

    public void clearPathFrame();                          //clear frame path

    public LatLng getHomePoint();                         //get home point

    public void finishCreateMap();                        //what to do when the map is ready

    public void chooseField(LatLng position);            //choose field- for scan or for edit

}
