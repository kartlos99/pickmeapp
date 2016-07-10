package com.example.kdiakonidze.pickmeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.example.kdiakonidze.pickmeapp.adapters.AddStatementAdapter;
import com.example.kdiakonidze.pickmeapp.fragments.AddDriverStatementF;

public class AddStatement extends AppCompatActivity {

    private ViewPager viewPager;
    private Toolbar toolbar;
    private TabLayout tabsAddStatement;
    AddDriverStatementF fragment1;
    android.support.v4.app.FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_statement);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.add_statement_pager);
        fragmentManager = getSupportFragmentManager();
        AddStatementAdapter addStatementAdapter = new AddStatementAdapter(fragmentManager);
        viewPager.setAdapter(addStatementAdapter);

        tabsAddStatement = (TabLayout) findViewById(R.id.tabs_add_statement_page);
        tabsAddStatement.setupWithViewPager(viewPager);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_statement, menu);
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
