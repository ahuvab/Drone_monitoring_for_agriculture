package com.example.user.airscort_agriculture;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;

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
}