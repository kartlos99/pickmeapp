package com.example.kdiakonidze.pickmeapp.utils;

import com.example.kdiakonidze.pickmeapp.models.CarBrend;
import com.example.kdiakonidze.pickmeapp.models.CarModel;
import com.example.kdiakonidze.pickmeapp.models.Cities;
import com.facebook.AccessToken;
import com.facebook.Profile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by k.diakonidze on 7/10/2016.
 */
public class Constantebi {
    public static String MY_ID ;
    public static String MY_NAME = "";
    public static String MY_MOBILE = "";

    public final static String MY_OWN_STAT = "chemi";
    public final static String ALL_STAT = "yvela";
    public final static String FILTERED_STAT = "gafiltruli";
    public final static String FAVORIT_STAT = "rcheuli";
    public final static String STAT_TYPE_DRIVER = "driver";
    public final static String STAT_TYPE_PASSANGER = "passanger";
    public final static String REASON_ADD = "add";
    public final static String REASON_EDIT = "edit";
    public final static int MY_STATEMENT = 1;
    public final static int FAV_STATEMENT = 2;
    public final static int MAN_WIDTH = 32;
    public final static int MAN_HIEGHT = 52;

    public static ArrayList<CarBrend> brendList = new ArrayList<>();
    public static ArrayList<CarModel> modelList = new ArrayList<>();
    public static ArrayList<Cities> cityList = new ArrayList<>();

    public static List<Long> FAV_STAT_DRIVER = new ArrayList<>();
    public static List<Long> FAV_STAT_PASSANGER = new ArrayList<>();

    public static AccessToken accessToken;
    public static Profile profile;

    public static Boolean IS_FILTERED = false;
    public static Boolean FILTER_DR = false;
    public static Boolean FILTER_PS = false;
    public static boolean dataServerParse = false;   // true - parse; false - some server;
    public static boolean ONLYONES = true;   // ertxel daxatos fb_UI navigation menushi

    public       static String FILTER_CRITERIUM = "";
    public final static String URL_GET_FILTER = "http://geolab.club/geolabwork/kartlos/get_filter.php?";
    public final static String URL_GET_SITIS = "http://geolab.club/geolabwork/kartlos/get_citis.php";
    public final static String URL_ADD_USER = "http://geolab.club/geolabwork/kartlos/add_user.php";
    public final static String URL_INSERT_ST1 = "http://geolab.club/geolabwork/kartlos/insert_st_1.php";
    public final static String URL_INSERT_ST2 = "http://geolab.club/geolabwork/kartlos/insert_st_2.php";
    public final static String URL_UPDATE_ST1 = "http://geolab.club/geolabwork/kartlos/update_st_1.php";
    public final static String URL_UPDATE_ST2 = "http://geolab.club/geolabwork/kartlos/update_st_2.php";
    public final static String URL_GET_ST_LIST = "http://geolab.club/geolabwork/kartlos/get_st_list.php?";  //  http://geolab.club/geolabwork/kartlos/get_st1_list.php?start_row=0&page_size=10
}
