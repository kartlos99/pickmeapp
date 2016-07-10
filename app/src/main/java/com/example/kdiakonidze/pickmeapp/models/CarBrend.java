package com.example.kdiakonidze.pickmeapp.models;

/**
 * Created by k.diakonidze on 7/10/2016.
 */
public class CarBrend {
    int id = -1;
    String marka = "";

    public int getId() {
        return id;
    }

    public CarBrend(int id, String marka) {
        this.id = id;
        this.marka = marka;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMarka() {
        return marka;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }
}
