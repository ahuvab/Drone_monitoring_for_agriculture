package com.example.user.airscort_agriculture.Classes;

/*
This class represent point that include two float values
 */
public class PointInFloat {

    private float x;
    private float y;

    public PointInFloat(float x, float y){
        this.x=x;
        this.y=y;
    }

    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }

    /* retrun the distance in meter between two  x values */
    public float getXDistance(PointInFloat p){
        return Math.abs(this.getX()-p.getX());
    }

    /* retrun the distance in meter between two  y values */
    public float getYDistance(PointInFloat p ){
        return Math.abs(this.getY()-p.getY());
    }
}
