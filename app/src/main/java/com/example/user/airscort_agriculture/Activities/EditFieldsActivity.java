package com.example.user.airscort_agriculture.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.airscort_agriculture.DB.DataAccess;
import com.example.user.airscort_agriculture.Classes.DronePath;
import com.example.user.airscort_agriculture.Fragments.MapFragment;
import com.example.user.airscort_agriculture.Interfaces.MapInterface;
import com.example.user.airscort_agriculture.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/*
this activity allowing to user to edit exist field: change field area, change field name or delete field.
 */
public class EditFieldsActivity extends AppCompatActivity implements MapInterface{

    private MapFragment map;
    private FrameLayout frameLayout;
    private LatLng homePoint;
    private DataAccess dataAccess;
    private String fieldName;
    private String mode;
    private ArrayList<LatLng> pathFrame;
    private ArrayList<LatLng> fullPath;
    private DronePath dronePath;
    private ActionBar actionBar;
    private final int MAX_INPUT_LENGTH=25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_fields);

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){                                             //get field for edit
            fieldName=bundle.getString(getString(R.string.field_name));
        }
        actionBar = getSupportActionBar();
        actionBar.setTitle("Edit " + fieldName + " field");     // set title for activity
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        dataAccess = new DataAccess(this);
        dronePath=new DronePath();
        frameLayout = (FrameLayout) findViewById(R.id.mapFrameLayout);
        map = new MapFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(frameLayout.getId(), map, "MAP_FRAGMENT")
                .commit();
        map.setMapInterface(this);

        homePoint = dataAccess.getHomePoint();
        mode=getString(R.string.edit_option);             //edit mode
    }

    //edit options-overflow
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_for_edit_field_page, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.save:           //save changes
               saveChanges();
                break;
            case R.id.delete:           //delete field
                alertForDelete();
                break;
            case R.id.change:           //change field name
                dialogForChangeName();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveChanges(){
        pathFrame=getPathFrame();
        fullPath=getFullDronePath();
        dataAccess.setPath(getString(R.string.field_name), fieldName, pathFrame);     //save changes in DB
        dataAccess.setPath(getString(R.string.full_path), fieldName, fullPath);
        float distance=map.fieldDistance(fullPath);
        dataAccess.setFieldDistance(fieldName, distance);                              //update the field's distance
        Toast.makeText(EditFieldsActivity.this, "Changes saved ", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, ShowFieldsActivity.class);
        startActivity(intent);
    }

    /* retrun the center of the field */
    public LatLng findCenterField(){
        dronePath.findfMinAndMaxPoint(pathFrame);
        double lat=(dronePath.getminLat()+dronePath.getmaxLat())/2;
        double lng=(dronePath.getminLng()+dronePath.getmaxLng())/2;
        return new LatLng(lat,lng);
    }

    /* dialog for confirm the deleting field */
    private void alertForDelete(){
        new android.app.AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("delete field")
                .setMessage("Are you sure you want to delete "+fieldName+ " field?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataAccess.deleteField(fieldName);                //delete from DB
                        Intent intent = new Intent(EditFieldsActivity.this, ShowFieldsActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


   /* dialog for enter new field name */
    public void dialogForChangeName(){
        LayoutInflater layoutInflater = LayoutInflater.from(EditFieldsActivity.this);
        View promptView = layoutInflater.inflate(R.layout.enter_field_name_dialog, null);
        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EditFieldsActivity.this);
        alertDialogBuilder.setView(promptView);
        TextView textView=(TextView) promptView.findViewById(R.id.title);
        textView.setText("Enter new field's name");
        final EditText newFieldName = (EditText) promptView.findViewById(R.id.fieldName);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (!newFieldName.getText().toString().equals("")) {   //check if enter field's name
                            if (newFieldName.length() > MAX_INPUT_LENGTH) {       //if the input is too big
                                Toast.makeText(EditFieldsActivity.this, getString(R.string.long_name), Toast.LENGTH_LONG).show();
                            } else {
                                dialog.dismiss();
                                if (dataAccess.FieldNameHasExist(newFieldName.getText().toString())) {     //if the name has exist
                                    Toast.makeText(EditFieldsActivity.this, getString(R.string.field_name_has_exist), Toast.LENGTH_LONG).show();
                                } else {
                                    dataAccess.updateFieldName(fieldName, newFieldName.getText().toString());

                                    Toast.makeText(EditFieldsActivity.this,
                                            "the field name had change from " + fieldName + " to  " + newFieldName.getText().toString(),
                                            Toast.LENGTH_LONG).show();
                                    fieldName = newFieldName.getText().toString();
                                    actionBar.setTitle("Edit " + fieldName + " field");
                                }
                            }
                        } else {
                            Toast.makeText(EditFieldsActivity.this, getString(R.string.name_for_field), Toast.LENGTH_LONG).show();
                            //TODO: keep dialog open
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        // create an alert dialog
        android.app.AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onBackPressed(){}

    //implements map interface
    public String getMode(){
        return mode;
    }

    public String getFieldName(){
        return fieldName;
    }

    public void setFullDronePath(ArrayList<LatLng> arr){
        fullPath=arr;
    }

    public ArrayList<LatLng> getFullDronePath(){
        return fullPath;
    }

    public void clearFullDronePath(){
        fullPath.clear();
    }

    public ArrayList<LatLng> getPathFrame(){return pathFrame;}

    public void clearPathFrame(){
    }

    public LatLng getHomePoint(){return homePoint;}

    public void finishCreateMap(){
        pathFrame=dataAccess.getFramePath(fieldName);
        fullPath = dataAccess.getDronePath(fieldName);
        map.drawPolygonWithMarker(pathFrame, Color.GREEN);         //draw the edited field on map
        LatLng targetPos=findCenterField();                        //place field in the center of the map
        map.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(targetPos, 15));
    }

    public void chooseField(LatLng position){}
}
