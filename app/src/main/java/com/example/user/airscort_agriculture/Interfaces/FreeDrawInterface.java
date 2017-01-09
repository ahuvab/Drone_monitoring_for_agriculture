package com.example.user.airscort_agriculture.Interfaces;


import com.example.user.airscort_agriculture.Classes.PointInFloat;

import java.util.ArrayList;

public interface FreeDrawInterface {

    public void convertPixelToLatLng(ArrayList<PointInFloat> pathInPixels);    //convert pixels to point(latitude, longtitude)

    public void drawOnMapFragment();                                          //draw path on map
}
