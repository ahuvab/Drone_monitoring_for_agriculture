package com.example.user.airscort_agriculture;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

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
        array.add("sunflower");
        array.add("wheat 1");
        array.add("wheat 2");
        array.add("watermelon");

        return array;
    }
    public String getFirstName(String email){
        return "Yitzhak";
    }

}