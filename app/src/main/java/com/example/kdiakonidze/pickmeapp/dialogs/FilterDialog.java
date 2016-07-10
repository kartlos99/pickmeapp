package com.example.kdiakonidze.pickmeapp.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.kdiakonidze.pickmeapp.MainActivity;
import com.example.kdiakonidze.pickmeapp.R;
import com.example.kdiakonidze.pickmeapp.utils.Constantebi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by k.diakonidze on 7/10/2016.
 */
public class FilterDialog extends DialogFragment {

    private Calendar currTime, time1, time2;
    Button filterBtn, filter_pirobebi_btn, filterExitBtn;
    RelativeLayout comfortBox;
    Boolean pirobebi = false;
    CheckBox filterDriver, filterPassanger, chk_cond, chk_sigar, chk_sabarg, chk_atplace, chk_cxov;
    private ArrayAdapter<String> cityFromAdapret;
    private ArrayAdapter<String> cityToAdapret;
    private ArrayAdapter<String> dateSpinnerAdapter1;
    private ArrayAdapter<String> dateSpinnerAdapter2;
    private List<String> citylist = new ArrayList<>();
    private List<String> datelist = new ArrayList<>();

    AutoCompleteTextView cityFrom, cityTo;
    Spinner dateSpinner1, dateSpinner2;

    private String setedDate1, setedDate2;

    DatePickerDialog.OnDateSetListener datelistener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            time1.set(Calendar.YEAR, year);
            time1.set(Calendar.MONTH, monthOfYear);
            time1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            setedDate1 = dateFormat.format(time1.getTime());

