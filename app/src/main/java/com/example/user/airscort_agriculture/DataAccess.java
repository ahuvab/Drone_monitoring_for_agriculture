package com.example.user.airscort_agriculture;


import android.content.Context;

import com.example.user.airscort_agriculture.LocalDB.LocalDB;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataAccess {

    private LocalDB localDB;
    //TODO: conect to server

    public DataAccess(Context c){
        localDB=new LocalDB(c);
    }

    public void addField(String name, ArrayList<LatLng> pathFrame,ArrayList<LatLng> dronePath, float distance){
        ArrayList<String>latFrame=new ArrayList();     //split each Arraylist <LatLng to 2 Array- latitude and longtitude
        ArrayList<String>lonFrame=new ArrayList();
        ArrayList<String>latfullPath=new ArrayList();
        ArrayList<String>lonFullPath=new ArrayList();

        for(int i=0; i<pathFrame.size(); i++){
            latFrame.add(Double.toString(pathFrame.get(i).latitude));
            lonFrame.add(Double.toString(pathFrame.get(i).longitude));
        }
        for(int i=0; i<dronePath.size(); i++){
            latfullPath.add(Double.toString(dronePath.get(i).latitude));
            lonFullPath.add(Double.toString(dronePath.get(i).longitude));
        }

        String frameArrayLat = convertArrayListToString(latFrame);     //convert each arraylist to string
        String frameArrayLon = convertArrayListToString(lonFrame);
        String droePathArrayLat = convertArrayListToString(latfullPath);
        String droePathArrayLon = convertArrayListToString(lonFullPath);

        localDB.addField(name,frameArrayLat,frameArrayLon,droePathArrayLat,droePathArrayLon,distance);
        //TODO: addField to server
    }

    public String convertArrayListToString(ArrayList<String> arr){
        JSONObject framePathJson = new JSONObject();
        try {
            framePathJson.put("array", new JSONArray(arr));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String frameArrayLat = framePathJson.toString();
        return frameArrayLat;
    }

    public ArrayList getNamesFields(){
        return localDB.getNamesFields();
    }

    public float getDistance(String fieldsName){
        return localDB.getDistance(fieldsName);
    }

    public ArrayList<LatLng> getDronePath(String fieldsName){
        return localDB.getDronePath(fieldsName);
    }

    public ArrayList<LatLng> getFramePath(String fieldsName){
        return localDB.getFramePath(fieldsName);
    }

    public void deleteField(String name){
        localDB.deleteField(name);
        //TODO: delete from server
    }

    public void deleteAllFields(){
        localDB.deleteAllFields();
        //TODO: delete from server
    }

    //return current location of the drone
    public LatLng getDroneLoation(){
        //TODO:GET THE LOCATION FROM SERVER
        return new LatLng(32.578481, 35.266195);
    }

    public boolean FieldNameHasExist(String newName){
        return localDB.nameHasExist(newName);
    }

    public LatLng getHomePoint(){
        return new LatLng(32.574511, 35.264361);
    }

    public void updateFieldName(String oldName, String newName){
        localDB.updateFieldName(oldName,newName);
        //TODO: update in server
    }

    public void setPath(String whichPath,String name,ArrayList<LatLng> path){
        ArrayList<String> lat=new ArrayList();     //split each Arraylist <LatLng to 2 Array- latitude and longtitude
        ArrayList<String> lon=new ArrayList();

        for(int i=0; i<path.size(); i++){
            lat.add(Double.toString(path.get(i).latitude));
            lon.add(Double.toString(path.get(i).longitude));
        }

        String frameArrayLat = convertArrayListToString(lat);     //convert each arraylist to string
        String frameArrayLon = convertArrayListToString(lon);

        if(whichPath.equals("frame")) {
            localDB.setFramePath(name, frameArrayLat, frameArrayLon);

        }
        else{                 //full path
            localDB.setDronePath(name, frameArrayLat,frameArrayLon);
            //TODO: set in server
        }
    }

    public void setFieldDistance(String name, float distance){
        localDB.setFieldDistance(name, distance);
    }



}
