package com.example.user.airscort_agriculture.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/* local data-base. include 5 tables: fields, history, scan, user and time */
public class LocalDB {
    private SQLiteDatabase db;
    private DBHelper helper;

    public LocalDB(Context c) {
        helper = new DBHelper(c);
    }

    //user table
    public void addUser(String fName, String lName, String email, String password){
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FieldContracts.FieldsEntry.FIRST_NAME, fName);
        values.put(FieldContracts.FieldsEntry.LAST_NAME, lName);
        values.put(FieldContracts.FieldsEntry.EMAIL, email);
        values.put(FieldContracts.FieldsEntry.PASSWORD, password);
        db.insert(FieldContracts.FieldsEntry.USER_TABLE, null, values);
        db.close();
    }

    public void deleteUser(){
        db = helper.getWritableDatabase();
        db.delete(FieldContracts.FieldsEntry.USER_TABLE, null, null);
        db.close();
    }

    /*  update user details */
    public void setUser(String oldEmail, String fName, String lName, String email, String password){
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FieldContracts.FieldsEntry.FIRST_NAME, fName);
        values.put(FieldContracts.FieldsEntry.LAST_NAME, lName);
        values.put(FieldContracts.FieldsEntry.EMAIL, email);
        values.put(FieldContracts.FieldsEntry.PASSWORD, password);
        String where= FieldContracts.FieldsEntry.EMAIL + "=?";
        String[]whereArgs={oldEmail};
        db.update(FieldContracts.FieldsEntry.USER_TABLE, values, where, whereArgs);
        db.close();
    }

    public void setUserID(int id, String email){
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FieldContracts.FieldsEntry.USER_ID, id);
        String where= FieldContracts.FieldsEntry.EMAIL + "=?";
        String[]whereArgs={email};
        db.update(FieldContracts.FieldsEntry.USER_TABLE, values, where, whereArgs);
        db.close();
    }

    public void setHomePoint(String latHome, String lonHome, String email){
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FieldContracts.FieldsEntry.HOME_LAT, latHome);
        values.put(FieldContracts.FieldsEntry.HOME_LON, lonHome);
        String where= FieldContracts.FieldsEntry.EMAIL + "=?";
        String[]whereArgs={email};
        db.update(FieldContracts.FieldsEntry.USER_TABLE, values, where, whereArgs);
        db.close();
    }

    /* return user first name */
    public String getFName(String email){
        db=helper.getReadableDatabase();
        String[] selectArgs = {email};
        Cursor cursor=db.rawQuery("SELECT " + FieldContracts.FieldsEntry.FIRST_NAME +
                " FROM " + FieldContracts.FieldsEntry.USER_TABLE +
                " WHERE " + FieldContracts.FieldsEntry.EMAIL + "=?", selectArgs);
        int dataIndex;
        String name="";
        if(cursor.moveToNext()){
            dataIndex=cursor.getColumnIndex(FieldContracts.FieldsEntry.FIRST_NAME);
            name=cursor.getString(dataIndex);
        }
        return name;
    }

    /* return user last name */
    public String getLName(String email){
        db=helper.getReadableDatabase();
        String[] selectArgs = {email};
        Cursor cursor=db.rawQuery("SELECT " + FieldContracts.FieldsEntry.LAST_NAME +
                " FROM " + FieldContracts.FieldsEntry.USER_TABLE +
                " WHERE " + FieldContracts.FieldsEntry.EMAIL + "=?", selectArgs);
        int dataIndex;
        String name="";
        if(cursor.moveToNext()){
            dataIndex=cursor.getColumnIndex(FieldContracts.FieldsEntry.LAST_NAME);
            name=cursor.getString(dataIndex);
        }
        return name;
    }

    /* return user password */
    public String getPassword(String email){
        db=helper.getReadableDatabase();
        String[] selectArgs = {email};
        Cursor cursor=db.rawQuery("SELECT " + FieldContracts.FieldsEntry.PASSWORD +
                " FROM " + FieldContracts.FieldsEntry.USER_TABLE +
                " WHERE " + FieldContracts.FieldsEntry.EMAIL + "=?", selectArgs);
        int dataIndex;
        String name="";
        if(cursor.moveToNext()){
            dataIndex=cursor.getColumnIndex(FieldContracts.FieldsEntry.PASSWORD);
            name=cursor.getString(dataIndex);
        }
        return name;
    }

    /* return user ID */
    public int getUserId(String email){
        db=helper.getReadableDatabase();
        String[] selectArgs = {email};
        Cursor cursor=db.rawQuery("SELECT " + FieldContracts.FieldsEntry.USER_ID +
                " FROM " + FieldContracts.FieldsEntry.USER_TABLE +
                " WHERE " + FieldContracts.FieldsEntry.EMAIL + "=?", selectArgs);
        int dataIndex;
        int id=-1;
        if(cursor.moveToNext()){
            dataIndex=cursor.getColumnIndex(FieldContracts.FieldsEntry.USER_ID);
            id=cursor.getInt(dataIndex);
        }
        return id;
    }
    /* return home point */
    public LatLng getHomePoint(String email){
        double lat=Double.valueOf(getHomeLat(email));
        double lon=Double.valueOf(getHomeLon(email));
        return new LatLng(lat,lon);
    }

    public String getHomeLat(String email){
        db=helper.getReadableDatabase();
        String[] selectArgs = {email};
        Cursor cursor=db.rawQuery("SELECT " + FieldContracts.FieldsEntry.HOME_LAT +
                " FROM " + FieldContracts.FieldsEntry.USER_TABLE +
                " WHERE " + FieldContracts.FieldsEntry.EMAIL + "=?", selectArgs);
        int dataIndex;
        String lat="";
        if(cursor.moveToNext()){
            dataIndex=cursor.getColumnIndex(FieldContracts.FieldsEntry.HOME_LAT);
            lat=cursor.getString(dataIndex);
        }
        return lat;
    }

    public String getHomeLon(String email){
        db=helper.getReadableDatabase();
        String[] selectArgs = {email};
        Cursor cursor=db.rawQuery("SELECT " + FieldContracts.FieldsEntry.HOME_LON +
                " FROM " + FieldContracts.FieldsEntry.USER_TABLE +
                " WHERE " + FieldContracts.FieldsEntry.EMAIL + "=?", selectArgs);
        int dataIndex;
        String lon="";
        if(cursor.moveToNext()){
            dataIndex=cursor.getColumnIndex(FieldContracts.FieldsEntry.HOME_LON);
            lon=cursor.getString(dataIndex);
        }
        return lon;
    }

    //field table
    public void addField(String name, String latFrame, String lngFrame, String latFullPath, String lngFullPath, float distance){
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FieldContracts.FieldsEntry.NAME, name);
        values.put(FieldContracts.FieldsEntry.PATH_FRAME_LAT, latFrame);
        values.put(FieldContracts.FieldsEntry.PATH_FRAME_LON, lngFrame);
        values.put(FieldContracts.FieldsEntry.DRONE_PATH_LAT, latFullPath);
        values.put(FieldContracts.FieldsEntry.DRONE_PATH_LON, lngFullPath);
        values.put(FieldContracts.FieldsEntry.DISTANCE, distance);
        db.insert(FieldContracts.FieldsEntry.FIELDS_TABLE, null, values);
        db.close();
    }

    public ArrayList getNamesFields(){
        db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT " + FieldContracts.FieldsEntry.NAME + " FROM " +
                FieldContracts.FieldsEntry.FIELDS_TABLE, null);

        ArrayList arr=new ArrayList<>();
        int dataIndex;
        String date;
        while(cursor.moveToNext()){
            dataIndex=cursor.getColumnIndex(FieldContracts.FieldsEntry.NAME);
            date=cursor.getString(dataIndex);
            arr.add(date);
        }
        return arr;
    }
    public ArrayList<Integer> getIdFields(){
        db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT " + FieldContracts.FieldsEntry.FIELD_ID + " FROM " +
                FieldContracts.FieldsEntry.FIELDS_TABLE, null);

        ArrayList arr=new ArrayList<>();
        int dataIndex;
        int date;
        while(cursor.moveToNext()){
            dataIndex=cursor.getColumnIndex(FieldContracts.FieldsEntry.FIELD_ID);
            date=cursor.getInt(dataIndex);
            arr.add(date);
        }
        return arr;
    }

    public float getDistance(String fieldsName){
        db=helper.getReadableDatabase();
        String[] selectArgs = {fieldsName};
        Cursor cursor=db.rawQuery("SELECT " + FieldContracts.FieldsEntry.DISTANCE +
                " FROM " + FieldContracts.FieldsEntry.FIELDS_TABLE +
                " WHERE " + FieldContracts.FieldsEntry.NAME + "=?", selectArgs);
        int dataIndex;
        float name=0;
        if(cursor.moveToNext()){
            dataIndex=cursor.getColumnIndex(FieldContracts.FieldsEntry.DISTANCE);
            name=cursor.getFloat(dataIndex);
        }
        return name;
    }

    public void setFieldID(int id, String name){
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FieldContracts.FieldsEntry.FIELD_ID, id);
        String where= FieldContracts.FieldsEntry.NAME + "=?";
        String[]whereArgs={name};
        db.update(FieldContracts.FieldsEntry.FIELDS_TABLE,values,where,whereArgs);
        db.close();
    }

    public String getFieldNameGivenID(int id){
        db=helper.getReadableDatabase();
        String [] selectArgs = {String.valueOf(id)};
        Cursor cursor=db.rawQuery("SELECT " + FieldContracts.FieldsEntry.NAME +
                " FROM " + FieldContracts.FieldsEntry.FIELDS_TABLE +
                " WHERE " + FieldContracts.FieldsEntry.FIELD_ID + "=?", selectArgs);
        int dataIndex;
        String name="";
        if(cursor.moveToNext()){
            dataIndex=cursor.getColumnIndex(FieldContracts.FieldsEntry.NAME);
            name=cursor.getString(dataIndex);
        }
        return name;
    }

    public int getFieldIdGivenName(String name){
        db=helper.getReadableDatabase();
        String [] selectArgs = {name};
        Cursor cursor=db.rawQuery("SELECT " + FieldContracts.FieldsEntry.FIELD_ID +
                " FROM " + FieldContracts.FieldsEntry.FIELDS_TABLE +
                " WHERE " + FieldContracts.FieldsEntry.NAME + "=?", selectArgs);
        int dataIndex;
        int id=-1;
        if(cursor.moveToNext()){
            dataIndex=cursor.getColumnIndex(FieldContracts.FieldsEntry.FIELD_ID);
            id=cursor.getInt(dataIndex);
        }
        return id;
    }

    public void setDronePath(String name, String latFull, String lngFull ){
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FieldContracts.FieldsEntry.DRONE_PATH_LAT, latFull);
        values.put(FieldContracts.FieldsEntry.DRONE_PATH_LON, lngFull);
        String where= FieldContracts.FieldsEntry.NAME + "=?";
        String[]whereArgs={name};
        db.update(FieldContracts.FieldsEntry.FIELDS_TABLE, values, where, whereArgs);
        db.close();
    }

    public void setFramePath(String name, String latFrame, String lngFrame){
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FieldContracts.FieldsEntry.PATH_FRAME_LAT, latFrame);
        values.put(FieldContracts.FieldsEntry.PATH_FRAME_LON, lngFrame);
        String where= FieldContracts.FieldsEntry.NAME + "=?";
        String[]whereArgs={name};
        db.update(FieldContracts.FieldsEntry.FIELDS_TABLE, values, where, whereArgs);
        db.close();
    }

    public ArrayList<LatLng> getDronePath(String fieldsName){
        ArrayList<LatLng> arr=new ArrayList<>();
        String latPath=getLatFullPath(fieldsName);
        String lonPath=getLonFullPath(fieldsName);
        arr=getUndividedArray(latPath,lonPath);

        return arr;
    }

    public ArrayList<LatLng> getFramePath(String fieldsName){

        ArrayList<LatLng> arr=new ArrayList<>();
        String latPath=getLatFrame(fieldsName);
        String lonPath=getLonFrame(fieldsName);
        arr=getUndividedArray(latPath,lonPath);
        return arr;
    }

    //return one LatLng array with the frame values
    public ArrayList<LatLng> getUndividedArray(String lat, String lon){
        ArrayList<LatLng> arr=new ArrayList<>();
        try {
            JSONObject jsonLat = new JSONObject(lat);
            JSONArray jsonLatArray = jsonLat.optJSONArray("array");
            JSONObject jsonLon = new JSONObject(lon);
            JSONArray jsonLonArray = jsonLon.optJSONArray("array");
            for (int i = 0; i < jsonLatArray.length(); i++) {
                arr.add(new LatLng(Double.parseDouble((String) jsonLatArray.get(i)),Double.parseDouble((String) jsonLonArray.get(i)) ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arr;
    }

    //retrun latitude points of frame path as string from db
    public String getLatFrame(String fieldsName){
        db=helper.getReadableDatabase();
        String[] selectArgs = {fieldsName};
        Cursor cursor=db.rawQuery("SELECT " + FieldContracts.FieldsEntry.PATH_FRAME_LAT +
                " FROM " + FieldContracts.FieldsEntry.FIELDS_TABLE +
                " WHERE " + FieldContracts.FieldsEntry.NAME + "=?", selectArgs);

        int dataIndex;
        String path="";
        if(cursor.moveToNext()){
            dataIndex=cursor.getColumnIndex(FieldContracts.FieldsEntry.PATH_FRAME_LAT);
            path=cursor.getString(dataIndex);

        }
        return path;
    }

    //retrun longtitude points of frame path as string from db
    public String getLonFrame(String fieldsName){
        db=helper.getReadableDatabase();
        String[] selectArgs = {fieldsName};
        Cursor cursor=db.rawQuery("SELECT " + FieldContracts.FieldsEntry.PATH_FRAME_LON +
                " FROM " + FieldContracts.FieldsEntry.FIELDS_TABLE +
                " WHERE " + FieldContracts.FieldsEntry.NAME + "=?", selectArgs);

        int dataIndex;
        String path="";
        if(cursor.moveToNext()){
            dataIndex=cursor.getColumnIndex(FieldContracts.FieldsEntry.PATH_FRAME_LON);
            path=cursor.getString(dataIndex);

        }
        return path;
    }

    //retrun latitude points of full drone path as string from db
    public String getLatFullPath(String fieldsName){
        db=helper.getReadableDatabase();
        String[] selectArgs = {fieldsName};
        Cursor cursor=db.rawQuery("SELECT " + FieldContracts.FieldsEntry.DRONE_PATH_LAT +
                " FROM " + FieldContracts.FieldsEntry.FIELDS_TABLE +
                " WHERE " + FieldContracts.FieldsEntry.NAME + "=?", selectArgs);

        int dataIndex;
        String path="";
        if(cursor.moveToNext()){
            dataIndex=cursor.getColumnIndex(FieldContracts.FieldsEntry.DRONE_PATH_LAT);
            path=cursor.getString(dataIndex);

        }
        return path;
    }

    //retrun longtitude points of full drone path as string from db
    public String getLonFullPath(String fieldsName){
        db=helper.getReadableDatabase();
        String[] selectArgs = {fieldsName};
        Cursor cursor=db.rawQuery("SELECT " + FieldContracts.FieldsEntry.DRONE_PATH_LON +
                " FROM " + FieldContracts.FieldsEntry.FIELDS_TABLE +
                " WHERE " + FieldContracts.FieldsEntry.NAME + "=?", selectArgs);

        int dataIndex;
        String path="";
        if(cursor.moveToNext()){
            dataIndex=cursor.getColumnIndex(FieldContracts.FieldsEntry.DRONE_PATH_LON);
            path=cursor.getString(dataIndex);

        }
        return path;
    }

    public void deleteField(String name){
        db = helper.getWritableDatabase();
        String where=FieldContracts.FieldsEntry.NAME +"=?";
        String [] whereArgs={name};
        db.delete(FieldContracts.FieldsEntry.FIELDS_TABLE, where,whereArgs);
        db.close();
    }

    public void deleteAllFields(){
        db = helper.getWritableDatabase();
        db.delete(FieldContracts.FieldsEntry.FIELDS_TABLE, null,null);
        db.close();
    }

    public boolean nameHasExist(String newName){
        ArrayList<String> array=getNamesFields();
        for(int i=0; i<array.size(); i++){
            if (array.get(i).equals(newName)){
                return true;
            }
        }
        return false;
    }

    public void updateFieldName(String oldName, String newName){
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FieldContracts.FieldsEntry.NAME, newName);
        String where= FieldContracts.FieldsEntry.NAME + "=?";
        String[]whereArgs={oldName};
        db.update(FieldContracts.FieldsEntry.FIELDS_TABLE, values, where, whereArgs);
        db.close();
    }

    public void setFieldDistance(String name,float dis){
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FieldContracts.FieldsEntry.DISTANCE, dis);
        String where= FieldContracts.FieldsEntry.NAME + "=?";
        String[]whereArgs={name};
        db.update(FieldContracts.FieldsEntry.FIELDS_TABLE,values,where,whereArgs);
        db.close();
    }

    //history table
    public void addHistory(String date, String fieldsList){
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FieldContracts.FieldsEntry.DATE, date);
        values.put(FieldContracts.FieldsEntry.FIELDS_LIST, fieldsList);
        long id = db.insert(FieldContracts.FieldsEntry.HISTORY_TABLE, null, values);
        db.close();
    }

    public void setMissionId(int id, String date, String fieldsList){
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FieldContracts.FieldsEntry.MISSION_ID, id);
        String where=FieldContracts.FieldsEntry.DATE +"=? AND "+ FieldContracts.FieldsEntry.FIELDS_LIST+ "=?";
        String [] whereArgs={date, fieldsList};
        db.update(FieldContracts.FieldsEntry.HISTORY_TABLE,values,where,whereArgs);
        db.close();
    }

    public int getMissionId(String date, String fieldsList){
        db=helper.getReadableDatabase();
        String [] selectArgs = {date,fieldsList};
        Cursor cursor=db.rawQuery("SELECT " + FieldContracts.FieldsEntry.MISSION_ID +
                " FROM " + FieldContracts.FieldsEntry.HISTORY_TABLE +
                " WHERE " + FieldContracts.FieldsEntry.DATE +"=? AND "+ FieldContracts.FieldsEntry.FIELDS_LIST+ "=?",selectArgs);
        int dataIndex;
        int id=-1;
        if(cursor.moveToNext()){
            dataIndex=cursor.getColumnIndex(FieldContracts.FieldsEntry.MISSION_ID);
            id=cursor.getInt(dataIndex);
        }
        return id;
    }

    public ArrayList<Integer> getMissionsId(){
        db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT " + FieldContracts.FieldsEntry.MISSION_ID + " FROM " +
                FieldContracts.FieldsEntry.HISTORY_TABLE, null);
        ArrayList arr=new ArrayList<>();
        int dataIndex;
        int date;
        while(cursor.moveToNext()){
            dataIndex=cursor.getColumnIndex(FieldContracts.FieldsEntry.MISSION_ID);
            date=cursor.getInt(dataIndex);
            arr.add(date);
        }
        return arr;
    }

    public void deleteHistory(String date, String fields){
        db = helper.getWritableDatabase();
        String where=FieldContracts.FieldsEntry.DATE +"=? AND "+ FieldContracts.FieldsEntry.FIELDS_LIST +"=?";
        String [] whereArgs={date, fields};
        db.delete(FieldContracts.FieldsEntry.HISTORY_TABLE, where, whereArgs);
        db.close();
    }

    public void deleteHistoryById(int id){
        db = helper.getWritableDatabase();
        String where=FieldContracts.FieldsEntry.MISSION_ID +"=?";
        String [] whereArgs={String.valueOf(id)};
        db.delete(FieldContracts.FieldsEntry.HISTORY_TABLE, where, whereArgs);
        db.close();
    }

    public void deleteAllHistory(){
        db = helper.getWritableDatabase();
        db.delete(FieldContracts.FieldsEntry.HISTORY_TABLE, null,null);
        db.close();
    }

    public ArrayList<String> getHistoryDates(){
        db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT " + FieldContracts.FieldsEntry.DATE + " FROM " +
                FieldContracts.FieldsEntry.HISTORY_TABLE, null);
        ArrayList arr=new ArrayList<>();
        int dataIndex;
        String date="";
        while(cursor.moveToNext()){

            dataIndex=cursor.getColumnIndex(FieldContracts.FieldsEntry.DATE);
            date=cursor.getString(dataIndex);
            arr.add(date);
        }
        return arr;
    }

    public ArrayList<String> getFieldsListHistory(String date){
        db=helper.getReadableDatabase();
        String[] selectArgs = {date};
        Cursor cursor=db.rawQuery("SELECT " + FieldContracts.FieldsEntry.FIELDS_LIST +
                " FROM " + FieldContracts.FieldsEntry.HISTORY_TABLE +
                " WHERE " + FieldContracts.FieldsEntry.DATE + "=?" ,selectArgs);

        int dataIndex;
        String field="";
        if(cursor.moveToNext()){
            dataIndex=cursor.getColumnIndex(FieldContracts.FieldsEntry.FIELDS_LIST);
            field=cursor.getString(dataIndex);

        }
        ArrayList arr=convertStringToArrayList(field);

        return arr;
    }

    public void addScanning(String fields, String date, String resolution, int high){
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FieldContracts.FieldsEntry.SCAN_DATE, date);
        values.put(FieldContracts.FieldsEntry.FIELD, fields);
        values.put(FieldContracts.FieldsEntry.RESOLUTION, resolution);
        values.put(FieldContracts.FieldsEntry.HIGH, high);
        db.insert(FieldContracts.FieldsEntry.SCANNING_TABLE, null, values);
        db.close();
    }

    public void deleteScanning(){
        db = helper.getWritableDatabase();
        db.delete(FieldContracts.FieldsEntry.SCANNING_TABLE, null, null);
        db.close();
    }

    public ArrayList<String> getFieldsFromScanning(){
        db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT " + FieldContracts.FieldsEntry.FIELD + " FROM " +
                FieldContracts.FieldsEntry.SCANNING_TABLE, null);

        int dataIndex;
        String fields="";
        if(cursor.moveToNext()){

            dataIndex=cursor.getColumnIndex(FieldContracts.FieldsEntry.FIELD);
            fields=cursor.getString(dataIndex);
        }
        ArrayList<String> arr=convertStringToArrayList(fields);
        return arr;
    }

    public String getDateFromScanning(){
        db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT " + FieldContracts.FieldsEntry.SCAN_DATE + " FROM " +
                FieldContracts.FieldsEntry.SCANNING_TABLE, null);
        int dataIndex;
        String date="";
        if(cursor.moveToNext()){
            dataIndex=cursor.getColumnIndex(FieldContracts.FieldsEntry.SCAN_DATE);
            date=cursor.getString(dataIndex);
        }
        return date;
    }

    public String getResolutionFromScanning(){
        db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT " + FieldContracts.FieldsEntry.RESOLUTION + " FROM " +
                FieldContracts.FieldsEntry.SCANNING_TABLE, null);
        String res="";int dataIndex;

        if(cursor.moveToNext()){
            dataIndex=cursor.getColumnIndex(FieldContracts.FieldsEntry.RESOLUTION);
            res=cursor.getString(dataIndex);
        }
        return res;
    }
    public int getHighFromScanning(){
        db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT " + FieldContracts.FieldsEntry.HIGH + " FROM " +
                FieldContracts.FieldsEntry.SCANNING_TABLE, null);
        int high=0;
        if(cursor.moveToNext()){
            high=Integer.parseInt(cursor.toString());
        }
        return high;
    }

    public ArrayList<String> convertStringToArrayList(String str){
        ArrayList arr=new ArrayList<>();
        try {
            JSONObject json = new JSONObject(str);
            JSONArray jsonArray = json.optJSONArray("array");
            for (int i = 0; i < jsonArray.length(); i++) {
                arr.add(jsonArray.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arr;
    }

    //time table
    public void addScanningTime(long miliseconds){
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FieldContracts.FieldsEntry.SCANNING_TIME.toString(), miliseconds);
        db.insert(FieldContracts.FieldsEntry.TIME_TABLE, null, values);
        db.close();
    }

    public void deleteScanningTime(){
        db = helper.getWritableDatabase();
        db.delete(FieldContracts.FieldsEntry.TIME_TABLE, null, null);
        db.close();
    }

    public long getScanningTime(){
        db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT " + FieldContracts.FieldsEntry.SCANNING_TIME + " FROM " +
                FieldContracts.FieldsEntry.TIME_TABLE, null);

        int dataIndex;
        long time=0;
        if(cursor.moveToNext()){
            dataIndex=cursor.getColumnIndex(FieldContracts.FieldsEntry.SCANNING_TIME);
            time=Long.valueOf(cursor.getString(dataIndex));
        }
        return time;
    }
}
