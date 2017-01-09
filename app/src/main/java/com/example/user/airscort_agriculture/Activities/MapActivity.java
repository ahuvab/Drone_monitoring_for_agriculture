package com.example.user.airscort_agriculture.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Toast;

import com.example.user.airscort_agriculture.DB.DataAccess;
import com.example.user.airscort_agriculture.Fragments.MapFragment;
import com.example.user.airscort_agriculture.Interfaces.MapInterface;
import com.example.user.airscort_agriculture.R;
import com.example.user.airscort_agriculture.Classes.RadioButtonView;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/*
This activity allowing to user to define his fields by drawing on map
 */
public class MapActivity extends FragmentActivity implements MapInterface, View.OnClickListener, OnMenuItemClickListener {


    private DataAccess dataAccess;
    private RadioButtonView draw, delete,save, more;    //button on toolbar
    private ImageButton undo;
    private FrameLayout frameLayout;
    private String mode,nameOfField;
    private ArrayList<LatLng> pathFrame;                 //the frame field
    private ArrayList<LatLng> path;                      //full drone path
    private PopupMenu popupDrawMenu, popupOptionMenu;   //for menu options and draw options
    private MapFragment map;                            //map fragment
    private final int MAX_INPUT_LENGTH=25;
    private EditText fieldName;
    private boolean newuUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        frameLayout = (FrameLayout) findViewById(R.id.mapFrameLayout);
        pathFrame = new ArrayList<>();
        path= new ArrayList<>();
        map = new MapFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(frameLayout.getId(), map, "MAP_FRAGMENT")
                .commit();
        map.setMapInterface(this);
        dataAccess=new DataAccess(this);
        nameOfField="";
        mode="";
        draw = (RadioButtonView) findViewById(R.id.editor_tools_draw);
        delete = (RadioButtonView) findViewById(R.id.editor_tools_trash);
        undo =(ImageButton) findViewById(R.id.editor_tools_undo);
        save = (RadioButtonView) findViewById(R.id.editor_tools_save);
        more = (RadioButtonView) findViewById(R.id.editor_tools_more);
        draw.setOnClickListener(this);
        delete.setOnClickListener(this);
        undo.setOnClickListener(this);
        save.setOnClickListener(this);
        more.setOnClickListener(this);

        //draw options- waypoint or continuous
        popupDrawMenu = new PopupMenu(this, findViewById(R.id.drawOptions));
        popupDrawMenu.getMenuInflater().inflate(R.menu.draw_options, popupDrawMenu.getMenu());
        popupDrawMenu.setOnMenuItemClickListener(this);

        //more option- show exist fields or move to choose fields to scan
        popupOptionMenu=new PopupMenu(this, findViewById(R.id.moreOptions));
        popupOptionMenu.getMenuInflater().inflate(R.menu.more_option_tool, popupOptionMenu.getMenu());
        popupOptionMenu.setOnMenuItemClickListener(this);

        findViewById(R.id.drawOptions).setOnClickListener(this);
        findViewById(R.id.moreOptions).setOnClickListener(this);

