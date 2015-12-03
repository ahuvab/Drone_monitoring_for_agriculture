package com.example.user.airscort_agriculture;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

    public void changePassword(String email, String newPass){

    }

    public void forgetPassword(){

    }

    public boolean existUser(String email, String pass){
        return true;
    }

    public boolean existEmail(String email){
        return false;
    }


}