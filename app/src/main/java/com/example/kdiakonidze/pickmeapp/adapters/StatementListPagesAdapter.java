package com.example.kdiakonidze.pickmeapp.adapters;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.kdiakonidze.pickmeapp.R;
import com.example.kdiakonidze.pickmeapp.fragments.DriverStatatementListFragment;
import com.example.kdiakonidze.pickmeapp.fragments.PassengerStatatementListFragment;

/**
 * Created by k.diakonidze on 7/10/2016.
 */
public class StatementListPagesAdapter extends FragmentPagerAdapter {

    int icons[] = {R.drawable.ic_filter_list_white_24dp, R.drawable.ic_account_circle_white_48dp};
    String titles[] = {"   წავიყვან   ","   წამიყვანეთ   "};
    private String location="";

    public StatementListPagesAdapter(FragmentManager fm, String location) {
        super(fm);
        this.location = location;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("location",location);

        Fragment fragment = null;
        if(position == 0){
            fragment = new DriverStatatementListFragment();
            fragment.setArguments(bundle);
        }
        if(position == 1){
            fragment = new PassengerStatatementListFragment();
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {

//        Drawable drawable = getResources().getDraweble

        return titles[position];
    }
}
