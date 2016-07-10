package com.example.kdiakonidze.pickmeapp.datebase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;


import com.example.kdiakonidze.pickmeapp.models.CarBrend;
import com.example.kdiakonidze.pickmeapp.models.CarModel;
import com.example.kdiakonidze.pickmeapp.models.Cities;
import com.example.kdiakonidze.pickmeapp.models.DriverStatement;
import com.example.kdiakonidze.pickmeapp.models.PassangerStatement;

import java.util.ArrayList;

/**
 * Created by k.diakonidze on 7/10/2016.
 */
public class DBmanager {

    private static DBhelper dbhelper;
    private static SQLiteDatabase db;

    public static void initialaize(Context context) {
        if (dbhelper == null) {
            dbhelper = new DBhelper(context);
        }
    }

    public static void openWritable() {
        db = dbhelper.getWritableDatabase();
    }

    public static void openReadable() {
        db = dbhelper.getReadableDatabase();
    }

    public static void close() {
        db.close();
    }

    public static long insertCity(Cities city) {
        ContentValues values = new ContentValues();
        values.put(DBscheme.CITY_ID, city.getC_id());
        values.put(DBscheme.NAME, city.getNameGE());
        values.put(DBscheme.NAME_EN, city.getNameEN());
        values.put(DBscheme.CITY_PHOTO, city.getImage());
        return db.insert(DBscheme.CITY_TABLE_NAME, null, values);
    }

    public static long insertToModel(CarModel carModel) {
        ContentValues values = new ContentValues();
        values.put(DBscheme.MODEL_ID, carModel.getId());
        values.put(DBscheme.MARKA_ID, carModel.getBrendID());
        values.put(DBscheme.NAME, carModel.getModel());
        return db.insert(DBscheme.MODEL_TABLE_NAME, null, values);
    }

    public static long insertToMarka(CarBrend carBrend) {
        ContentValues values = new ContentValues();
        values.put(DBscheme.MARKA_ID, carBrend.getId());
        values.put(DBscheme.NAME, carBrend.getMarka());
        return db.insert(DBscheme.MARKA_TABLE_NAME, null, values);
    }

