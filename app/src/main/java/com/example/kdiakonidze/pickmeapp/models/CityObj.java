package com.example.kdiakonidze.pickmeapp.models;

/**
 * Created by k.diakonidze on 7/10/2016.
 */
public class CityObj {
    public String name, prevCity;
    public int distance, time;

    public CityObj(String name, String prevCity, int distance, int time) {
        this.name = name;
        this.prevCity = prevCity;
        this.distance = distance;
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrevCity() {
        return prevCity;
    }

    public void setPrevCity(String prevCity) {
        this.prevCity = prevCity;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
