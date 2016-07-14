package com.example.kdiakonidze.pickmeapp;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.DrawableRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.facebook.AccessToken;
import com.example.kdiakonidze.pickmeapp.adapters.StatementListPagesAdapter;
import com.example.kdiakonidze.pickmeapp.datebase.DBhelper;
import com.example.kdiakonidze.pickmeapp.datebase.DBmanager;
import com.example.kdiakonidze.pickmeapp.datebase.DBscheme;
import com.example.kdiakonidze.pickmeapp.dialogs.FilterDialog;
import com.example.kdiakonidze.pickmeapp.dialogs.PrivateInfo;
import com.example.kdiakonidze.pickmeapp.fragments.DriverStatatementListFragment;
import com.example.kdiakonidze.pickmeapp.models.Cities;
import com.example.kdiakonidze.pickmeapp.models.DriverStatement;
import com.example.kdiakonidze.pickmeapp.models.PassangerStatement;
import com.example.kdiakonidze.pickmeapp.utils.Constantebi;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements FilterDialog.OnFilterDone {

    private MenuItem menuItemFilter;
    private int movida = 0;
    FragmentManager manager;
    private ProgressDialog progress;
    public final Context mainctx = this;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    CallbackManager callbackManager;

    private ViewPager pager = null;
    private StatementListPagesAdapter myadapter;
    private TabLayout tabs;


    //    **********************************************************************************************
    void printHK() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "diakonidze.kartlos.voiage",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Toast.makeText(this, "ResumE !", Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "PauZa !", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        printHK();

        final CoordinatorLayout coordLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        manager = getSupportFragmentManager();

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("PeckMeApp");


        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddStatement.class);
                startActivity(intent);
            }
        });

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                drawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {
                    case R.id.manuFilter:

                        filterIssues();

                        Toast.makeText(getApplicationContext(), "filtri", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.manuAdd:
                        Intent intent = new Intent(getApplicationContext(), AddStatement.class);
                        startActivity(intent);
                        return true;

                    case R.id.manuMy:
                        Intent toMyPage = new Intent(getApplicationContext(), MyStatements.class);
                        startActivity(toMyPage);
                        return true;

                    case R.id.manuFav:
                        Intent toMyfav = new Intent(getApplicationContext(), FavoriteStatements.class);
                        startActivity(toMyfav);
                        return true;

                    case R.id.manuFBlogin:

                        if (Constantebi.accessToken == null) {
//                          aq vlogindebit FB-it
                            List<String> permissions = Arrays.asList("public_profile", "email", "user_friends");
                            LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, permissions);
                        } else {
//                          aq gamovdivart FB-it
                            LoginManager.getInstance().logOut();
                            Constantebi.accessToken = null;
                            clearUIatLogout();
                        }

                        return true;

                    case R.id.manuExit:
                        break;

                    case R.id.manuPrivit:

                        PrivateInfo infoDialog = new PrivateInfo();
                        infoDialog.show(manager, "info");

                        return true;
                }

                return false;
            }
        });



        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
//                Toast.makeText(getApplicationContext(),"daiketa",Toast.LENGTH_SHORT).show();
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);


            }

            @Override
            public void onDrawerOpened(View drawerView) {
//                Toast.makeText(getApplicationContext(),"gaigo",Toast.LENGTH_SHORT).show();
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);

                if (Constantebi.ONLYONES && Constantebi.profile != null) {
                    Toast.makeText(getApplicationContext(), "haaa", Toast.LENGTH_SHORT).show();
                    fillUIatLogin();
                    Constantebi.ONLYONES = false;
                }
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        pager = (ViewPager) findViewById(R.id.pageView);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        if (Constantebi.IS_FILTERED){
            myadapter = new StatementListPagesAdapter(fragmentManager, Constantebi.FILTERED_STAT);
        }else {
            myadapter = new StatementListPagesAdapter(fragmentManager, Constantebi.ALL_STAT);
        }
        pager.setAdapter(myadapter);

        tabs = (TabLayout) findViewById(R.id.tabs_mainpage);
        tabs.setupWithViewPager(pager);

