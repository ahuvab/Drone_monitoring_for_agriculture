package com.example.user.airscort_agriculture.DB;


import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.user.airscort_agriculture.R;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataAccess {

    private LocalDB localDB;
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor spEditor;
    private ServerConection server;

    public DataAccess(Context c) {
        context = c;
        localDB = new LocalDB(c);
        sharedPreferences = c.getSharedPreferences(c.getString(R.string.user_details), c.MODE_PRIVATE);
        spEditor = sharedPreferences.edit();
        server=new ServerConection(c);
    }

    public void addField(String name, ArrayList<LatLng> pathFrame, ArrayList<LatLng> dronePath, float distance) {
        ArrayList<String> latFrame = new ArrayList();     //split each Arraylist <LatLng to 2 Array- latitude and longtitude
        ArrayList<String> lonFrame = new ArrayList();
        ArrayList<String> latfullPath = new ArrayList();
        ArrayList<String> lonFullPath = new ArrayList();

        for (int i = 0; i < pathFrame.size(); i++) {
            latFrame.add(Double.toString(pathFrame.get(i).latitude));
            lonFrame.add(Double.toString(pathFrame.get(i).longitude));
        }
        for (int i = 0; i < dronePath.size(); i++) {
            latfullPath.add(Double.toString(dronePath.get(i).latitude));
            lonFullPath.add(Double.toString(dronePath.get(i).longitude));
        }

        String frameArrayLat = convertArrayListToString(latFrame);     //convert each arraylist to string
        String frameArrayLon = convertArrayListToString(lonFrame);
        String droePathArrayLat = convertArrayListToString(latfullPath);
        String droePathArrayLon = convertArrayListToString(lonFullPath);

        localDB.addField(name, frameArrayLat, frameArrayLon, droePathArrayLat, droePathArrayLon, distance);
//        server.addField(getUserId(), name, pathFrame,dronePath,distance );
        //TODO: addField to server
    }

    public String convertArrayListToString(ArrayList<String> arr) {
        JSONObject framePathJson = new JSONObject();
        try {
            framePathJson.put("array", new JSONArray(arr));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String stringFromArray = framePathJson.toString();//.replaceAll("\"", "'");
        return stringFromArray;
    }

    public ArrayList getNamesFields() {
        return localDB.getNamesFields();
    }
    public String getFieldNameGivenID(int id){
        return localDB.getFieldNameGivenID(id);
    }
    public int getFieldIdGivenName(String name){
        return localDB.getFieldIdGivenName(name);
    }

    public float getDistance(String fieldsName) {
        return localDB.getDistance(fieldsName);
    }

    public ArrayList<LatLng> getDronePath(String fieldsName) {
        return localDB.getDronePath(fieldsName);
    }

    public ArrayList<LatLng> getFramePath(String fieldsName) {
        return localDB.getFramePath(fieldsName);
    }

    /* delete field from local DB and from server */
    public void deleteField(String name) {
        int id=localDB.getFieldIdGivenName(name);
        if(!server.deleteField(id)){
            Toast.makeText(context, context.getString(R.string.problem_server), Toast.LENGTH_LONG).show();
        }
        else{
            localDB.deleteField(name);
        }
        //TODO: delete from server
    }

    /* delete all user's fields from local DB and from server */
    public void deleteAllFields() {
        ArrayList<Integer> fieldsId=localDB.getIdFields();
        int i;
        for(i=0; i<fieldsId.size(); i++){
            if(!server.deleteField(fieldsId.get(i))){       //If the deletion failed
                Toast.makeText(context, context.getString(R.string.problem_server), Toast.LENGTH_LONG).show();
                break;
            }
            //TODO: delete each field from server
        }
        if(i==fieldsId.size()){                         //else
            localDB.deleteAllFields();                //delete from local
        }
    }

    //return current location of the drone
    public LatLng getDroneLocation() {
        //TODO:GET THE LOCATION FROM SERVER
        return new LatLng(32.578481, 35.266195);
    }

    public boolean FieldNameHasExist(String newName) {
        return localDB.nameHasExist(newName);
    }

    public LatLng getHomePoint() {

        return new LatLng(32.574511,35.264361);
        //TODO: get drone home from shared preferences
//        Float lat= sharedPreferences.getFloat(context.getString(R.string.latitude_drone_home), 0);
//        Float lng= sharedPreferences.getFloat(context.getString(R.string.longtitude_drone_home), 0);
//        return new LatLng(lat, lng);

    }

    public void updateFieldName(String oldName, String newName) {
        localDB.updateFieldName(oldName, newName);
        //TODO: update in server- delete field and add new one

    }

    public void setPath(String whichPath, String name, ArrayList<LatLng> path) {
        ArrayList<String> lat = new ArrayList();     //split each Arraylist <LatLng to 2 Array- latitude and longtitude
        ArrayList<String> lon = new ArrayList();

        for (int i = 0; i < path.size(); i++) {
            lat.add(Double.toString(path.get(i).latitude));
            lon.add(Double.toString(path.get(i).longitude));
        }

        String frameArrayLat = convertArrayListToString(lat);     //convert each arraylist to string
        String frameArrayLon = convertArrayListToString(lon);

        if (whichPath.equals(context.getString(R.string.field_name))) {
            localDB.setFramePath(name, frameArrayLat, frameArrayLon);

        } else {                 //full path
            localDB.setDronePath(name, frameArrayLat, frameArrayLon);
            //TODO: set in server-delete field and add new one
        }
    }

    public void setFieldDistance(String name, float distance) {
        localDB.setFieldDistance(name, distance);
    }

    public void addHistory(String date, ArrayList<String> fieldsList) {
        String stringList = convertArrayListToString(fieldsList);
        localDB.addHistory(date, stringList);
        //TODO:add to server
    }

    public void deleteHistory(String date, ArrayList<String> fieldsList) {
        String stringList = convertArrayListToString(fieldsList);
        localDB.deleteHistory(date, stringList);
        //TODO:delete from server
    }

    public void deleteAllHistory() {
        localDB.deleteAllHistory();
        //TODO: delete from server
    }

    public ArrayList<String> getHistoryDates() {
        return localDB.getHistoryDates();
    }

    public ArrayList<String> getFieldsListHistory(String date) {
        return localDB.getFieldsListHistory(date);
    }

    public void addUser(String first, String last, String email, String pass, String stationId) {
        updateSP(first, last, email, pass);
        saveHomePointSP();
        //TODO: add user to server
    }

    public void editUser(String first, String last, String email, String pass) {
        updateSP(first, last, email, pass);
        //TODO: save in server- delete user and add new
    }

    public void updateSP(String first, String last, String email, String pass) {
        spEditor.putString(context.getString(R.string.f_name), first);                 // save in shared prefernce
        spEditor.putString(context.getString(R.string.l_name), last);
        spEditor.putString(context.getString(R.string.email), email);
        spEditor.putString(context.getString(R.string.password), pass);
        spEditor.apply();
    }

    public void saveHomePointSP(){
//        LatLng homePoint=getHomePointFromServer();
        spEditor.putFloat(context.getString(R.string.latitude_drone_home), 0);
        spEditor.putFloat(context.getString(R.string.longtitude_drone_home), 0);
        spEditor.apply();
    }

//    public LatLng getHomePointFromServer(){
//        //TODO: get home point from server
//        return new LatLng(32.574511,35.264361);
//    }

//    public boolean existUser(String email, String pass) {
//        //TODO: check in server
//        return true;
//    }
//
    public boolean existEmail(String email) {
        return server.ifEmailExist(email);
    }

    public String login(String email, String pass) {
        //TODO:the server will return user's details- name + last name + home point
        //updateSP with the details thet get from server
        // spEditor.putFloat(context.getString(R.string.latitude), (float) homePoint.latitude);      //save drone home in sp
//        spEditor.putFloat(context.getString(R.string.longtitude), (float) homePoint.longitude);
//        spEditor.apply();
        return "ahuva";
    }

    public String getFirstName() {
        return sharedPreferences.getString(context.getString(R.string.f_name), "");
    }

    public String getLastName() {
        return sharedPreferences.getString(context.getString(R.string.l_name), "");
    }

    public String getEmail() {
        return sharedPreferences.getString(context.getString(R.string.email), "");
    }

    public String getPassword() {
        return sharedPreferences.getString(context.getString(R.string.password), "");
    }

    public int getUserId(){
        return sharedPreferences.getInt(context.getString(R.string.user_id), -1);
    }

    public void addScanning( String date,ArrayList<String> fields, String resolution, int high){
        String str=convertArrayListToString(fields);
        localDB.addScanning(str, date, resolution,high);
        //TODO: send scanning to server
    }

    public void deleteScanning(){
        localDB.deleteScanning();
        //TODO: stop scaning in server
    }

    public ArrayList<String> getFieldsFromScanning(){
        return localDB.getFieldsFromScanning();
    }

    public String getDateFromScanning(){
        return localDB.getDateFromScanning();
    }

    public String getResolutionFromScanning(){
        return localDB.getResolutionFromScanning();
    }

    public int getHighFromScanning(){
        return localDB.getHighFromScanning();
    }

}