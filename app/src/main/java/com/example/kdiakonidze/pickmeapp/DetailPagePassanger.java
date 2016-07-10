package com.example.kdiakonidze.pickmeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kdiakonidze.pickmeapp.datebase.DBmanager;
import com.example.kdiakonidze.pickmeapp.models.PassangerStatement;
import com.example.kdiakonidze.pickmeapp.utils.Constantebi;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailPagePassanger extends AppCompatActivity {

    MenuItem menuItemedit;
    MenuItem menuItemdel;
    MenuItem menuItemfav;

    private CollapsingToolbarLayout collapsingToolbar;
    private PassangerStatement passangerStatement;
    private String whereFrom = "";
    private Toolbar toolbar;

    private boolean favoriteState = false;
    int mutedColor = R.attr.colorPrimary;

    @Override
    protected void onPause() {
        super.onPause();

        if (favoriteState) {
            if (!Constantebi.FAV_STAT_PASSANGER.contains(passangerStatement.getId())) {
                DBmanager.initialaize(this);
                DBmanager.openWritable();
                DBmanager.insertIntoPassanger(passangerStatement, Constantebi.FAV_STATEMENT);
                DBmanager.close();
                Constantebi.FAV_STAT_PASSANGER.add(passangerStatement.getId());
            }
        } else {
            if (Constantebi.FAV_STAT_PASSANGER.contains(passangerStatement.getId())) {
                DBmanager.initialaize(this);
                DBmanager.openWritable();
                DBmanager.deletePassangerStatement(passangerStatement.getId());
                DBmanager.close();
                Constantebi.FAV_STAT_PASSANGER.remove((passangerStatement.getId()));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Constantebi.FAV_STAT_PASSANGER.contains(passangerStatement.getId())) {
            favoriteState = true;
        } else {
            favoriteState = false;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout_passanger);

        whereFrom = getIntent().getStringExtra("from");
        passangerStatement = (PassangerStatement) getIntent().getSerializableExtra("driver_st");

        ImageView header = (ImageView) findViewById(R.id.header);

        ImageView callerImage = (ImageView) findViewById(R.id.caller);
        callerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + passangerStatement.getNumber()));
                startActivity(callIntent);
            }
        });

        final Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(passangerStatement.getName() + " " + passangerStatement.getSurname());
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.ColorPrimary));
        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);

        TextView cityT = (TextView) findViewById(R.id.detiles_city_text);
        TextView timeT = (TextView) findViewById(R.id.detiles_time_text);
        TextView freespaceT = (TextView) findViewById(R.id.detiles_freespace_text);
        TextView priceT = (TextView) findViewById(R.id.detiles_price_text);
        TextView commentT = (TextView) findViewById(R.id.detiles_comment_text);
        ImageView headerCityImage = (ImageView) findViewById(R.id.header);

        int i = 0;
        while (i < Constantebi.cityList.size()) {
            if (passangerStatement.getCityTo().equals(String.valueOf(Constantebi.cityList.get(i).getC_id()))) {
                if (!Constantebi.cityList.get(i).getImage().equals(""))
                    Picasso.with(this)
                            .load("http://geolab.club/geolabwork/kartlos/picture/"+Constantebi.cityList.get(i).getImage() + ".jpg" )
                            .resize(700, 500)
                            .centerCrop()
                            .into(headerCityImage);
                i=1000;
            }
            i++;
        }

        cityT.setText(cityAtID(passangerStatement.getCityFrom()) + " - " + cityAtID(passangerStatement.getCityTo()));
        timeT.setText(passangerStatement.getDate() + " " + passangerStatement.getTime());
        freespaceT.setText(String.valueOf(passangerStatement.getFreeSpace()));
        priceT.setText(String.valueOf(passangerStatement.getPrice()));
        commentT.setText(passangerStatement.getComment());

        LinearLayout pirobebi_detail = (LinearLayout) findViewById(R.id.pirobebi_detail);
        CheckBox kontCB = (CheckBox) findViewById(R.id.driver_conditioner_checkBox);
        CheckBox athomeCB = (CheckBox) findViewById(R.id.driver_atplace_checkBox);
        CheckBox sigarCB = (CheckBox) findViewById(R.id.driver_cigar_checkBox);
        CheckBox bagCB = (CheckBox) findViewById(R.id.driver_baggage_checkBox);
        CheckBox animalCB = (CheckBox) findViewById(R.id.driver_animal_checkBox);

        if (passangerStatement.getKondencioneri() != 2) {
            pirobebi_detail.setVisibility(View.VISIBLE);
            if (passangerStatement.getKondencioneri() == 1) kontCB.setChecked(true);
            else kontCB.setChecked(false);
            if (passangerStatement.getSigareti() == 1) sigarCB.setChecked(true);
            else sigarCB.setChecked(false);
            if (passangerStatement.getAtHome() == 1) athomeCB.setChecked(true);
            else athomeCB.setChecked(false);
            if (passangerStatement.getSabarguli() == 1) bagCB.setChecked(true);
            else bagCB.setChecked(false);
            if (passangerStatement.getCxovelebi() == 1) animalCB.setChecked(true);
            else animalCB.setChecked(false);
        } else {
            pirobebi_detail.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_page_passanger, menu);

        menuItemedit = (MenuItem) menu.findItem(R.id.edit_dr_manu);
        menuItemdel = (MenuItem) menu.findItem(R.id.del_dr_manu);
        menuItemfav = (MenuItem) menu.findItem(R.id.fav_dr_manu);

        if (whereFrom.equals(Constantebi.ALL_STAT) || whereFrom.equals(Constantebi.FAVORIT_STAT)) {
            menuItemedit.setVisible(false);
            menuItemdel.setVisible(false);
        }
        if (whereFrom.equals(Constantebi.MY_OWN_STAT)) {
            menuItemfav.setVisible(false);
        }
        if (favoriteState)
            menuItemfav.setIcon(R.drawable.ic_star_white_24dp);

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

        if (id == R.id.fav_dr_manu) {
            favoriteState = !favoriteState;
            if (favoriteState) {
                item.setIcon(R.drawable.ic_star_white_24dp);
            } else {
                item.setIcon(R.drawable.ic_star_border_white_24dp);
            }
            return true;
        }

        if (id == R.id.del_dr_manu) {
            // serverze gaushvebt gancx ID-s wasashlelad
            // da dadebiti pasuxis shemtxvevashi lokaluradac wavshlit

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            String url = "http://geolab.club/geolabwork/kartlos/delete_st.php?stType=2&userID=" + passangerStatement.getUserID() + "&s_id=" + passangerStatement.getId();

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {

                    Toast.makeText(getApplicationContext(), "წაიშალა!", Toast.LENGTH_SHORT).show();

                    DBmanager.initialaize(getApplicationContext());
                    DBmanager.openWritable();
                    DBmanager.deletePassangerStatement(passangerStatement.getId());
                    DBmanager.close();

                    Intent toMyPage = new Intent(getApplicationContext(), MyStatements.class);
                    startActivity(toMyPage);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getApplicationContext(), volleyError.toString() + "   -   " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(jsonRequest);

            return true;
        }

        if (id == R.id.edit_dr_manu) {
            // aq unda gamovidzaxot gancxadebis Sesavsebi forma, romelic
            // shevsebuli iqneba redaqtirebadi gancxadebis parametrebiT
            // amitom intentshi vatant gancxadebas
            Intent intent = new Intent(getApplicationContext(), EditMyStatement.class);
            intent.putExtra("driver_st", passangerStatement);
            intent.putExtra("type", Constantebi.STAT_TYPE_PASSANGER);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String cityAtID(String c_ID){
        int id = Integer.parseInt(c_ID);
        for(int i=0; i< Constantebi.cityList.size(); i++){
            if(Constantebi.cityList.get(i).getC_id()==id){
                return Constantebi.cityList.get(i).getNameGE();
            }
        }
        return "";
    }
}
