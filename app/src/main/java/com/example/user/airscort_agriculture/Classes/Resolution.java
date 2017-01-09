package com.example.user.airscort_agriculture.Classes;

import java.util.ArrayList;

/*
This class represent a scanning resolution. the resolution is user-selectable
 */
public class Resolution {

    private final String LOWEST="lowest";      //the options for scanning resolution
    private final String LOW="low";
    private final String MEDIUM="medium";
    private final String HIGH="high";
    private final String BEST="best";

    private int height;                                        //drone flight height depending on the resolution
    private double longtitude_space;                         //width of scanning line depending on the resolution
    private final double WIDTH_PER_METER=0.0000097042;       //the Width that drone "see" for each meter of height

    public Resolution(){}

    /* return a list of option for scanning resolution */
    public ArrayList<String> getResolutionList(){
        String[] res={"Select resolution:",LOWEST,LOW, MEDIUM,HIGH, BEST};
        ArrayList<String> arr=new ArrayList<>();
        for(int i=0; i<res.length; i++){
            arr.add(res[i]);
        }
        return arr;
    }

    /* return high for scanning givan resolution */
    public int highForResolution(String resolution){
        switch (resolution){
            case LOWEST:
                height=100;
                break;
            case LOW:
                height=75;
                break;
            case  MEDIUM:
                height=50;
                break;
            case HIGH:
                height=25;
                break;
            case BEST:
                height=8;
                break;
        }
        return height;
    }

    /* return width for scanning givan resolution */
    public double longtitudeSpace(String resolution){
        int high=highForResolution(resolution);
        longtitude_space=high*WIDTH_PER_METER;;
        return longtitude_space;
    }


    public String getLOW(){
        return LOW;
    }
    public String getLOWEST(){
        return LOWEST;
    }
    public String getMEDIUM(){
        return MEDIUM;
    }
    public String getHIGH(){
        return HIGH;
    }
    public String getBEST(){
        return BEST;
    }

}
