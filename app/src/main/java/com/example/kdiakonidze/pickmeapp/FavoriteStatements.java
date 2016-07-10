package com.example.kdiakonidze.pickmeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.kdiakonidze.pickmeapp.adapters.StatementListPagesAdapter;
import com.example.kdiakonidze.pickmeapp.utils.Constantebi;


public class FavoriteStatements extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager pager = null;
    private TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_statements_layout);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("რჩეული განცხ.");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pager = (ViewPager) findViewById(R.id.pageView);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        StatementListPagesAdapter myadapter = new StatementListPagesAdapter(fragmentManager, Constantebi.FAVORIT_STAT);
        pager.setAdapter(myadapter);

        tabs = (TabLayout) findViewById(R.id.tabs_fav_page);
        tabs.setupWithViewPager(pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favorite_statements, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
