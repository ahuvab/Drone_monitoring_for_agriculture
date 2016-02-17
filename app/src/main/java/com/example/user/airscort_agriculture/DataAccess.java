package com.example.user.airscort_agriculture;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class DataAccess {

    public DataAccess(Context c){}

    public boolean addUser(String first, String last, String email, String pass){
        return true;
    }

    public boolean editUser(String first, String last, String email, String pass){
        return true;
    }

    public void changePassword(String email, String lastPass,String newPass){

    }

    public void addField(String name, List<LatLong> path){

    }

//    public void forgetPassword(){
//
//    }

    public boolean existUser(String email, String pass){
        return true;
    }

    public boolean existEmail(String email){
        return false;
    }

    public ArrayList<String> getAllFields (String email){
        ArrayList<String> array=new ArrayList<>();
        array.add("corn");
        array.add("cannabis");
        array.add("wheat 1");
        array.add("wheat 2");
        array.add("sunflower");

        return array;
    }
    public String getFirstName(){
        return "Yitzhak";
    }

    public String getLastName(){
        return "Tal";
    }

    public String getEmail(){
        return "yitzhak@airscort.me";
    }

    public String getPassword(){
        return "111111";
    }

    public int getTimeToScan(String fieldsName){
        return 125;
    }

    public ArrayList<String> getHistoryDates(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date date1=new Date(114,5,11);
        Date date2=new Date(114,9,6);
        Date date3=new Date(114,12,12);
        Date date4=new Date(115,2,22);
        Date date5=new Date(115,6,3);
        Date date6=new Date(115,11,17);
        ArrayList<String> array=new ArrayList<>();
        array.add(sdf.format(date1)+":corn, cannabis, wheat1");
        array.add(sdf.format(date2)+":wheat1, wheat2, corn");
        array.add(sdf.format(date3)+":sunflower");
        array.add(sdf.format(date4)+":cannabis, sunflower, wheat1");
        array.add(sdf.format(date5)+":corn, wheat2, wheat1, cannabis");
        array.add(sdf.format(date6)+":corn, cannabis, wheat1, wheat2, sunflower ");
        return array;

    }

    public int getNumberOfFields(){
        return 5;
    }

    public LatLng getHomePoint(){
        return new LatLng(32.574511, 35.264361);
    }

//    public ArrayList<String> getHistoryFields(){
//        ArrayList<String> array=new ArrayList<>();
//        array.add("corn, cannabis, wheat1");
//        array.add("wheat1, wheat2, corn");
//        array.add("sunflower");
//        array.add("cannabis, sunflower, wheat1");
//        array.add("corn, wheat2, wheat1, cannabis");
//        array.add("corn, cannabis, wheat1, wheat2, sunflower ");
//        return array;
//    }
}