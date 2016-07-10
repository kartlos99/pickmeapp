package com.example.kdiakonidze.pickmeapp.models;

/**
 * Created by k.diakonidze on 7/10/2016.
 */
public class Cities {
    int c_id;
    String nameGE, nameEN, image;

    public Cities(int c_id, String nameGE) {
        this.c_id = c_id;
        this.nameGE = nameGE;
        nameEN="";
        image="";
    }

    @Override
    public String toString() {
        return "Cities{" +
                "c_id=" + c_id +
                ", nameGE='" + nameGE + '\'' +
                ", nameEN='" + nameEN + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    public int getC_id() {
        return c_id;
    }

    public void setC_id(int c_id) {
        this.c_id = c_id;
    }

    public String getNameGE() {
        return nameGE;
    }

    public void setNameGE(String nameGE) {
        this.nameGE = nameGE;
    }

    public String getNameEN() {
        return nameEN;
    }

    public void setNameEN(String nameEN) {
        this.nameEN = nameEN;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
