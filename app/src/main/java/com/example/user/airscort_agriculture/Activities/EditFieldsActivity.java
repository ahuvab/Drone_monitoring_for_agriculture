package com.example.user.airscort_agriculture.Activities;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.user.airscort_agriculture.DronePath;
import com.example.user.airscort_agriculture.MapFragment;
import com.example.user.airscort_agriculture.MapInterface;
import com.example.user.airscort_agriculture.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_fields);

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            fieldName=bundle.getString(getString(R.string.field_name));
        }
        actionBar = getSupportActionBar();
        actionBar.setTitle("Edit " + fieldName + " field");     // set title for activity
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        frameLayout = (FrameLayout) findViewById(R.id.mapFrameLayout);
        map = new MapFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(frameLayout.getId(), map, "MAP_FRAGMENT")
                .commit();
        map.setMapInterface(this);

        dataAccess = new DataAccess(this);
        homePoint = dataAccess.getHomePoint();
        mode=getString(R.string.edit_option);
        dronePath=new DronePath(map.getMap());
    }

    public LatLng findCenterField(){
        dronePath.finfMinAndMaxPoint(pathFrame);
        double lat=(dronePath.getminLat()+dronePath.getmaxLat())/2;
        double lng=(dronePath.getminLng()+dronePath.getmaxLng())/2;
        return new LatLng(lat,lng);
    }

    //overflow
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
        dataAccess.setPath("frame", fieldName, pathFrame);
        dataAccess.setPath("fullPath", fieldName, fullPath);
        float distance=map.fieldDistance(fullPath);
        dataAccess.setFieldDistance(fieldName, distance);
        Toast.makeText(EditFieldsActivity.this, "Changes saved ", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, ShowFieldsActivity.class);
        startActivity(intent);
    }

    private void alertForDelete(){
        new android.app.AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("delete field")
                .setMessage("Are you sure you want to delete "+fieldName+ " field?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataAccess.deleteField(fieldName);
                        Intent intent = new Intent(EditFieldsActivity.this, ShowFieldsActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    //dialog for field name
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
                            dialog.dismiss();

                            if (dataAccess.FieldNameHasExist(newFieldName.getText().toString())) {
                                Toast.makeText(EditFieldsActivity.this, getString(R.string.field_name_has_exist), Toast.LENGTH_LONG).show();
                            }
                            else {
                                dataAccess.updateFieldName(fieldName, newFieldName.getText().toString());

                                Toast.makeText(EditFieldsActivity.this,
                                        "the field name had change from "+fieldName + " to  "+ newFieldName.getText().toString(),
                                        Toast.LENGTH_LONG).show();
                                fieldName=newFieldName.getText().toString();
                                actionBar.setTitle("Edit "+ fieldName+" field");
                            }
                        }
                        else {
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
        map.drawPolygonWithMarker(pathFrame);
        map.drawDronePath(fullPath);

        LatLng targetPos=findCenterField();
        map.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(targetPos, 15));
    }

    public void chooseFieldToScan(LatLng position){}
}