        if(dataAccess.getNamesFields().size()>0){
            newuUser=false;
        }
        else{
            newuUser=true;
        }

    }

    /* explain to user how to define field */
    private void showDrawExplain(String message){
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setIcon(R.drawable.ic_help)
                .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editor_tools_draw:             //draw on map
                popupDrawMenu.show();
                break;

            case R.id.editor_tools_trash:           //delete the drawing
                mode = getString(R.string.delete_option);
                map.stopDrawFree();
                if(getPathFrame().size()>0) {       //if there is a drawn field
                    showDialogForDelete();   //dialog to confirm the delete
                }
                else{
                    Toast.makeText(MapActivity.this, getString(R.string.no_field_to_delete), Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.editor_tools_undo:           //cancel last drawing
                map.undo();
                break;

            case R.id.editor_tools_save:           //save the painted field
                mode = getString(R.string.save_option);
                map.stopDrawFree();
                //input field's name dialog
                if(map.getIsPathReady()) {          //check if field was defines
                    showInputDialog();
                }
                else {
                    Toast.makeText(MapActivity.this, getString(R.string.define_close_area), Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.editor_tools_more:           //more option-show exist fields or move to choose fields to scan
                map.stopDrawFree();
                popupOptionMenu.show();
                break;
        }
    }

    /* dialog for deleting */
    public void showDialogForDelete(){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("delete field")
                .setMessage(getString(R.string.delete_field))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        map.deletePath();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    /* draw and more options */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()) {
            //draw options
            case R.id.waypoint:                             //waypoint drawing
                map.setIsFreeDraw(false);
                mode = getString(R.string.waypoint_draw);
                map.stopDrawFree();
                break;
            case R.id.continuous:                           //continuous drawing
                mode = getString(R.string.continu_draw);
                clearPathFrame();
                map.deletePath();
                map.drawFree();
                break;

            //menu options
            case R.id.show:                                 //show fields
                mode = getString(R.string.show_option);
                ArrayList<String> names=dataAccess.getNamesFields();
                if (names.size() > 0) {                  //show all fields if defined
                    Intent intent = new Intent(this, ShowFieldsActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MapActivity.this, getString(R.string.no_fields), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.scan:                                 //go to scan
                map.stopDrawFree();
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    /* dialog for enter field name */
    public void showInputDialog(){
        LayoutInflater layoutInflater = LayoutInflater.from(MapActivity.this);
        View promptView = layoutInflater.inflate(R.layout.enter_field_name_dialog, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapActivity.this);
        alertDialogBuilder.setView(promptView);

        fieldName = (EditText) promptView.findViewById(R.id.fieldName);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();

        Button theButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new CustomListener(alert));
    }

    class CustomListener implements View.OnClickListener {
        private final Dialog dialog;
        public CustomListener(Dialog dialog) {
            this.dialog = dialog;
        }
        @Override
        public void onClick(View v) {
            if (!fieldName.getText().toString().equals("")) {   //check if enter field's name
                if (fieldName.length() > MAX_INPUT_LENGTH) {
                    Toast.makeText(MapActivity.this, getString(R.string.long_name), Toast.LENGTH_LONG).show();
                } else {
                    nameOfField = fieldName.getText().toString();
                    if (dataAccess.FieldNameHasExist(nameOfField)) {       //if field's name already exist
                        Toast.makeText(MapActivity.this, getString(R.string.field_name_has_exist), Toast.LENGTH_LONG).show();
                    } else {
                        dialog.dismiss();
                        saveField();
                    }
                }
            } else {
                Toast.makeText(MapActivity.this, getString(R.string.name_for_field), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void saveField(){
        map.resetPointsCounter();                      //reset points counter
        path=map.getDronePath();                      //get full path
        float distance= map.fieldDistance(path);     //distance of the field
        Toast.makeText(MapActivity.this, "The field " + nameOfField + " is saved", Toast.LENGTH_LONG).show();
        dataAccess.addField(nameOfField, pathFrame, path, distance);         //add field to DB
        map.deletePath();

    }

    @Override
    public void onBackPressed(){}


    //implement mapInterface
    public String getMode(){
        return mode;
    }

    public String getFieldName(){
        return nameOfField;
    }

    public void setFullDronePath(ArrayList<LatLng> arr){
        path=arr;
    }

    public ArrayList<LatLng> getFullDronePath(){
        return path;
    }

    public void clearFullDronePath(){
        path.clear();
    }

    public ArrayList<LatLng> getPathFrame(){
        return pathFrame;
    }

    public void clearPathFrame(){
        pathFrame.clear();
    }
    public LatLng getHomePoint(){
        return dataAccess.getHomePoint();
    }

    public void finishCreateMap(){
        map.addHomeMarker(getHomePoint());
        if(newuUser){       //if he's a new user explain him how to draw field
            showDrawExplain(getString(R.string.draw_explain1)+"\n"+"\n"+
                    getString(R.string.draw_explain2) +"\n"+"\n"+ getString(R.string.draw_explain3));
        }
    }

    public void chooseField(LatLng position){}

}
