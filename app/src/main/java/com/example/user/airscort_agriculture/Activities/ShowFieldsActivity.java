package com.example.user.airscort_agriculture.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.user.airscort_agriculture.DB.DataAccess;
import com.example.user.airscort_agriculture.Classes.DronePath;
import com.example.user.airscort_agriculture.Fragments.MapFragment;
import com.example.user.airscort_agriculture.Interfaces.MapInterface;
import com.example.user.airscort_agriculture.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/*
this activity allows to user to view his fields on the map
 */
public class ShowFieldsActivity extends AppCompatActivity implements MapInterface {

    private MapFragment map;
    private FrameLayout frameLayout;
    private LatLng homePoint;                //home station
    private ArrayList<LatLng> pathFrame;
    private ArrayList<String> fieldsName;    //user's fields
    private DataAccess dataAccess;
    private String mode;                    //mode in editor- show
    private DronePath dronePath;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_fields);
        actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.show_all_fields));     // set title for activity

        frameLayout = (FrameLayout) findViewById(R.id.mapFrameLayout);      //map fragment
        map = new MapFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(frameLayout.getId(), map, "MAP_FRAGMENT")
                .commit();
        map.setMapInterface(this);

        dataAccess = new DataAccess(this);
        homePoint = dataAccess.getHomePoint();
        fieldsName=dataAccess.getNamesFields();
        mode = getString(R.string.show_option);
        dronePath=new DronePath();
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
            case R.id.add:                                     //add new field
                Intent intent=new Intent(this, MapActivity.class);
                startActivity(intent);
                break;
            case R.id.edit:                                    //edit field
                mode=getString(R.string.edit_option);
                showAlertForEdit();            //explain how choose field to edit
                break;
            case R.id.delete:                                 //delete all fields
                mode=getString(R.string.delete_option);
                if (dataAccess.getDateFromScanning().equals("")) {        //if there is a current scanning- can't delete fields
                    alertForDelete();
                }
                else{
                    Toast.makeText(ShowFieldsActivity.this, getString(R.string.cant_delete), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.scan:                                    //go to choose fields to scan
                ArrayList<String> names=dataAccess.getNamesFields();
                if (names.size() > 0) {
                    Intent intent3 = new Intent(this, ChooseFieldsToScanActivity.class);
                    startActivity(intent3);
                }
                else {                                          //if there is no fields
                    Toast.makeText(ShowFieldsActivity.this, getString(R.string.no_fields), Toast.LENGTH_LONG).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /* dialog for confirm the deleting field */
    private void alertForDelete(){
        new android.app.AlertDialog.Builder(ShowFieldsActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("delete fields")
                .setMessage("Are you sure you want to delete all fields?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataAccess.deleteAllFields();
                        Intent intent = new Intent(ShowFieldsActivity.this, MapActivity.class);
                        Toast.makeText(ShowFieldsActivity.this, "You have to define fields", Toast.LENGTH_LONG).show();
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    /* dialog for explanation how to choose field for edit */
    private void showAlertForEdit(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.tap_field))
                .setCancelable(false)
                .setIcon(R.drawable.ic_info)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /* check the location of user's click */
    public String checkChosenField(LatLng position){
        String field="";
        for(int i=0; i<fieldsName.size(); i++){
            if(dronePath.contains(position,dataAccess.getFramePath(fieldsName.get(i)))){
                field=fieldsName.get(i);
                break;
            }
        }
        return field;
    }

    @Override
    public void onBackPressed(){}

    //implements map interface
    public void finishCreateMap(){
        map.addHomeMarker(homePoint);
        for(int i=0; i<fieldsName.size(); i++){
            pathFrame=dataAccess.getFramePath(fieldsName.get(i));
            map.drawPolygon(pathFrame, Color.GREEN);
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

    //if click on map- check if field has been chosen
    public void chooseField(LatLng position){
        String field=checkChosenField(position);          // check the position

        if(!field.equals("")) {                           //if the click was inside any user's field
            ArrayList<String> arr=dataAccess.getFieldsFromScanning();
            int i=0;
            for(; i<arr.size(); i++){
                if(arr.get(i).equals(field)){              //if the field for edit is scanning now
                    Toast.makeText(ShowFieldsActivity.this, getString(R.string.cant_edit), Toast.LENGTH_LONG).show();
                    break;
                }
            }
            if(i==arr.size()){                             //if the field is not scanning now-move to edit
                Intent intent=new Intent(this, EditFieldsActivity.class);
                intent.putExtra(getString(R.string.field_name),field);
                startActivity(intent);
            }
        }
        else{
            Toast.makeText(ShowFieldsActivity.this, getString(R.string.not_chosen_field), Toast.LENGTH_LONG).show();
        }
    }
}
