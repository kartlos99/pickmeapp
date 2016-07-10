package com.example.kdiakonidze.pickmeapp.models;

/**
 * Created by k.diakonidze on 7/10/2016.
 */
public class CityConnection {
    public String cityA, cityB;
    public int time, distance;

    public CityConnection(String cityA, String cityB, int time, int distance) {
        this.cityA = cityA;
        this.cityB = cityB;
        this.time = time;
        this.distance = distance;
    }

    public String getCityA() {
        return cityA;
    }

    public void setCityA(String cityA) {
        this.cityA = cityA;
    }

    public String getCityB() {
        return cityB;
    }

    public void setCityB(String cityB) {
        this.cityB = cityB;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
