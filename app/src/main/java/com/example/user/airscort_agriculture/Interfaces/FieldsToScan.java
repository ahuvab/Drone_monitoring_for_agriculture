package com.example.user.airscort_agriculture.Interfaces;

import java.util.ArrayList;


public interface FieldsToScan {
    public void addField(String nameOfField);           //add field to scanning fields list

    public void deleteField (String nameOfField);       //remove field from scanning fields list

    public void clear();                                //clear the scanning fields list

    public ArrayList getFieldsToScan();                 //get scanning fields list

    public boolean ifContainField(String name);         //return if the field aleredy exist in list
}
