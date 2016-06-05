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
//        TODO: server.addField(getUserId(),name, distance ,pathFrame,dronePath );
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
        localDB.deleteField(name);

        //TODO:
//        int id=localDB.getFieldIdGivenName(name);
//        if(!server.deleteField(id)){
//            Toast.makeText(context, context.getString(R.string.problem_server), Toast.LENGTH_LONG).show();
//        }
//        else{
//            localDB.deleteField(id);
//        }

    }

    /* delete all user's fields from local DB and from server */
    public void deleteAllFields() {
        ArrayList<Integer> fieldsId=localDB.getIdFields();
        //TODO:
//        int i;
//        for(i=0; i<fieldsId.size(); i++){
//            if(!server.deleteField(fieldsId.get(i))){       //If the deletion failed
//                Toast.makeText(context, context.getString(R.string.problem_server), Toast.LENGTH_LONG).show();
//                break;
//            }
//        }
//        if(i==fieldsId.size()){                         //else
            localDB.deleteAllFields();                //delete from local
//        }
    }

    //return current location of the drone
    public LatLng getDroneLocation() {
//       TODO:  server.getDronrLocation(getUserId());
        return new LatLng(32.578481, 35.266195);
    }

    public boolean FieldNameHasExist(String newName) {
        return localDB.nameHasExist(newName);
    }

    public LatLng getHomePoint() {
        return new LatLng(32.574511,35.264361);
        //TODO: localDB.getHomePoint(getEmail());
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
    }

    public void deleteHistory(String date, ArrayList<String> fieldsList) {
        String stringList = convertArrayListToString(fieldsList);
        int id=localDB.getMissionId(date,fieldsList.toString());
        localDB.deleteHistory(date, convertArrayListToString(fieldsList));
//        localDB.deleteHistory(id);
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
        localDB.addUser(first,last,email,pass);
        updateSP(email);
        //TODO: add user to server and save user_id  & homepoint in local DB- setUserID + setHomePoint
    }

    public void deleteUser(){
        localDB.deleteUser();
    }

    public void editUser(String first, String last, String email, String pass) {
        localDB.setUser(getEmail(), first,last,email,pass);
        updateSP(email);
        //TODO: save in server- delete user and add new
    }

    //save user email in shared preferences
    public void updateSP(String email) {
        spEditor.putString(context.getString(R.string.email), email);
        spEditor.apply();
    }


    public boolean existEmail(String email) {
        return false;
        //TODO: return server.ifEmailExist(email);
    }

    public boolean login(String email, String pass) {
        //delete all local DB and get new details from server
        //TODO: localDB.deleteUser();
        //TODO: localDB.deleteAllHistory();
        //TODO:localDB.deleteScanning();
        //TODO: localDB.deleteAllFields();
        //TODO: return server.login(email,pass);

        if(email.equals(getEmail())&& pass.equals(localDB.getPassword(email))){     //temp
            return true;
        }
        return false;
    }

    public String getFirstName() {
        return localDB.getFName(getEmail());
    }

    public String getLastName() {
        return localDB.getLName(getEmail());
    }

    public String getEmail() {
        return sharedPreferences.getString(context.getString(R.string.email), "");
    }

    public String getPassword() {
        return localDB.getPassword(getEmail());
    }

    public int getUserId(){
        return localDB.getUserId(getEmail());
    }


    public void addScanning( String date,ArrayList<String> fields, String resolution, int high){
        String str=convertArrayListToString(fields);
        localDB.addScanning(str, date, resolution, high);
        addHistory(date, fields);

        ArrayList<Integer> fieldsId=new ArrayList<>();    //convert fields name list to fields ID LIST
        for(int i=0; i<fields.size(); i++){
            fieldsId.add(localDB.getFieldIdGivenName(fields.get(i)));
        }
        // TODO: server.startScanning(getUserId(),fieldsId);
    }

    public void deleteScanning(){
        int id=localDB.getMissionId(localDB.getDateFromScanning(), localDB.getFieldsFromScanning().toString());
        localDB.deleteScanning();
//        localDB.deleteHistory(id);
//        TODO: server.stopScanning(id);
    }

    public void finishScanning(){
        int id=localDB.getMissionId(localDB.getDateFromScanning(),localDB.getFieldsFromScanning().toString());
        localDB.deleteScanning();
        //TODO: STOP SCANNING IN SERVER
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