    public static ArrayList<Cities> getCityList() {
        ArrayList<Cities> cityList = new ArrayList<>();

        Cursor cursor = db.query(DBscheme.CITY_TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Cities newCity = new Cities(cursor.getInt(cursor.getColumnIndex(DBscheme.CITY_ID)),
                        cursor.getString(cursor.getColumnIndex(DBscheme.NAME)));
                newCity.setImage(cursor.getString(cursor.getColumnIndex(DBscheme.CITY_PHOTO)));
                newCity.setNameEN(cursor.getString(cursor.getColumnIndex(DBscheme.NAME_EN)));
                cityList.add(newCity);
            } while (cursor.moveToNext());
        }
        return cityList;
    }

    public static ArrayList<CarModel> getModelList() {
        ArrayList<CarModel> modelList = new ArrayList<>();
        Cursor cursor = db.query(DBscheme.MODEL_TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                CarModel model = new CarModel(cursor.getInt(cursor.getColumnIndex(DBscheme.MODEL_ID)),
                        cursor.getInt(cursor.getColumnIndex(DBscheme.MARKA_ID)),
                        cursor.getString(cursor.getColumnIndex(DBscheme.NAME)));
                modelList.add(model);
            } while (cursor.moveToNext());
        }
        return modelList;
    }

    public static ArrayList<CarBrend> getMarkaList() {
        ArrayList<CarBrend> markaList = new ArrayList<>();
        Cursor cursor = db.query(DBscheme.MARKA_TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                CarBrend marka = new CarBrend(cursor.getInt(cursor.getColumnIndex(DBscheme.MARKA_ID)),
                        cursor.getString(cursor.getColumnIndex(DBscheme.NAME)));
                markaList.add(marka);
            } while (cursor.moveToNext());
        }
        return markaList;
    }

    public static long updatePassangerStatement(PassangerStatement passangerStatement) {
        ContentValues values = new ContentValues();
        values.put(DBscheme.S_ID, passangerStatement.getId());
        values.put(DBscheme.USER_ID, passangerStatement.getUserID());
        values.put(DBscheme.PLACE_X, passangerStatement.getPlaceX());
        values.put(DBscheme.PLACE_Y, passangerStatement.getPlaceY());
        values.put(DBscheme.FREESPACE, passangerStatement.getFreeSpace());
        values.put(DBscheme.PRICE, passangerStatement.getPrice());
        values.put(DBscheme.CONDICIONERI, passangerStatement.getKondencioneri());
        values.put(DBscheme.SIGAR, passangerStatement.getSigareti());
        values.put(DBscheme.SABARGULI, passangerStatement.getSabarguli());
        values.put(DBscheme.CXOVELEBI, passangerStatement.getCxovelebi());
        values.put(DBscheme.ATHOME, passangerStatement.getAtHome());
        values.put(DBscheme.CITY_FROM, passangerStatement.getCityFrom());
        values.put(DBscheme.CITY_TO, passangerStatement.getCityTo());
        values.put(DBscheme.DATE, passangerStatement.getDate());
        values.put(DBscheme.TIME, passangerStatement.getTime());
        values.put(DBscheme.COMMENT, passangerStatement.getComment());
        values.put(DBscheme.NUMBER_MOBILE, passangerStatement.getNumber());
        values.put(DBscheme.NAME, passangerStatement.getName());
        values.put(DBscheme.SURMANE, passangerStatement.getSurname());

        return db.update(DBscheme.PASSANGER_TABLE_NAME, values, DBscheme.S_ID + " = " + passangerStatement.getId(), null);
    }

    public static void deletePassangerStatement(long id) {
        db.delete(DBscheme.PASSANGER_TABLE_NAME, DBscheme.S_ID + " = " + id, null);
    }

    public static long updateDriverStatement(DriverStatement driverStatement) {
        ContentValues values = new ContentValues();
        values.put(DBscheme.S_ID, driverStatement.getId());
        values.put(DBscheme.USER_ID, driverStatement.getUserID());
        values.put(DBscheme.PLACE_X, driverStatement.getPlaceX());
        values.put(DBscheme.PLACE_Y, driverStatement.getPlaceY());
        values.put(DBscheme.FREESPACE, driverStatement.getFreeSpace());
        values.put(DBscheme.PRICE, driverStatement.getPrice());
        values.put(DBscheme.CONDICIONERI, driverStatement.getKondencioneri());
        values.put(DBscheme.SIGAR, driverStatement.getSigareti());
        values.put(DBscheme.SABARGULI, driverStatement.getSabarguli());
        values.put(DBscheme.CXOVELEBI, driverStatement.getCxovelebi());
        values.put(DBscheme.ATHOME, driverStatement.getAtHome());
        values.put(DBscheme.MARKA, driverStatement.getMarka());
        values.put(DBscheme.MODELI, driverStatement.getModeli());
        values.put(DBscheme.COLOR, driverStatement.getColor());
        values.put(DBscheme.PHOTO_ST, driverStatement.getCarpicture());
        values.put(DBscheme.AGE_TO, driverStatement.getAgeTo());
        values.put(DBscheme.GENDER_ST, driverStatement.getGender());
        values.put(DBscheme.CITY_FROM, driverStatement.getCityFrom());
        values.put(DBscheme.CITY_PATH, driverStatement.getCityPath());
        values.put(DBscheme.CITY_TO, driverStatement.getCityTo());
        values.put(DBscheme.DATE, driverStatement.getDate());
        values.put(DBscheme.TIME, driverStatement.getTime());
        values.put(DBscheme.COMMENT, driverStatement.getComment());
        values.put(DBscheme.NUMBER_MOBILE, driverStatement.getNumber());
        values.put(DBscheme.NAME, driverStatement.getName());
        values.put(DBscheme.SURMANE, driverStatement.getSurname());

        return db.update(DBscheme.DRIVER_TABLE_NAME, values, DBscheme.S_ID + " = " + driverStatement.getId(), null);
    }

    public static void deleteDriverStatement(long id) {
        db.delete(DBscheme.DRIVER_TABLE_NAME, DBscheme.S_ID + " = " + id, null);
    }

    public static long insertIntoDriver(DriverStatement driverStatement, int location) {
        ContentValues values = new ContentValues();

        values.put(DBscheme.S_ID, driverStatement.getId());
        values.put(DBscheme.USER_ID, driverStatement.getUserID());
        values.put(DBscheme.PLACE_X, driverStatement.getPlaceX());
        values.put(DBscheme.PLACE_Y, driverStatement.getPlaceY());
        values.put(DBscheme.FREESPACE, driverStatement.getFreeSpace());
        values.put(DBscheme.PRICE, driverStatement.getPrice());
        values.put(DBscheme.CONDICIONERI, driverStatement.getKondencioneri());
        values.put(DBscheme.SIGAR, driverStatement.getSigareti());
        values.put(DBscheme.SABARGULI, driverStatement.getSabarguli());
        values.put(DBscheme.CXOVELEBI, driverStatement.getCxovelebi());
        values.put(DBscheme.ATHOME, driverStatement.getAtHome());
        values.put(DBscheme.MARKA, driverStatement.getMarka());
        values.put(DBscheme.MODELI, driverStatement.getModeli());
        values.put(DBscheme.COLOR, driverStatement.getColor());
        values.put(DBscheme.PHOTO_ST, driverStatement.getCarpicture());
        values.put(DBscheme.AGE_TO, driverStatement.getAgeTo());
        values.put(DBscheme.GENDER_ST, driverStatement.getGender());
        values.put(DBscheme.CITY_FROM, driverStatement.getCityFrom());
        values.put(DBscheme.CITY_PATH, driverStatement.getCityPath());
        values.put(DBscheme.CITY_TO, driverStatement.getCityTo());
        values.put(DBscheme.DATE, driverStatement.getDate());
        values.put(DBscheme.TIME, driverStatement.getTime());
        values.put(DBscheme.COMMENT, driverStatement.getComment());
        values.put(DBscheme.MANU_LOCATION, location);                            //********* favritebshia tu chem gancxadebebshi
        values.put(DBscheme.NUMBER_MOBILE, driverStatement.getNumber());
        values.put(DBscheme.NAME, driverStatement.getName());
        values.put(DBscheme.SURMANE, driverStatement.getSurname());

        return db.insert(DBscheme.DRIVER_TABLE_NAME, null, values);
    }

    public static long insertIntoPassanger(PassangerStatement passangerStatement, int location) {
        ContentValues values = new ContentValues();

        values.put(DBscheme.S_ID, passangerStatement.getId());
        values.put(DBscheme.USER_ID, passangerStatement.getUserID());
        values.put(DBscheme.PLACE_X, passangerStatement.getPlaceX());
        values.put(DBscheme.PLACE_Y, passangerStatement.getPlaceY());
        values.put(DBscheme.FREESPACE, passangerStatement.getFreeSpace());
        values.put(DBscheme.PRICE, passangerStatement.getPrice());
        values.put(DBscheme.CONDICIONERI, passangerStatement.getKondencioneri());
        values.put(DBscheme.SIGAR, passangerStatement.getSigareti());
        values.put(DBscheme.SABARGULI, passangerStatement.getSabarguli());
        values.put(DBscheme.CXOVELEBI, passangerStatement.getCxovelebi());
        values.put(DBscheme.ATHOME, passangerStatement.getAtHome());
        values.put(DBscheme.CITY_FROM, passangerStatement.getCityFrom());
        values.put(DBscheme.CITY_TO, passangerStatement.getCityTo());
        values.put(DBscheme.DATE, passangerStatement.getDate());
        values.put(DBscheme.TIME, passangerStatement.getTime());
        values.put(DBscheme.COMMENT, passangerStatement.getComment());
        values.put(DBscheme.MANU_LOCATION, location);                            //********* favritebshia tu chem gancxadebebshi
        values.put(DBscheme.NUMBER_MOBILE, passangerStatement.getNumber());
        values.put(DBscheme.NAME, passangerStatement.getName());
        values.put(DBscheme.SURMANE, passangerStatement.getSurname());

        return db.insert(DBscheme.PASSANGER_TABLE_NAME, null, values);
    }

    public static ArrayList<DriverStatement> getDriverList(int location) {
        ArrayList<DriverStatement> statementsToReturn = new ArrayList<>();
        Cursor cursor = db.query(DBscheme.DRIVER_TABLE_NAME, null, DBscheme.MANU_LOCATION + " = " + location, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String user_id = cursor.getString(cursor.getColumnIndex(DBscheme.USER_ID));
                int freeSpace = cursor.getInt(cursor.getColumnIndex(DBscheme.FREESPACE));
                int price = cursor.getInt(cursor.getColumnIndex(DBscheme.PRICE));
                String date = cursor.getString(cursor.getColumnIndex(DBscheme.DATE));
                String cityfrom = cursor.getString(cursor.getColumnIndex(DBscheme.CITY_FROM));
                String cityto = cursor.getString(cursor.getColumnIndex(DBscheme.CITY_TO));

                DriverStatement driverStatement = new DriverStatement(user_id, freeSpace, price, date, cityfrom, cityto);

                driverStatement.setId(cursor.getInt(cursor.getColumnIndex(DBscheme.S_ID)));
                driverStatement.setPlaceX(cursor.getInt(cursor.getColumnIndex(DBscheme.PLACE_X)));
                driverStatement.setPlaceY(cursor.getInt(cursor.getColumnIndex(DBscheme.PLACE_Y)));
                driverStatement.setKondencioneri(cursor.getInt(cursor.getColumnIndex(DBscheme.CONDICIONERI)));
                driverStatement.setSigareti(cursor.getInt(cursor.getColumnIndex(DBscheme.SIGAR)));
                driverStatement.setSabarguli(cursor.getInt(cursor.getColumnIndex(DBscheme.SABARGULI)));
                driverStatement.setCxovelebi(cursor.getInt(cursor.getColumnIndex(DBscheme.CXOVELEBI)));
                driverStatement.setAtHome(cursor.getInt(cursor.getColumnIndex(DBscheme.ATHOME)));
                driverStatement.setMarka(cursor.getInt(cursor.getColumnIndex(DBscheme.MARKA)));
                driverStatement.setModeli(cursor.getInt(cursor.getColumnIndex(DBscheme.MODELI)));
                driverStatement.setColor(cursor.getInt(cursor.getColumnIndex(DBscheme.COLOR)));
                driverStatement.setCarpicture(cursor.getString(cursor.getColumnIndex(DBscheme.PHOTO_ST)));
                driverStatement.setAgeTo(cursor.getInt(cursor.getColumnIndex(DBscheme.AGE_TO)));
                driverStatement.setGender(cursor.getInt(cursor.getColumnIndex(DBscheme.GENDER_ST)));
                driverStatement.setCityFrom(cursor.getString(cursor.getColumnIndex(DBscheme.CITY_FROM)));
                driverStatement.setCityPath(cursor.getString(cursor.getColumnIndex(DBscheme.CITY_PATH)));
                driverStatement.setCityTo(cursor.getString(cursor.getColumnIndex(DBscheme.CITY_TO)));
                driverStatement.setDate(cursor.getString(cursor.getColumnIndex(DBscheme.DATE)));
                driverStatement.setTime(cursor.getString(cursor.getColumnIndex(DBscheme.TIME)));
                driverStatement.setComment(cursor.getString(cursor.getColumnIndex(DBscheme.COMMENT)));

                driverStatement.setName(cursor.getString(cursor.getColumnIndex(DBscheme.NAME)));
                driverStatement.setSurname(cursor.getString(cursor.getColumnIndex(DBscheme.SURMANE)));
                driverStatement.setNumber(cursor.getString(cursor.getColumnIndex(DBscheme.NUMBER_MOBILE)));

                statementsToReturn.add(driverStatement);
            } while (cursor.moveToNext());
        }
        return statementsToReturn;
    }

    public static ArrayList<PassangerStatement> getPassangerList(int location) {
        ArrayList<PassangerStatement> statementsToReturn = new ArrayList<>();
        Cursor cursor = db.query(DBscheme.PASSANGER_TABLE_NAME, null, DBscheme.MANU_LOCATION + " = " + location, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String user_id = cursor.getString(cursor.getColumnIndex(DBscheme.USER_ID));
                int freeSpace = cursor.getInt(cursor.getColumnIndex(DBscheme.FREESPACE));
                int price = cursor.getInt(cursor.getColumnIndex(DBscheme.PRICE));
                String date = cursor.getString(cursor.getColumnIndex(DBscheme.DATE));
                String cityfrom = cursor.getString(cursor.getColumnIndex(DBscheme.CITY_FROM));
                String cityto = cursor.getString(cursor.getColumnIndex(DBscheme.CITY_TO));

                PassangerStatement passangerStatement = new PassangerStatement(user_id, freeSpace, price, cityfrom, cityto, date);

                passangerStatement.setId(cursor.getInt(cursor.getColumnIndex(DBscheme.S_ID)));
                passangerStatement.setPlaceX(cursor.getInt(cursor.getColumnIndex(DBscheme.PLACE_X)));
                passangerStatement.setPlaceY(cursor.getInt(cursor.getColumnIndex(DBscheme.PLACE_Y)));
                passangerStatement.setKondencioneri(cursor.getInt(cursor.getColumnIndex(DBscheme.CONDICIONERI)));
                passangerStatement.setSigareti(cursor.getInt(cursor.getColumnIndex(DBscheme.SIGAR)));
                passangerStatement.setSabarguli(cursor.getInt(cursor.getColumnIndex(DBscheme.SABARGULI)));
                passangerStatement.setCxovelebi(cursor.getInt(cursor.getColumnIndex(DBscheme.CXOVELEBI)));
                passangerStatement.setAtHome(cursor.getInt(cursor.getColumnIndex(DBscheme.ATHOME)));
                passangerStatement.setCityFrom(cursor.getString(cursor.getColumnIndex(DBscheme.CITY_FROM)));
                passangerStatement.setCityTo(cursor.getString(cursor.getColumnIndex(DBscheme.CITY_TO)));
                passangerStatement.setDate(cursor.getString(cursor.getColumnIndex(DBscheme.DATE)));
                passangerStatement.setTime(cursor.getString(cursor.getColumnIndex(DBscheme.TIME)));
                passangerStatement.setComment(cursor.getString(cursor.getColumnIndex(DBscheme.COMMENT)));

                passangerStatement.setName(cursor.getString(cursor.getColumnIndex(DBscheme.NAME)));
                passangerStatement.setSurname(cursor.getString(cursor.getColumnIndex(DBscheme.SURMANE)));
                passangerStatement.setNumber(cursor.getString(cursor.getColumnIndex(DBscheme.NUMBER_MOBILE)));

                statementsToReturn.add(passangerStatement);
            } while (cursor.moveToNext());
        }
        return statementsToReturn;
    }

}
