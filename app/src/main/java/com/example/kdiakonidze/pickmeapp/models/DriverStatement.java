package com.example.kdiakonidze.pickmeapp.models;

import java.io.Serializable;

/**
 * Created by k.diakonidze on 7/10/2016.
 */
public class DriverStatement implements Serializable {
    long id, placeX, placeY;
    int freeSpace, price, kondencioneri, sigareti, sabarguli, cxovelebi, atHome, marka, modeli, color, ageFrom, ageTo, gender;

    String cityFrom, cityTo, cityPath, date, time, comment,  number, name, surname, userID, carpicture;

    public DriverStatement(String userID, int freeSpace, int price, String date, String cityFrom, String cityTo) {
        this.userID = userID;
        this.freeSpace = freeSpace;
        this.price = price;
        this.date = date;
        this.cityFrom = cityFrom;
        this.cityTo = cityTo;

        id=0; placeX=0; placeY=0;
        kondencioneri=2;
        sigareti=2;
        sabarguli=2;
        cxovelebi=2;
        atHome=2;
        marka=0; modeli=0; color=0;
        ageFrom=0; ageTo=100; gender=2;
        cityPath="";
        time="";
        comment="";
        number=""; name=""; surname="";
    }

    @Override
    public String toString() {
        return "DriverStatement{" +
                "id=" + id +
                ", userID=" + userID +
                ", placeX=" + placeX +
                ", placeY=" + placeY +
                ", freeSpace=" + freeSpace +
                ", price=" + price +
                ", kondencioneri=" + kondencioneri +
                ", sigareti=" + sigareti +
                ", sabarguli=" + sabarguli +
                ", cxovelebi=" + cxovelebi +
                ", atHome=" + atHome +
                ", marka=" + marka +
                ", modeli=" + modeli +
                ", color=" + color +
                ", ageFrom=" + ageFrom +
                ", ageTo=" + ageTo +
                ", gender=" + gender +
                ", cityFrom='" + cityFrom + '\'' +
                ", cityTo='" + cityTo + '\'' +
                ", cityPath='" + cityPath + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", comment='" + comment + '\'' +
                ", number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", carpicture='" + carpicture + '\'' +
                '}';
    }

    public String getCarpicture() {
        return carpicture;
    }

    public void setCarpicture(String carpicture) {
        this.carpicture = carpicture;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public long getPlaceX() {
        return placeX;
    }

    public void setPlaceX(long placeX) {
        this.placeX = placeX;
    }

    public long getPlaceY() {
        return placeY;
    }

    public void setPlaceY(long placeY) {
        this.placeY = placeY;
    }

    public int getFreeSpace() {
        return freeSpace;
    }

    public void setFreeSpace(int freeSpace) {
        this.freeSpace = freeSpace;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getKondencioneri() {
        return kondencioneri;
    }

    public void setKondencioneri(int kondencioneri) {
        this.kondencioneri = kondencioneri;
    }

    public int getSigareti() {
        return sigareti;
    }

    public void setSigareti(int sigareti) {
        this.sigareti = sigareti;
    }

    public int getSabarguli() {
        return sabarguli;
    }

    public void setSabarguli(int sabarguli) {
        this.sabarguli = sabarguli;
    }

    public int getCxovelebi() {
        return cxovelebi;
    }

    public void setCxovelebi(int cxovelebi) {
        this.cxovelebi = cxovelebi;
    }

    public int getAtHome() {
        return atHome;
    }

    public void setAtHome(int atHome) {
        this.atHome = atHome;
    }

    public int getMarka() {
        return marka;
    }

    public void setMarka(int marka) {
        this.marka = marka;
    }

    public int getModeli() {
        return modeli;
    }

    public void setModeli(int modeli) {
        this.modeli = modeli;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getAgeFrom() {
        return ageFrom;
    }

    public void setAgeFrom(int ageFrom) {
        this.ageFrom = ageFrom;
    }

    public int getAgeTo() {
        return ageTo;
    }

    public void setAgeTo(int ageTo) {
        this.ageTo = ageTo;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCityFrom() {
        return cityFrom;
    }

    public void setCityFrom(String cityFrom) {
        this.cityFrom = cityFrom;
    }

    public String getCityTo() {
        return cityTo;
    }

    public void setCityTo(String cityTo) {
        this.cityTo = cityTo;
    }

    public String getCityPath() {
        return cityPath;
    }

    public void setCityPath(String cityPath) {
        this.cityPath = cityPath;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}