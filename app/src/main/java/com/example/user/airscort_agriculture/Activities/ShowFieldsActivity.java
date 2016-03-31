package com.example.user.airscort_agriculture.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.user.airscort_agriculture.DataAccess;
import com.example.user.airscort_agriculture.MapFragment;
import com.example.user.airscort_agriculture.MapInterface;
import com.example.user.airscort_agriculture.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class ShowFieldsActivity extends AppCompatActivity implements MapInterface {

    private MapFragment map;
    private FrameLayout frameLayout;
    private LatLng homePoint;
    private ArrayList<LatLng> pathFrame;
    private ArrayList<LatLng> fullPath;
    private ArrayList<String> fieldsName;
    private DataAccess dataAccess;
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_fields);

        frameLayout = (FrameLayout) findViewById(R.id.mapFrameLayout);
        map = new MapFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(frameLayout.getId(), map, "MAP_FRAGMENT")
                .commit();
        map.setMapInterface(this);

        dataAccess = new DataAccess(this);
        homePoint = dataAccess.getHomePoint();
        mode = getString(R.string.show_option);
    }

    //overflow
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_for_show_fields_page, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.add:
                Intent intent=new Intent(this, MapActivity.class);
                startActivity(intent);
                break;
            case R.id.edit:
                //TODO: edit fields
                break;
            case R.id.scan:
                Intent intent2 = new Intent(this, ChooseFieldsToScanActivity.class);
                startActivity(intent2);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    //implements map interface
    public void finishCreateMap(){
        map.addHomeMarker(homePoint);
        fieldsName=dataAccess.getNamesFields();

        for(int i=0; i<fieldsName.size(); i++){
            pathFrame=dataAccess.getFramePath(fieldsName.get(i));
            fullPath = dataAccess.getDronePath(fieldsName.get(i));
            map.drawPolygon(pathFrame);
            map.drawDronePath(fullPath);
        }
    }

    public String getMode(){
        return mode;
    }

    public String getFieldName(){
        return null;
    }

    public void setFullDronePath(ArrayList<LatLng> arr){}

    public ArrayList<LatLng> getFullDronePath(){
        return null;
    }

    public void clearFullDronePath(){}

    public ArrayList<LatLng> getPathFrame(){
        return null;
    }

    public void clearPathFrame(){}

    public LatLng getHomePoint(){
        return homePoint;
    }


}
