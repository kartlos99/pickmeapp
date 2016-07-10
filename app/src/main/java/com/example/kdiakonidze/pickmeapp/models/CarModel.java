package com.example.kdiakonidze.pickmeapp.models;

/**
 * Created by k.diakonidze on 7/10/2016.
 */
public class CarModel {
    int id = -1, brendID = -1;
    String model = "";

    public CarModel(int id, int brendID, String model) {
        this.id = id;
        this.brendID = brendID;
        this.model = model;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBrendID() {
        return brendID;
    }

    public void setBrendID(int brendID) {
        this.brendID = brendID;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
