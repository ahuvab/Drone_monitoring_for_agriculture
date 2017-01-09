package com.example.user.airscort_agriculture.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.airscort_agriculture.Activities.StartScanningActivity;
import com.example.user.airscort_agriculture.Adapters.ListFieldsAdapter;
import com.example.user.airscort_agriculture.Classes.DronePath;
import com.example.user.airscort_agriculture.Classes.Resolution;
import com.example.user.airscort_agriculture.DB.DataAccess;
import com.example.user.airscort_agriculture.Interfaces.FieldsToScan;
import com.example.user.airscort_agriculture.R;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*  Fragment that appear only if the user has fields */
public class HomeFragment extends Fragment implements FieldsToScan {

    private ListView listView;
    private ArrayList<String> array, chosenFields;
    private ListFieldsAdapter adapter;
    private TextView  whichFields;
    private Button scanButton;
    private DataAccess dataAccess;
    private String resolution;
    private DronePath dronePath;
    private int high;
    private PopupWindow popupWindow;
    private Resolution resolutionClass;

    public void HomeFragment() {}

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        dataAccess=new DataAccess(v.getContext());
        whichFields = (TextView) v.findViewById(R.id.fieldsText);
        listView=(ListView)v.findViewById(R.id.listView);
        array=new ArrayList<String>(dataAccess.getNamesFields());
        adapter=new ListFieldsAdapter(v.getContext(), R.layout.single_listview_field_checkbox, array);
        adapter.setFieldsToScan(this);
        listView.setAdapter(adapter);
        listView.setChoiceMode(listView.CHOICE_MODE_MULTIPLE);
        resolution="";
        resolutionClass=new Resolution();
        dronePath=new DronePath();


        chosenFields=new ArrayList<String>();
        scanButton=(Button)v.findViewById(R.id.scanButton);
        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(chosenFields.size()>0) {
                    if (dataAccess.getDateFromScanning().equals("")) {       //check if there have been scanning
                        callPopup(v,inflater);                                       //popup for confirm scanning and select resolution
                    }
                    else{
                        showAlertForFollow(v);
                    }
                }
                else{       //no selected fields
                    Toast.makeText(v.getContext(), getString(R.string.no_chose), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;

    }

    /* dialog for explanation how to follow current mission*/
    private void showAlertForFollow(View v){
        final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage(getString(R.string.busy_drone1)+"\n"+"\n"+ getString(R.string.busy_drone2))
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

    //popup to confirm the selection of fields
    public void callPopup(View v,LayoutInflater inflater){
//        LayoutInflater layoutInflater = (LayoutInflater) v.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.confirm_scanning_popup, null);
        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        TextView titlePop=(TextView)popupView.findViewById(R.id.title);
        ListView fields = (ListView) popupView.findViewById(R.id.listView2);
        Spinner spinner=(Spinner)popupView.findViewById(R.id.resolution);            //spinner foe choose scanning resolution
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    resolution = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //adapter for listview
        ArrayAdapter adapter = new ArrayAdapter<String>(v.getContext(), R.layout.simple_listview_field, R.id.fieldName, chosenFields);
        fields.setAdapter(adapter);

        //adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_spinner_item, resolutionClass.getResolutionList());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        Button ok = (Button) popupView.findViewById(R.id.buttonOk);
        ok.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBeforeScanning(v);
            }
        });
    }

    /* Check if resolution was selected */
    public void checkBeforeScanning(View v) {
        if (resolution.equals("")) {
            Toast.makeText(v.getContext(), getString(R.string.select_resolution), Toast.LENGTH_LONG).show();
        } else {
            popupWindow.dismiss();
            startScannning(v);
        }
    }
    /* start scanning */
    public void startScannning(View v){
        updateDronePath();
        addScan();
        Intent intent=new Intent(v.getContext(), StartScanningActivity.class);
        startActivity(intent);
    }


    /* add scan to DB- to history and to current scanning*/
    public void addScan(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        high=resolutionClass.highForResolution(resolution);
        //add scaning to current mission and to history scanning
        dataAccess.addScanning(dateFormat.format(date), chosenFields, resolution, high);
    }

    /* update drone path for each selected field Depending on the selected resolution */
    public void updateDronePath(){
        for (int i = 0; i<chosenFields.size(); i++){
            ArrayList<LatLng> arr =dataAccess.getFramePath(chosenFields.get(i));
            ArrayList<LatLng> pathD=dronePath.findDronePath(arr, resolution);
            dataAccess.setPath(getString(R.string.full_path),chosenFields.get(i),pathD);
        }
    }

    //implement FieldsToScan interface
    @Override
    public void addField(String nameOfField){
        chosenFields.add(nameOfField);
    }

    @Override
    public void deleteField(String nameOfField){
        chosenFields.remove(nameOfField);
    }

    @Override
    public void clear( ){
        chosenFields.clear();
    }

    @Override
    public ArrayList getFieldsToScan(){
        return chosenFields;
    }

    @Override
    public boolean ifContainField(String name){
        for(int i=0; i<chosenFields.size(); i++){
            if(chosenFields.get(i).equals(name)){
                return true;
            }
        }
        return false;
    }

}
