package com.example.kdiakonidze.pickmeapp.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.kdiakonidze.pickmeapp.fragments.AddDriverStatementF;
import com.example.kdiakonidze.pickmeapp.fragments.AddPassengetStatementF;
import com.example.kdiakonidze.pickmeapp.models.DriverStatement;
import com.example.kdiakonidze.pickmeapp.models.PassangerStatement;
import com.example.kdiakonidze.pickmeapp.utils.Constantebi;

/**
 * Created by k.diakonidze on 7/10/2016.
 */
public class AddStatementAdapter extends FragmentStatePagerAdapter {

    public AddStatementAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        bundle.putString("action", Constantebi.REASON_ADD);

        if (position == 0) {
            DriverStatement statement = new DriverStatement(Constantebi.MY_ID,0,0,"","","");
            bundle.putSerializable("statement", statement);
            fragment = new AddDriverStatementF();

            fragment.setArguments(bundle);
        }
        if (position == 1) {
            PassangerStatement statement = new PassangerStatement(Constantebi.MY_ID,0,0,"","","");
            bundle.putSerializable("statement", statement);
            fragment = new AddPassengetStatementF();
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
        if (position == 0) {
            return "ვეძებ მგზავრს";
        } else {
            return "ვეძებ ტრანსპორტს";
        }
    }
}