            if (!datelist.contains(setedDate1)) {
                datelist.add(setedDate1);
                ((ArrayAdapter<String>) dateSpinner1.getAdapter()).notifyDataSetChanged();
            }
            dateSpinner1.setSelection(getIndexInSpinner(dateSpinner1, setedDate1));
        }
    };

    DatePickerDialog.OnDateSetListener datelistener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            time2.set(Calendar.YEAR, year);
            time2.set(Calendar.MONTH, monthOfYear);
            time2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            setedDate2 = dateFormat.format(time2.getTime());

            if (!datelist.contains(setedDate2)) {
                datelist.add(setedDate2);
                ((ArrayAdapter<String>) dateSpinner2.getAdapter()).notifyDataSetChanged();
            }
            dateSpinner2.setSelection(getIndexInSpinner(dateSpinner2, setedDate2));
        }
    };

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("ფილტრი                 ");
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_layout, container, false);
        filterBtn = (Button) view.findViewById(R.id.filter_Btn);
        filterExitBtn = (Button) view.findViewById(R.id.filter_Exit_Btn);
        filter_pirobebi_btn = (Button) view.findViewById(R.id.filter_pirobebi_btn);
        filterDriver = (CheckBox) view.findViewById(R.id.filterDriver);
        filterPassanger = (CheckBox) view.findViewById(R.id.filterPassanger);
        cityFrom = (AutoCompleteTextView) view.findViewById(R.id.filter1_cityfrom);
        cityTo = (AutoCompleteTextView) view.findViewById(R.id.filter1_cityto);
        dateSpinner1 = (Spinner) view.findViewById(R.id.filter_time1);
        dateSpinner2 = (Spinner) view.findViewById(R.id.filter_time2);
        comfortBox = (RelativeLayout) view.findViewById(R.id.filter_comfortBox);
        chk_cond = (CheckBox) view.findViewById(R.id.filter_conditioner_checkBox);
        chk_sigar = (CheckBox) view.findViewById(R.id.filter_cigar_checkBox);
        chk_atplace = (CheckBox) view.findViewById(R.id.filter_atplace_checkBox);
        chk_sabarg = (CheckBox) view.findViewById(R.id.filter_baggage_checkBox);
        chk_cxov = (CheckBox) view.findViewById(R.id.filter_animal_checkBox);

        filterDriver.setChecked(true);
        filterPassanger.setChecked(true);

        datelist.clear();
        datelist.add("დღეს");
        datelist.add("ხვალ");
        datelist.add("ზეგ");
        datelist.add("სხვა");

        dateSpinnerAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, datelist);
        dateSpinnerAdapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, datelist);

        dateSpinner1.setAdapter(dateSpinnerAdapter1);
        dateSpinner2.setAdapter(dateSpinnerAdapter2);

        currTime = Calendar.getInstance();
        time1 = Calendar.getInstance();
        time2 = Calendar.getInstance();

        dateSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 3) {
                    DatePickerDialog dialog = new DatePickerDialog(getActivity(), datelistener, currTime.get(Calendar.YEAR), currTime.get(Calendar.MONTH), currTime.get(Calendar.DAY_OF_MONTH));
                    dialog.setCancelable(false);
                    dialog.show();

                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            dateSpinner1.setSelection(0);
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dateSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 3) {
                    DatePickerDialog dialog = new DatePickerDialog(getActivity(), datelistener2, currTime.get(Calendar.YEAR), currTime.get(Calendar.MONTH), currTime.get(Calendar.DAY_OF_MONTH));
                    dialog.setCancelable(false);
                    dialog.show();

                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            dateSpinner2.setSelection(0);
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        citylist.clear();
        for (int i = 0; i < Constantebi.cityList.size(); i++) {
            citylist.add(Constantebi.cityList.get(i).getNameGE());
        }

        cityFromAdapret = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, citylist);
        cityToAdapret = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, citylist);

        cityFrom.setAdapter(cityFromAdapret);
        cityFrom.setThreshold(1);
        cityTo.setAdapter(cityToAdapret);
        cityTo.setThreshold(1);


        filter_pirobebi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pirobebi = !pirobebi;
                if (pirobebi) {
                    comfortBox.setVisibility(View.VISIBLE);
                    filter_pirobebi_btn.setBackgroundResource(R.drawable.greenbtn_lite);
                } else {
                    comfortBox.setVisibility(View.GONE);
                    filter_pirobebi_btn.setBackgroundResource(R.drawable.greenbtn_dark);
                }
            }
        });

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constantebi.FILTER_CRITERIUM = readCriterium();

                Constantebi.FILTER_DR = filterDriver.isChecked();
                Constantebi.FILTER_PS = filterPassanger.isChecked();

                Constantebi.IS_FILTERED = true;
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
//
//                DriverStatatementListFragment dlf = new DriverStatatementListFragment();
//                dlf.getDriversStatements();

                dismiss();
            }
        });

        filterExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        return view;

    }

    private String readCriterium(){
        String url = "";

        Constantebi.FILTER_DR = filterDriver.isChecked();
        Constantebi.FILTER_PS = filterPassanger.isChecked();

        String cityF = "1";
        if (citylist.contains(cityFrom.getText().toString())) {
            cityF = String.valueOf(Constantebi.cityList.get(citylist.indexOf(cityFrom.getText().toString())).getC_id());
        }
        String cityT = "1";
        if (citylist.contains(cityTo.getText().toString())) {
            cityT = String.valueOf(Constantebi.cityList.get(citylist.indexOf(cityTo.getText().toString())).getC_id());
        }
        url = "&cityFrom=" + cityF + "&cityTo=" + cityT;

        if (dateSpinner1.getSelectedItemPosition() < 3) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, dateSpinner1.getSelectedItemPosition());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            setedDate1 = dateFormat.format(calendar.getTime());
        } else {
            setedDate1 = dateSpinner1.getSelectedItem().toString();
        }
        if (dateSpinner2.getSelectedItemPosition() < 3) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, dateSpinner2.getSelectedItemPosition());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            setedDate2 = dateFormat.format(calendar.getTime());
        } else {
            setedDate2 = dateSpinner2.getSelectedItem().toString();
        }
        url += "&date1=" + setedDate1 + "&date2=" + setedDate2;

        if (pirobebi) {
            if (chk_cond.isChecked()) {
                url += "&kondincioneri=1";
            }
            if (chk_atplace.isChecked()) {
                url += "&adgilzemisvla=1";
            }
            if (chk_sigar.isChecked()) {
                url += "&sigareti=1";
            }
            if (chk_sabarg.isChecked()) {
                url += "&sabarguli=1";
            }
            if (chk_cxov.isChecked()) {
                url += "&cxoveli=1";
            }
        }

        return url;
    }

    private int getIndexInSpinner(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }
}