//        tabs.setTabGravity(TabLayout.GRAVITY_FILL);
//        tabs.setTabTextColors(Color.WHITE, getResources().getColor(R.color.fab_color));

        LoadCities();

        facebookissues();
    }

    private void filterIssues(){        // *************** filtris *********************

        if (Constantebi.IS_FILTERED) {
            navigationView.getMenu().getItem(0).setTitle(getString(R.string.manu_filter_cancel));
            navigationView.getMenu().getItem(0).setIcon(R.drawable.filter_remove_white);
            Constantebi.IS_FILTERED = false;
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }else{
            navigationView.getMenu().getItem(0).setTitle(getString(R.string.manu_filter));
            navigationView.getMenu().getItem(0).setIcon(R.drawable.filter_white);
            FilterDialog filterDialog = new FilterDialog();
            filterDialog.show(manager, "filter");
        }

    }

    private void facebookissues() {

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Constantebi.profile = Profile.getCurrentProfile();
                Constantebi.accessToken = AccessToken.getCurrentAccessToken();

                if (Constantebi.profile != null) {
                    Constantebi.MY_NAME = Constantebi.profile.getName();
                    Constantebi.MY_ID = Constantebi.profile.getId();
                    fillUIatLogin();

                    Toast.makeText(getApplicationContext(), "prof info geted!", Toast.LENGTH_LONG).show();

                    String url = Constantebi.URL_ADD_USER;

                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("fb_id", Constantebi.MY_ID);
                            params.put("name", Constantebi.MY_NAME);
                            params.put("comment", "");
                            params.toString();
                            return params;
                        }
                    };
                    queue.add(stringRequest);


                } else {
                    Toast.makeText(getApplicationContext(), "no prof!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        Toast.makeText(this, "NOT logged", Toast.LENGTH_SHORT).show();
        if (Profile.getCurrentProfile() != null) {
            Toast.makeText(this, "logged in", Toast.LENGTH_SHORT).show();
            Constantebi.accessToken = AccessToken.getCurrentAccessToken();
            Constantebi.profile = Profile.getCurrentProfile();

            Constantebi.MY_NAME = Constantebi.profile.getName();
            Constantebi.MY_ID = Constantebi.profile.getId();
            fillUIatLogin();
        }
    }

    private void getFavoriteStatements() {
        DBmanager.initialaize(this);
        DBmanager.openReadable();
        ArrayList<DriverStatement> driverStatements = DBmanager.getDriverList(Constantebi.FAV_STATEMENT);
        ArrayList<PassangerStatement> passangerStatements = DBmanager.getPassangerList(Constantebi.FAV_STATEMENT);
        DBmanager.close();

        for (int i = 0; i < driverStatements.size(); i++) {
            Constantebi.FAV_STAT_DRIVER.add(driverStatements.get(i).getId());
        }
        for (int i = 0; i < passangerStatements.size(); i++) {
            Constantebi.FAV_STAT_PASSANGER.add(passangerStatements.get(i).getId());
        }
    }

    private void LoadCities() {

        DBmanager.initialaize(this);
        DBmanager.openReadable();
        Constantebi.cityList = DBmanager.getCityList();
        DBmanager.close();

        // aq sxva kriteriumia chasasmeli, es droebitia
        if (Constantebi.cityList.size() == 0) {

            progress = ProgressDialog.show(this, "ჩამოტვირთვა", "გთხოვთ დაიცადოთ");


            RequestQueue queue = Volley.newRequestQueue(this);

            String url = Constantebi.URL_GET_SITIS;
            JsonArrayRequest requestCities = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray jsonArray) {

                            if (jsonArray.length() > 0) {
                                Constantebi.cityList.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        Cities newCity = new Cities(jsonArray.getJSONObject(i).getInt("id"), jsonArray.getJSONObject(i).getString("nameGE"));

                                        // newCity.setNameEN(jsonArray.getJSONObject(i).getString("nameEN"));
                                        newCity.setImage(jsonArray.getJSONObject(i).getString("image"));
                                        Constantebi.cityList.add(newCity);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            movida++;
                            writeToDB();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            progress.dismiss();
                            Toast.makeText(MainActivity.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
            );

            queue.add(requestCities);


//            queue.add(requestMarka);
//            queue.add(requestModel);

            Toast.makeText(this, "INtidan chamotvirtva", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        menuItemFilter = (MenuItem) menu.findItem(R.id.manu_filter);
        if (Constantebi.IS_FILTERED){
            menuItemFilter.setIcon(R.drawable.filter_remove_white);
        }else{
            menuItemFilter.setIcon(R.drawable.filter_white);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.add_statement) {
            Intent intent = new Intent(getApplicationContext(), AddStatement.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.manu_filter) {

            filterIssues();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void writeToDB() {
        if (movida == 1) {

            progress.dismiss();

            AsyncWrite writedb = new AsyncWrite(new DBhelper(this));
            writedb.execute();

//            DBmanager.initialaize(getApplication());
//            DBmanager.openWritable();
//            for (int i = 0; i < Constantebi.brendList.size(); i++) {
//                DBmanager.insertToMarka(Constantebi.brendList.get(i));
//            }
//            for (int i = 0; i < Constantebi.modelList.size(); i++) {
//                DBmanager.insertToModel(Constantebi.modelList.get(i));
//            }
//            for (int i = 0; i < Constantebi.cityList.size(); i++) {
//                DBmanager.insertCity(Constantebi.cityList.get(i));
//            }
//            DBmanager.close();

        }
    }

    private void fillUIatLogin() {
        TextView my_fb_name = (TextView) findViewById(R.id.username);
        de.hdodenhof.circleimageview.CircleImageView profImage = (CircleImageView) findViewById(R.id.profile_image);

        if (my_fb_name != null) {
            if (Constantebi.MY_NAME != null) {
                my_fb_name.setText(Constantebi.MY_NAME);
            }

            if (Constantebi.profile != null) {
                Picasso.with(this)
                        .load("https://graph.facebook.com/" + Constantebi.profile.getId() + "/picture?type=large")
                        .into(profImage);

                navigationView.getMenu().getItem(5).setTitle(getString(R.string.manu_FBlogout));
            }
        }
    }

    private void clearUIatLogout() {
        TextView myname = (TextView) findViewById(R.id.username);
        myname.setText("");

        de.hdodenhof.circleimageview.CircleImageView profImage = (CircleImageView) findViewById(R.id.profile_image);
        profImage.setImageResource(R.drawable.ic_account_circle_white_48dp);

        navigationView.getMenu().getItem(5).setTitle(getString(R.string.manu_FBlogin));

        Constantebi.accessToken = null;
        Constantebi.profile = null;
    }

    @Override
    public void sendCreteria(String criteria) {
//        DriverStatatementListFragment currDriverStatements = (DriverStatatementListFragment) getSupportFragmentManager().findFragmentByTag("driver_fragment");
        DriverStatatementListFragment currDriverStatements = (DriverStatatementListFragment) myadapter.getItem(0);

        if(currDriverStatements != null) {
            currDriverStatements.reNewData(criteria);
        }else {
            Toast.makeText(this, "no fragment", Toast.LENGTH_LONG).show();
        }
    }

    private class AsyncWrite extends AsyncTask<DBhelper, String, String> {
        private DBhelper dbhelper;
        private SQLiteDatabase db;

        public AsyncWrite(DBhelper dbhelper) {
            this.dbhelper = dbhelper;
        }

        @Override
        protected String doInBackground(DBhelper... params) {

            db = dbhelper.getWritableDatabase();

            for (int i = 0; i < Constantebi.cityList.size(); i++) {
                ContentValues values = new ContentValues();
                values.put(DBscheme.CITY_ID, Constantebi.cityList.get(i).getC_id());
                values.put(DBscheme.NAME, Constantebi.cityList.get(i).getNameGE());
                values.put(DBscheme.NAME_EN, Constantebi.cityList.get(i).getNameEN());
                values.put(DBscheme.CITY_PHOTO, Constantebi.cityList.get(i).getImage());
                db.insert(DBscheme.CITY_TABLE_NAME, null, values);
            }

//            for (int i = 0; i < Constantebi.modelList.size(); i++) {
//                ContentValues values = new ContentValues();
//                values.put(DBscheme.MODEL_ID, Constantebi.modelList.get(i).getId());
//                values.put(DBscheme.MARKA_ID, Constantebi.modelList.get(i).getBrendID());
//                values.put(DBscheme.NAME, Constantebi.modelList.get(i).getModel());
//                db.insert(DBscheme.MODEL_TABLE_NAME, null, values);
//            }
//
//            for (int i = 0; i < Constantebi.brendList.size(); i++) {
//                ContentValues values = new ContentValues();
//                values.put(DBscheme.MARKA_ID, Constantebi.brendList.get(i).getId());
//                values.put(DBscheme.NAME, Constantebi.brendList.get(i).getMarka());
//                db.insert(DBscheme.MARKA_TABLE_NAME, null, values);
//            }

            db.close();

            return "saved in DB";
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
        }
    }
}
