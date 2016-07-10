package com.example.kdiakonidze.pickmeapp.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kdiakonidze.pickmeapp.R;
import com.example.kdiakonidze.pickmeapp.datebase.DBmanager;
import com.example.kdiakonidze.pickmeapp.models.CityConnection;
import com.example.kdiakonidze.pickmeapp.models.CityObj;
import com.example.kdiakonidze.pickmeapp.models.DriverStatement;
import com.example.kdiakonidze.pickmeapp.utils.Constantebi;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by k.diakonidze on 7/10/2016.
 */
public class AddDriverStatementF extends Fragment implements View.OnClickListener {
    private Calendar runTimeC;
    private TextView passAgeText;
    private Button driverDonebtn, pirobebiBtn, limitBtn, takePhotoBtn;
    private DriverStatement driverStatement;
    private ImageView carImageView;
    private ImageView carType1;
    private ImageView carType2;
    private ImageView carType3;
    private ImageView carType4;
    private ImageView carType5;

    private int carType = 0;
    private double pathLength = 0, pathTime = 0;
    private String pathItems = "", pathItemsID = "";

    private Uri uri;
    private File imagefile;

    Spinner freeSpaceSpinner, priceSpinner, markaSpinner, modelSpinner, genderSpinner, colorSpinner;
    CheckBox condicionerCK, atplaceCK, cigarCK, baggageCK, animalCK;
    EditText commentText;
    RelativeLayout comfort1;
    LinearLayout passangerLimitBox;
    AutoCompleteTextView cityFrom, cityTo;
    SeekBar seekBar;
    LinearLayout pathInfoBox;
    TextView pathItemText, pathDistanceText;

    private ArrayAdapter<String> cityFromAdapret;
    private ArrayAdapter<String> cityToAdapret;

    private ArrayAdapter<String> dateSpinnerAdapter;
    private ArrayAdapter<String> timeSpinnerAdapter;
    private ArrayAdapter<String> freeSpaceSpinnerAdapter;
    private ArrayAdapter<String> priceSpinnerAdapter;
    private ArrayAdapter<String> brendSpinerAdapter;
    private ArrayAdapter<String> modelSpinnerAdapter;

    private List<String> brendlist = new ArrayList<>();
    private List<String> modellist = new ArrayList<>();
    private List<String> timelist = new ArrayList<>();
    private List<String> datelist = new ArrayList<>();
    private List<String> citylist = new ArrayList<>();
    private List<String> freespacelist = new ArrayList<>();
    private List<String> pricelist = new ArrayList<>();
    private Spinner runDateSpinner;
    private Spinner runTimeSpinner;

    String setedDate, setedtime, workState;
    Boolean pirobebi, passengerLimit;


    DatePickerDialog.OnDateSetListener datelistener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            runTimeC.set(Calendar.YEAR, year);
            runTimeC.set(Calendar.MONTH, monthOfYear);
            runTimeC.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            setedDate = dateFormat.format(runTimeC.getTime());

            if (!datelist.contains(setedDate)) {
                datelist.add(setedDate);
                ((ArrayAdapter<String>) runDateSpinner.getAdapter()).notifyDataSetChanged();
            }
            runDateSpinner.setSelection(getIndexInSpinner(runDateSpinner, setedDate));
        }
    };

    TimePickerDialog.OnTimeSetListener timelistener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            runTimeC.set(Calendar.HOUR_OF_DAY, hourOfDay);
            runTimeC.set(Calendar.MINUTE, minute);
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            setedtime = dateFormat.format(runTimeC.getTime());

            if (!timelist.contains(setedtime)) {
                timelist.add(setedtime);
                ((ArrayAdapter<String>) runTimeSpinner.getAdapter()).notifyDataSetChanged();
            }
            runTimeSpinner.setSelection(getIndexInSpinner(runTimeSpinner, setedtime));
        }
    };

    public String convertImigToSrt(String teUri) {
//        System.out.print("______________");
        Bitmap bitmap = BitmapFactory.decodeFile(teUri);

        if (bitmap == null) {
            return "";
        } else {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 2, outputStream);

            byte[] bytes = outputStream.toByteArray();

            return Base64.encodeToString(bytes, Base64.DEFAULT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                if (imagefile.exists()) {
                    Toast.makeText(getActivity(), "sheinaxa " + imagefile.getAbsolutePath(), Toast.LENGTH_LONG).show();
//                    imageView.setImageURI(uri);
                    carImageView.clearAnimation();
                    Picasso.with(getActivity())
                            .load(imagefile)
                            .resize(500, 200)
                            .centerCrop()
                            .into(carImageView);
                } else {
                    Toast.makeText(getActivity(), "imagefile Error", Toast.LENGTH_LONG).show();
                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "uari fotoze", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initialazeAll();
        workState = getArguments().getString("action");

        if (savedInstanceState != null) {
            // tu reCreate moxda grafebshi vabrumebt ra mdgomareobac iyo
            driverStatement = (DriverStatement) savedInstanceState.getSerializable("statement");
            pathItems = savedInstanceState.getString("pathItems");
            pathLength = savedInstanceState.getDouble("distance");
            pathTime = savedInstanceState.getDouble("time");
            fillForm(driverStatement);
            drawPathInfo();
        } else {  // pirvelad chaitvirta es forma
            driverStatement = (DriverStatement) getArguments().getSerializable("statement");
            // chemi gancxadebis gaxsna redaqtirebisatvis
            if (workState.equals(Constantebi.REASON_EDIT)) {
                fillForm(driverStatement);
            }
            // axali gancxadebis chawera
            if (getArguments().getString("action").equals(Constantebi.REASON_ADD)) {

            }
        }


//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        // suratis gadageba ************************************************

        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imagefile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "my1photo.jpg");

                uri = Uri.fromFile(imagefile);
                imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                imageIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0.1);
                startActivityForResult(imageIntent, 0);
            }
        });

        // დროის დაყენება
        runDateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // tu aavirchiet "sxva" romelic me4 poziciaa gamova datepikeri
                if (i == 3) {
                    DatePickerDialog dialog = new DatePickerDialog(getActivity(), datelistener, runTimeC.get(Calendar.YEAR), runTimeC.get(Calendar.MONTH), runTimeC.get(Calendar.DAY_OF_MONTH));
                    dialog.setCancelable(false);
                    dialog.show();

                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            runDateSpinner.setSelection(0);
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        runTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 3) {
                    TimePickerDialog dialog = new TimePickerDialog(getActivity(), timelistener, runTimeC.get(Calendar.HOUR_OF_DAY), runTimeC.get(Calendar.MINUTE), false);
                    dialog.setCancelable(false);
                    dialog.show();

                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            runTimeSpinner.setSelection(0);
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // modelebis gafiltvra brendebis mixedvit
        markaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                modelebisListisDayeneba(position);
//                modelSpinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // damatebiti pirobebis manipulaciebi

        pirobebiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pirobebi = !pirobebi;
                if (pirobebi) {
                    comfort1.setVisibility(View.VISIBLE);
                    pirobebiBtn.setBackgroundResource(R.drawable.greenbtn_lite);
                } else {
                    comfort1.setVisibility(View.GONE);
                    pirobebiBtn.setBackgroundResource(R.drawable.greenbtn_dark);
                }
            }
        });


        // mgzavrze shezgudvebis dayeneba

        limitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passengerLimit = !passengerLimit;
                if (passengerLimit) {
                    passangerLimitBox.setVisibility(View.VISIBLE);
                    limitBtn.setBackgroundResource(R.drawable.greenbtn_lite);
                } else {
                    passangerLimitBox.setVisibility(View.GONE);
                    limitBtn.setBackgroundResource(R.drawable.greenbtn_dark);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                passAgeText.setText("მაქს. ასაკი " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // chawera / gagzavna bazashi

        driverDonebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!citylist.contains(cityFrom.getText().toString()) || !citylist.contains(cityTo.getText().toString())) {
                    Toast.makeText(getActivity(), "ქალაქი არასწორადაა მითითებული!", Toast.LENGTH_LONG).show();
                } else {
                    driverStatement = readForm();
                    driverStatement.setUserID(Constantebi.MY_ID);

                    RequestQueue queue = Volley.newRequestQueue(getActivity());

                    if (workState.equals(Constantebi.REASON_ADD)) {

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constantebi.URL_INSERT_ST1,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        // response -shi gvibrunebs ID -s
                                        Toast.makeText(getActivity(), "განცხადება დამატებულია!", Toast.LENGTH_SHORT).show();

                                        driverStatement.setId(Integer.valueOf(response));
                                        // tu warmatebit ganxorcielda bazashi chawera, chventanac vinaxavt localurad
                                        DBmanager.initialaize(getActivity());
                                        DBmanager.openWritable();
                                        DBmanager.insertIntoDriver(driverStatement, Constantebi.MY_STATEMENT);
                                        DBmanager.close();

                                        getActivity().onBackPressed();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                })
                        {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();

                                params.put("cityFrom", driverStatement.getCityFrom());
                                params.put("cityTo", driverStatement.getCityTo());
                                params.put("cityPath", driverStatement.getCityPath());
                                params.put("date", driverStatement.getDate());
                                params.put("time", driverStatement.getTime());
                                params.put("freespace", String.valueOf(driverStatement.getFreeSpace()));
                                params.put("price", String.valueOf(driverStatement.getPrice()));
                                params.put("kondincioneri", String.valueOf(driverStatement.getKondencioneri()));
                                params.put("sigareti",String.valueOf( driverStatement.getSigareti()));
                                params.put("sabarguli", String.valueOf(driverStatement.getSabarguli()));
                                params.put("adgilzemisvla", String.valueOf(driverStatement.getAtHome()));
                                params.put("cxoveli", String.valueOf(driverStatement.getCxovelebi()));
                                params.put("placex", "555");
                                params.put("placey", "555");
                                params.put("ageFrom", "1");
                                params.put("ageTo", String.valueOf(driverStatement.getAgeTo()));
                                params.put("gender", String.valueOf(driverStatement.getGender()));
                                params.put("comment", String.valueOf(driverStatement.getComment()));
                                params.put("mobile", Constantebi.MY_MOBILE);
                                params.put("status", "1");
                                params.put("sex", "1");
                                params.put("cartype", String.valueOf(driverStatement.getMarka()));
//                        params.put("photo", Constantebi.profile.getProfilePictureUri(200, 300).toString());
                                params.put("user_id", driverStatement.getUserID());
                                if (uri != null) {
                                    params.put("image", convertImigToSrt(uri.getPath()));
                                } else {
                                    params.put("image", "");
                                }

                                params.toString();
                                return params;
                            }
                        };

                        queue.add(stringRequest);

                    } else {       // **** aq modis ganaxlebis dros ******

                        StringRequest updateRequest = new StringRequest(Request.Method.POST, Constantebi.URL_UPDATE_ST1,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Toast.makeText(getActivity(), "ცვლილებები შენახულია!", Toast.LENGTH_SHORT).show();
                                        // tu warmatebit ganxorcielda bazashi chawera, chventanac vinaxavt localurad
                                        DBmanager.initialaize(getActivity());
                                        DBmanager.openWritable();
                                        DBmanager.updateDriverStatement(driverStatement);
                                        DBmanager.close();

                                        getActivity().onBackPressed();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                })
                        {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();

                                params.put("cityFrom", driverStatement.getCityFrom());
                                params.put("cityTo", driverStatement.getCityTo());
                                params.put("cityPath", driverStatement.getCityPath());
                                params.put("date", driverStatement.getDate());
                                params.put("time", driverStatement.getTime());
                                params.put("freespace", String.valueOf(driverStatement.getFreeSpace()));
                                params.put("price", String.valueOf(driverStatement.getPrice()));
                                params.put("kondincioneri", String.valueOf(driverStatement.getKondencioneri()));
                                params.put("sigareti",String.valueOf( driverStatement.getSigareti()));
                                params.put("sabarguli", String.valueOf(driverStatement.getSabarguli()));
                                params.put("adgilzemisvla", String.valueOf(driverStatement.getAtHome()));
                                params.put("cxoveli", String.valueOf(driverStatement.getCxovelebi()));
                                params.put("placex", "555");
                                params.put("placey", "555");
                                params.put("ageFrom", "1");
                                params.put("ageTo", String.valueOf(driverStatement.getAgeTo()));
                                params.put("gender", String.valueOf(driverStatement.getGender()));
                                params.put("comment", String.valueOf(driverStatement.getComment()));
                                params.put("mobile", Constantebi.MY_MOBILE);
                                params.put("status", "1");
                                params.put("sex", "1");
                                params.put("cartype", String.valueOf(driverStatement.getMarka()));
//                        params.put("photo", Constantebi.profile.getProfilePictureUri(200, 300).toString());
                                params.put("user_id", driverStatement.getUserID());
                                if (uri != null) {
                                    params.put("image", convertImigToSrt(uri.getPath()));
                                } else {
                                    params.put("image", "");
                                }
                                params.put("s_id", String.valueOf(driverStatement.getId()));

                                params.toString();
                                return params;
                            }
                        };

                        queue.add(updateRequest);
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        cityFromAdapret = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, citylist);
        cityToAdapret = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, citylist);

        cityFrom.setAdapter(cityFromAdapret);
        cityFrom.setThreshold(0);
        cityTo.setAdapter(cityToAdapret);
        cityTo.setThreshold(0);

        cityFrom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (cityFrom.getText().toString().equals(""))
                        cityFrom.showDropDown();
                    cityFrom.setTextColor(Color.BLACK);
                } else {
                    if (!citylist.contains(cityFrom.getText().toString()))
                        cityFrom.setTextColor(Color.RED);
                }
            }
        });

        cityTo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (cityTo.getText().toString().equals(""))
                        cityTo.showDropDown();
                    cityTo.setTextColor(Color.BLACK);
                } else {
                    if (!citylist.contains(cityTo.getText().toString()))
                        cityTo.setTextColor(Color.RED);
                }
            }
        });

        cityFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pathItems = "";
                pathTime = 0;
                pathLength = 0;
                if (citylist.contains(cityFrom.getText().toString()) && citylist.contains(cityTo.getText().toString())) {
                    if (!cityFrom.getText().toString().equals(cityTo.getText().toString())) {
                        getCityPath(String.valueOf(Constantebi.cityList.get(citylist.indexOf(cityFrom.getText().toString())).getC_id()), String.valueOf(Constantebi.cityList.get(citylist.indexOf(cityTo.getText().toString())).getC_id()));
                    }
                }
                drawPathInfo();
            }
        });

        cityTo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pathItems = "";
                pathTime = 0;
                pathLength = 0;
                if (citylist.contains(cityFrom.getText().toString()) && citylist.contains(cityTo.getText().toString())) {
                    if (!cityFrom.getText().toString().equals(cityTo.getText().toString())) {
                        getCityPath(String.valueOf(Constantebi.cityList.get(citylist.indexOf(cityFrom.getText().toString())).getC_id()), String.valueOf(Constantebi.cityList.get(citylist.indexOf(cityTo.getText().toString())).getC_id()));
                    }
                }
                drawPathInfo();
            }
        });

    }

    private void drawPathInfo() {
        pathInfoBox.setVisibility(View.VISIBLE);
        String cf = cityFrom.getText().toString();
        String ct = cityTo.getText().toString();
        if (pathItems.equals("")) {
            pathItemText.setText(cf + " - " + ct);
        } else {
            pathItemText.setText(cf + " - " + pathItems + " - " + ct);
        }
        pathDistanceText.setText(String.valueOf(pathLength) + "კმ.  " + String.valueOf((int) (pathTime / 60)) + "სთ " + String.valueOf((int) (pathTime % 60)) + "წთ");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.add_driver_statement_layout, container, false);

        pathInfoBox = (LinearLayout) view.findViewById(R.id.path_info_box);
        pathItemText = (TextView) view.findViewById(R.id.city_path_text);
        pathDistanceText = (TextView) view.findViewById(R.id.city_path_distance);
        freeSpaceSpinner = (Spinner) view.findViewById(R.id.driver_freespace_spiner);
        priceSpinner = (Spinner) view.findViewById(R.id.driver_price_spiner);
        markaSpinner = (Spinner) view.findViewById(R.id.driver_marka_spiner);
        modelSpinner = (Spinner) view.findViewById(R.id.driver_model_spiner);
        condicionerCK = (CheckBox) view.findViewById(R.id.driver_conditioner_checkBox);
        atplaceCK = (CheckBox) view.findViewById(R.id.driver_atplace_checkBox);
        cigarCK = (CheckBox) view.findViewById(R.id.driver_cigar_checkBox);
        baggageCK = (CheckBox) view.findViewById(R.id.driver_baggage_checkBox);
        animalCK = (CheckBox) view.findViewById(R.id.driver_animal_checkBox);
        commentText = (EditText) view.findViewById(R.id.driver_comment);
        genderSpinner = (Spinner) view.findViewById(R.id.driver_sex_spiner);
        cityFrom = (AutoCompleteTextView) view.findViewById(R.id.driver_cityfrom);
        cityTo = (AutoCompleteTextView) view.findViewById(R.id.driver_cityto);
        runDateSpinner = (Spinner) view.findViewById(R.id.driver_date_spiner);
        runTimeSpinner = (Spinner) view.findViewById(R.id.driver_time_spiner);
        comfort1 = (RelativeLayout) view.findViewById(R.id.comfort1);
        passangerLimitBox = (LinearLayout) view.findViewById(R.id.driver_passanger_restrict_box);
        seekBar = (SeekBar) view.findViewById(R.id.driver_pass_age_seek);
        driverDonebtn = (Button) view.findViewById(R.id.done_driver);
        colorSpinner = (Spinner) view.findViewById(R.id.driver_color_spiner);
        passAgeText = (TextView) view.findViewById(R.id.driver_pass_age_text);
        limitBtn = (Button) view.findViewById(R.id.driver_limit_btn);
        pirobebiBtn = (Button) view.findViewById(R.id.driver_pirobebi_btn);
        takePhotoBtn = (Button) view.findViewById(R.id.take_photo_btn);
        carImageView = (ImageView) view.findViewById(R.id.car_image);
        carType1 = (ImageView) view.findViewById(R.id.car_type_1);
        carType2 = (ImageView) view.findViewById(R.id.car_type_2);
        carType3 = (ImageView) view.findViewById(R.id.car_type_3);
        carType4 = (ImageView) view.findViewById(R.id.car_type_4);
        carType5 = (ImageView) view.findViewById(R.id.car_type_5);

        carType1.setOnClickListener(this);
        carType2.setOnClickListener(this);
        carType3.setOnClickListener(this);
        carType4.setOnClickListener(this);
        carType5.setOnClickListener(this);

        return view;
    }

    private void fillForm(DriverStatement statement) {

        cityFrom.setText(statement.getCityFrom());
        cityTo.setText(statement.getCityTo());

        if (!datelist.contains(statement.getDate())) {
            datelist.add(statement.getDate());
            ((ArrayAdapter<String>) runDateSpinner.getAdapter()).notifyDataSetChanged();
            setedDate = statement.getDate();
        }
        runDateSpinner.setSelection(getIndexInSpinner(runDateSpinner, statement.getDate()));

        if (!timelist.contains(statement.getTime())) {
            timelist.add(statement.getTime());
            ((ArrayAdapter<String>) runTimeSpinner.getAdapter()).notifyDataSetChanged();
        }
        runTimeSpinner.setSelection(getIndexInSpinner(runTimeSpinner, statement.getTime()));

        freeSpaceSpinner.setSelection(statement.getFreeSpace() - 1);
        priceSpinner.setSelection(statement.getPrice());

//        markaSpinner.setSelection(statement.getMarka());
//        modelebisListisDayeneba(statement.getMarka());
//        modelSpinner.setSelection(statement.getModeli());
//        colorSpinner.setSelection(statement.getColor());

        /*carType1.setBackground(null);
        carType2.setBackground(null);
        carType3.setBackground(null);
        carType4.setBackground(null);
        carType5.setBackground(null);

        switch (statement.getMarka()) {
            case 0:
                carType1.setBackground(getResources().getDrawable(R.drawable.abc_tab_indicator_mtrl_alpha));
                carType = 0;
                break;
            case 1:
                carType2.setBackground(getResources().getDrawable(R.drawable.abc_tab_indicator_mtrl_alpha));
                carType = 1;
                break;
            case 2:
                carType3.setBackground(getResources().getDrawable(R.drawable.abc_tab_indicator_mtrl_alpha));
                carType = 2;
                break;
            case 3:
                carType4.setBackground(getResources().getDrawable(R.drawable.abc_tab_indicator_mtrl_alpha));
                carType = 3;
                break;
            case 4:
                carType5.setBackground(getResources().getDrawable(R.drawable.abc_tab_indicator_mtrl_alpha));
                carType = 4;
                break;
        }*/

        if (statement.getAtHome() != 2) {
            pirobebi = true;
            pirobebiBtn.setBackgroundResource(R.drawable.greenbtn_lite);
            comfort1.setVisibility(View.VISIBLE);
            if (statement.getKondencioneri() == 1) condicionerCK.setChecked(true);
            else condicionerCK.setChecked(false);
            if (statement.getSigareti() == 1) cigarCK.setChecked(true);
            else cigarCK.setChecked(false);
            if (statement.getAtHome() == 1) atplaceCK.setChecked(true);
            else atplaceCK.setChecked(false);
            if (statement.getCxovelebi() == 1) animalCK.setChecked(true);
            else animalCK.setChecked(false);
            if (statement.getSabarguli() == 1) baggageCK.setChecked(true);
            else baggageCK.setChecked(false);
        } else {
            pirobebi = false;
            pirobebiBtn.setBackgroundResource(R.drawable.greenbtn_dark);
            comfort1.setVisibility(View.GONE);
        }

        if (statement.getAgeTo() != 1000) {
            seekBar.setProgress(statement.getAgeTo());
            genderSpinner.setSelection(statement.getGender());
            passAgeText.setText("მაქს. ასაკი " + statement.getAgeTo());
            passangerLimitBox.setVisibility(View.VISIBLE);
            limitBtn.setBackgroundResource(R.drawable.greenbtn_lite);
            passengerLimit = true;
        } else {
            seekBar.setProgress(1);
            genderSpinner.setSelection(0);
            passangerLimitBox.setVisibility(View.GONE);
            limitBtn.setBackgroundResource(R.drawable.greenbtn_dark);
            passengerLimit = false;
        }
        commentText.setText(statement.getComment());
    }

    private int BoolToInt(boolean checked) {
        if (checked) {
            return 1;
        } else {
            return 0;
        }
    }


    // formistvis sachiro monacemebis chatvirtva
    private void initialazeAll() {

        pirobebi = false;
        passengerLimit = false;

        seekBar.setMax(80);
        runTimeC = Calendar.getInstance();

        citylist.clear();
        for (int i = 0; i < Constantebi.cityList.size(); i++) {
            citylist.add(Constantebi.cityList.get(i).getNameGE());
        }


        timelist.add("დილით");
        timelist.add("შუადღეს");
        timelist.add("საღამოს");
        timelist.add("სხვა");

        datelist.add("დღეს");
        datelist.add("ხვალ");
        datelist.add("ზეგ");
        datelist.add("სხვა");

        setedDate = "";
        setedtime = "";

        for (int i = 1; i < 10; i++) {
            freespacelist.add(String.valueOf(i));
        }
        for (int i = 0; i < 51; i++) {
            pricelist.add(String.valueOf(i));
        }

//        brendlist.clear();
//        modellist.clear();
//        for (int i = 0; i < Constantebi.brendList.size(); i++) {
//            brendlist.add(Constantebi.brendList.get(i).getMarka());
//        }
//
//        for (int i = 0; i < Constantebi.modelList.size(); i++) {
//            if (Constantebi.modelList.get(i).getBrendID() == 1) {
//                modellist.add(Constantebi.modelList.get(i).getModel());
//            }
//        }
//        carType2.setBackground(getResources().getDrawable(R.drawable.abc_tab_indicator_mtrl_alpha));
        carType = 1;

        passAgeText.setText("მაქს. ასაკი " + seekBar.getProgress());


        modelSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, modellist);
        brendSpinerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, brendlist);
        dateSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, datelist);
        timeSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, timelist);
        freeSpaceSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, freespacelist);
        priceSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, pricelist);


        modelSpinner.setAdapter(modelSpinnerAdapter);
        markaSpinner.setAdapter(brendSpinerAdapter);
        freeSpaceSpinner.setAdapter(freeSpaceSpinnerAdapter);
        priceSpinner.setAdapter(priceSpinnerAdapter);
        runDateSpinner.setAdapter(dateSpinnerAdapter);
        runTimeSpinner.setAdapter(timeSpinnerAdapter);

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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        driverStatement = readForm();

        outState.putSerializable("statement", driverStatement);

        outState.putString("pathItems", pathItems);
        outState.putDouble("distance", pathLength);
        outState.putDouble("time", pathTime);
    }

    // formidan informaciis wakitxva ***************************************
    private DriverStatement readForm() {

        if (runDateSpinner.getSelectedItemPosition() < 3) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, runDateSpinner.getSelectedItemPosition());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            setedDate = dateFormat.format(runTimeC.getTime());
        }else{
            setedDate = runDateSpinner.getSelectedItem().toString();
        }

        String cityF = "1";
        if (citylist.contains(cityFrom.getText().toString())) {
            cityF = String.valueOf(Constantebi.cityList.get(citylist.indexOf(cityFrom.getText().toString())).getC_id());
        }
        String cityT = "1";
        if (citylist.contains(cityTo.getText().toString())) {
            cityT = String.valueOf(Constantebi.cityList.get(citylist.indexOf(cityTo.getText().toString())).getC_id());
        }

        DriverStatement statement = new DriverStatement(Constantebi.MY_ID,
                freeSpaceSpinner.getSelectedItemPosition() + 1,
                Integer.valueOf(priceSpinner.getSelectedItem().toString()),
                setedDate,
                cityF,
                cityT);

        statement.setTime(runTimeSpinner.getSelectedItem().toString());
        statement.setCityPath(pathItemsID);

        statement.setMarka(carType);
//        statement.setMarka(markaSpinner.getSelectedItemPosition());
//        statement.setModeli(modelSpinner.getSelectedItemPosition());
//        statement.setColor(colorSpinner.getSelectedItemPosition());

        if (pirobebi) {
            statement.setKondencioneri(BoolToInt(condicionerCK.isChecked()));
            statement.setAtHome(BoolToInt(atplaceCK.isChecked()));
            statement.setSigareti(BoolToInt(cigarCK.isChecked()));
            statement.setSabarguli(BoolToInt(baggageCK.isChecked()));
            statement.setCxovelebi(BoolToInt(animalCK.isChecked()));
        } else {
            statement.setKondencioneri(2);
            statement.setAtHome(2);
            statement.setSigareti(2);
            statement.setSabarguli(2);
            statement.setCxovelebi(2);
        }

        if (passengerLimit) {
            statement.setAgeTo(seekBar.getProgress());
            statement.setGender(genderSpinner.getSelectedItemPosition());
        } else {
            statement.setAgeTo(1000);
            statement.setGender(2);
        }
        statement.setComment(commentText.getText().toString());

        statement.setId(driverStatement.getId());
        statement.setUserID(driverStatement.getUserID());

        return statement;
    }

    private void modelebisListisDayeneba(int position) {
        String br = brendlist.get(position).toString();
        int br_id = 0;
        for (int i = 0; i < Constantebi.brendList.size(); i++) {
            if (br.equals(Constantebi.brendList.get(i).getMarka())) {
                br_id = Constantebi.brendList.get(i).getId();
            }
        }
        modellist.clear();
        for (int i = 0; i < Constantebi.modelList.size(); i++) {
            if (Constantebi.modelList.get(i).getBrendID() == br_id) {
                if (!Constantebi.modelList.get(i).getModel().equals(""))
                    modellist.add(Constantebi.modelList.get(i).getModel());
            }
        }
        if (modellist.size() == 0) {
            modellist.add("-");
        }
        modelSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, modellist);
        ((ArrayAdapter<String>) modelSpinner.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        /*carType1.setBackground(null);
        carType2.setBackground(null);
        carType3.setBackground(null);
        carType4.setBackground(null);
        carType5.setBackground(null);

        switch (view.getId()) {
            case R.id.car_type_1:
                carType1.setBackground(getResources().getDrawable(R.drawable.abc_tab_indicator_mtrl_alpha));
                carType = 0;
                break;
            case R.id.car_type_2:
                carType2.setBackground(getResources().getDrawable(R.drawable.abc_tab_indicator_mtrl_alpha));
                carType = 1;
                break;
            case R.id.car_type_3:
                carType3.setBackground(getResources().getDrawable(R.drawable.abc_tab_indicator_mtrl_alpha));
                carType = 2;
                break;
            case R.id.car_type_4:
                carType4.setBackground(getResources().getDrawable(R.drawable.abc_tab_indicator_mtrl_alpha));
                carType = 3;
                break;
            case R.id.car_type_5:
                carType5.setBackground(getResources().getDrawable(R.drawable.abc_tab_indicator_mtrl_alpha));
                carType = 4;
                break;
        }*/
    }


    private void getCityPath(String start_City, String finish_City) {


        ArrayList<CityConnection> cityConnectionList = new ArrayList<>();

        cityConnectionList.add(new CityConnection("ბათუმი", "ქობულეთი", 38, 29800));
        cityConnectionList.add(new CityConnection("ურეკი", "ფოთი", 23, 23000));
        cityConnectionList.add(new CityConnection("ურეკი", "ოზურგეთი", 31, 29000));
        cityConnectionList.add(new CityConnection("ურეკი", "ლანჩხუთი", 25, 26700));
        cityConnectionList.add(new CityConnection("ქობულეთი", "ურეკი", 20, 21400));
        cityConnectionList.add(new CityConnection("ქობულეთი", "ოზურგეთი", 34, 26400));
        cityConnectionList.add(new CityConnection("ფოთი", "ზუგდიდი", 65, 60000));
        cityConnectionList.add(new CityConnection("ფოთი", "სენაკი", 49, 42300));
        cityConnectionList.add(new CityConnection("ფოთი", "ლანჩხუთი", 43, 43900));
        cityConnectionList.add(new CityConnection("ფოთი", "ოზურგეთი", 53, 50900));
        cityConnectionList.add(new CityConnection("ზუგდიდი", "გალი", 18, 20900));
        cityConnectionList.add(new CityConnection("ზუგდიდი", "ხაიში", 61, 68800));
        cityConnectionList.add(new CityConnection("ზუგდიდი", "წალენჯიხა", 29, 25300));
        cityConnectionList.add(new CityConnection("ზუგდიდი", "ჩხოროწყუ", 32, 29100));
        cityConnectionList.add(new CityConnection("ზუგდიდი", "სენაკი", 43, 46700));
        cityConnectionList.add(new CityConnection("გალი", "სოხუმი", 75, 81200));
        cityConnectionList.add(new CityConnection("სოხუმი", "გაგრა", 84, 82500));
        cityConnectionList.add(new CityConnection("ხაიში", "მესტია", 53, 60000));
        cityConnectionList.add(new CityConnection("ხაიში", "წალენჯიხა", 56, 56700));
        cityConnectionList.add(new CityConnection("წალენჯიხა", "ჩხოროწყუ", 33, 17300));
        cityConnectionList.add(new CityConnection("ჩხოროწყუ", "სენაკი", 35, 35400));
        cityConnectionList.add(new CityConnection("ჩხოროწყუ", "მარტვილი", 42, 42400));
        cityConnectionList.add(new CityConnection("სენაკი", "მარტვილი", 36, 38500));
        cityConnectionList.add(new CityConnection("სენაკი", "ხონი", 36, 41300));
        cityConnectionList.add(new CityConnection("სენაკი", "სამტრედია", 25, 29200));
        cityConnectionList.add(new CityConnection("მარტვილი", "ხონი", 19, 18900));
        cityConnectionList.add(new CityConnection("მარტვილი", "სამტრედია", 43, 46800));
        cityConnectionList.add(new CityConnection("სამტრედია", "ხონი", 21, 20500));
        cityConnectionList.add(new CityConnection("სამტრედია", "ქუთაისი", 33, 34900));
        cityConnectionList.add(new CityConnection("სამტრედია", "საირმე", 87, 77500));
        cityConnectionList.add(new CityConnection("სამტრედია", "ჩოხატაური", 28, 28200));
        cityConnectionList.add(new CityConnection("სამტრედია", "ლანჩხუთი", 31, 32200));
        cityConnectionList.add(new CityConnection("ჩოხატაური", "ბახმარო", 58, 51200));
        cityConnectionList.add(new CityConnection("ჩოხატაური", "ოზურგეთი", 33, 29000));
        cityConnectionList.add(new CityConnection("ჩოხატაური", "ლანჩხუთი", 45, 36100));
        cityConnectionList.add(new CityConnection("საირმე", "ქუთაისი", 67, 50700));
        cityConnectionList.add(new CityConnection("საირმე", "ზესტაფონი", 88, 72000));
        cityConnectionList.add(new CityConnection("ქუთაისი", "ხონი", 34, 32200));
        cityConnectionList.add(new CityConnection("ქუთაისი", "ცაგერი", 67, 71900));
        cityConnectionList.add(new CityConnection("ქუთაისი", "ტყიბული", 49, 40500));
        cityConnectionList.add(new CityConnection("ქუთაისი", "ზესტაფონი", 36, 39900));
        cityConnectionList.add(new CityConnection("ცაგერი", "ლენტეხი", 21, 22100));
        cityConnectionList.add(new CityConnection("ცაგერი", "ამბროლაური", 59, 55200));
        cityConnectionList.add(new CityConnection("ამბროლაური", "ონი", 26, 29400));
        cityConnectionList.add(new CityConnection("ამბროლაური", "ტყიბული", 34, 39400));
        cityConnectionList.add(new CityConnection("ონი", "შოვი", 30, 29000));
        cityConnectionList.add(new CityConnection("ტყიბული", "ზესტაფონი", 45, 37100));
        cityConnectionList.add(new CityConnection("ზესტაფონი", "ჭიათურა", 37, 38100));
        cityConnectionList.add(new CityConnection("ზესტაფონი", "ხაშური", 61, 66300));
        cityConnectionList.add(new CityConnection("ზესტაფონი", "სამტრედია", 56, 66900));
        cityConnectionList.add(new CityConnection("ჭიათურა", "საჩხერე", 12, 13900));
        cityConnectionList.add(new CityConnection("საჩხერე", "გომი", 50, 55800));
        cityConnectionList.add(new CityConnection("ხაშური", "გომი", 14, 13200));
        cityConnectionList.add(new CityConnection("ხაშური", "ბორჯომი", 31, 30000));
        cityConnectionList.add(new CityConnection("ბორჯომი", "ბაკურიანი", 39, 30800));
        cityConnectionList.add(new CityConnection("ბორჯომი", "ახალციხე", 45, 48500));
        cityConnectionList.add(new CityConnection("ახალციხე", "აბასთუმანი", 25, 22100));
        cityConnectionList.add(new CityConnection("ახალციხე", "ზარზმა", 42, 35000));
        cityConnectionList.add(new CityConnection("ახალციხე", "ახალქალაქი", 59, 71400));
        cityConnectionList.add(new CityConnection("ზარზმა", "აბასთუმანი", 35, 23600));
        cityConnectionList.add(new CityConnection("ზარზმა", "ხულო", 110, 48500));
        cityConnectionList.add(new CityConnection("ხულო", "შუახევი", 25, 16800));
        cityConnectionList.add(new CityConnection("შუახევი", "ქედა", 47, 28600));
        cityConnectionList.add(new CityConnection("ქედა", "ბათუმი", 45, 38300));
        cityConnectionList.add(new CityConnection("გომი", "ცხინვალი", 41, 41700));
        cityConnectionList.add(new CityConnection("გომი", "თბილისი", 89, 117000));
        cityConnectionList.add(new CityConnection("გომი", "გორი", 32, 35500));
        cityConnectionList.add(new CityConnection("გორი", "ცხინვალი", 34, 35000));
        cityConnectionList.add(new CityConnection("გორი", "თბილისი", 71, 87200));
        cityConnectionList.add(new CityConnection("გორი", "მცხეთა", 66, 72400));
        cityConnectionList.add(new CityConnection("თბილისი", "ცხინვალი", 92, 115000));
        cityConnectionList.add(new CityConnection("თბილისი", "ანანური", 66, 67500));
        cityConnectionList.add(new CityConnection("თბილისი", "შატილი", 190, 149000));
        cityConnectionList.add(new CityConnection("თბილისი", "თიანეთი", 76, 80500));
        cityConnectionList.add(new CityConnection("თბილისი", "თელავი", 100, 95000));
        cityConnectionList.add(new CityConnection("თბილისი", "საგარეჯო", 51, 50500));
        cityConnectionList.add(new CityConnection("თბილისი", "რუსთავი", 39, 30000));
        cityConnectionList.add(new CityConnection("თბილისი", "მარნეული", 45, 42700));
        cityConnectionList.add(new CityConnection("თბილისი", "მანგლისი", 65, 54600));
        cityConnectionList.add(new CityConnection("თბილისი", "მცხეთა", 25, 21500));
        cityConnectionList.add(new CityConnection("ანანური", "გუდაური", 56, 54600));
        cityConnectionList.add(new CityConnection("გუდაური", "სტეფანწმინდა", 31, 32300));
        cityConnectionList.add(new CityConnection("ანანური", "შატილი", 148, 105000));
        cityConnectionList.add(new CityConnection("ანანური", "თიანეთი", 34, 36500));
        cityConnectionList.add(new CityConnection("შატილი", "თიანეთი", 151, 112000));
        cityConnectionList.add(new CityConnection("თიანეთი", "ახმეტა", 37, 26500));
        cityConnectionList.add(new CityConnection("ახმეტა", "ომალო", 125, 91200));
        cityConnectionList.add(new CityConnection("ახმეტა", "თელავი", 31, 28000));
        cityConnectionList.add(new CityConnection("თელავი", "ყვარელი", 34, 39000));
        cityConnectionList.add(new CityConnection("თელავი", "გურჯაანი", 37, 36700));
        cityConnectionList.add(new CityConnection("თელავი", "საგარეჯო", 84, 79000));
        cityConnectionList.add(new CityConnection("თელავი", "რუსთავი", 97, 93800));
        cityConnectionList.add(new CityConnection("ყვარელი", "ლაგოდეხი", 44, 45400));
        cityConnectionList.add(new CityConnection("ყვარელი", "გურჯაანი", 28, 28600));
        cityConnectionList.add(new CityConnection("ლაგოდეხი", "დედოფლისწყარო", 55, 66600));
        cityConnectionList.add(new CityConnection("ლაგოდეხი", "სიღნაღი", 42, 46900));
        cityConnectionList.add(new CityConnection("ლაგოდეხი", "საგარეჯო", 90, 107000));
        cityConnectionList.add(new CityConnection("ლაგოდეხი", "გურჯაანი", 51, 47700));
        cityConnectionList.add(new CityConnection("დედოფლისწყარო", "გურჯაანი", 52, 52600));
        cityConnectionList.add(new CityConnection("დედოფლისწყარო", "სიღნაღი", 33, 34700));
        cityConnectionList.add(new CityConnection("დედოფლისწყარო", "საგარეჯო", 72, 85000));
        cityConnectionList.add(new CityConnection("სიღნაღი", "გურჯაანი", 27, 21000));
        cityConnectionList.add(new CityConnection("სიღნაღი", "საგარეჯო", 48, 60500));
        cityConnectionList.add(new CityConnection("საგარეჯო", "გურჯაანი", 52, 63700));
        cityConnectionList.add(new CityConnection("საგარეჯო", "რუსთავი", 48, 49300));
        cityConnectionList.add(new CityConnection("რუსთავი", "გარდაბანი", 14, 12400));
        cityConnectionList.add(new CityConnection("რუსთავი", "მარნეული", 58, 54300));
        cityConnectionList.add(new CityConnection("რუსთავი", "მანგლისი", 93, 90600));
        cityConnectionList.add(new CityConnection("მარნეული", "ბოლნისი", 25, 23200));
        cityConnectionList.add(new CityConnection("მარნეული", "წალკა", 77, 81500));
        cityConnectionList.add(new CityConnection("მარნეული", "მანგლისი", 61, 61000));
        cityConnectionList.add(new CityConnection("წალკა", "მანგლისი", 33, 37900));
        cityConnectionList.add(new CityConnection("წალკა", "ნინოწმინდა", 61, 73200));
        cityConnectionList.add(new CityConnection("ნინოწმინდა", "ახალქალაქი", 17, 18700));
        cityConnectionList.add(new CityConnection("ოზურგეთი", "ლანჩხუთი", 38, 33500));
        cityConnectionList.add(new CityConnection("ქობულეთი", "ბათუმი", 38, 29800));
        cityConnectionList.add(new CityConnection("ფოთი", "ურეკი", 23, 23000));
        cityConnectionList.add(new CityConnection("ოზურგეთი", "ურეკი", 31, 29000));
        cityConnectionList.add(new CityConnection("ლანჩხუთი", "ურეკი", 25, 26700));
        cityConnectionList.add(new CityConnection("ურეკი", "ქობულეთი", 20, 21400));
        cityConnectionList.add(new CityConnection("ოზურგეთი", "ქობულეთი", 34, 26400));
        cityConnectionList.add(new CityConnection("ზუგდიდი", "ფოთი", 65, 60000));
        cityConnectionList.add(new CityConnection("სენაკი", "ფოთი", 49, 42300));
        cityConnectionList.add(new CityConnection("ლანჩხუთი", "ფოთი", 43, 43900));
        cityConnectionList.add(new CityConnection("ოზურგეთი", "ფოთი", 53, 50900));
        cityConnectionList.add(new CityConnection("გალი", "ზუგდიდი", 18, 20900));
        cityConnectionList.add(new CityConnection("ხაიში", "ზუგდიდი", 61, 68800));
        cityConnectionList.add(new CityConnection("წალენჯიხა", "ზუგდიდი", 29, 25300));
        cityConnectionList.add(new CityConnection("ჩხოროწყუ", "ზუგდიდი", 32, 29100));
        cityConnectionList.add(new CityConnection("სენაკი", "ზუგდიდი", 43, 46700));
        cityConnectionList.add(new CityConnection("სოხუმი", "გალი", 75, 81200));
        cityConnectionList.add(new CityConnection("გაგრა", "სოხუმი", 84, 82500));
        cityConnectionList.add(new CityConnection("მესტია", "ხაიში", 53, 60000));
        cityConnectionList.add(new CityConnection("წალენჯიხა", "ხაიში", 56, 56700));
        cityConnectionList.add(new CityConnection("ჩხოროწყუ", "წალენჯიხა", 33, 17300));
        cityConnectionList.add(new CityConnection("სენაკი", "ჩხოროწყუ", 35, 35400));
        cityConnectionList.add(new CityConnection("მარტვილი", "ჩხოროწყუ", 42, 42400));
        cityConnectionList.add(new CityConnection("მარტვილი", "სენაკი", 36, 38500));
        cityConnectionList.add(new CityConnection("ხონი", "სენაკი", 36, 41300));
        cityConnectionList.add(new CityConnection("სამტრედია", "სენაკი", 25, 29200));
        cityConnectionList.add(new CityConnection("ხონი", "მარტვილი", 19, 18900));
        cityConnectionList.add(new CityConnection("სამტრედია", "მარტვილი", 43, 46800));
        cityConnectionList.add(new CityConnection("ხონი", "სამტრედია", 21, 20500));
        cityConnectionList.add(new CityConnection("ქუთაისი", "სამტრედია", 33, 34900));
        cityConnectionList.add(new CityConnection("საირმე", "სამტრედია", 87, 77500));
        cityConnectionList.add(new CityConnection("ჩოხატაური", "სამტრედია", 28, 28200));
        cityConnectionList.add(new CityConnection("ლანჩხუთი", "სამტრედია", 31, 32200));
        cityConnectionList.add(new CityConnection("ბახმარო", "ჩოხატაური", 58, 51200));
        cityConnectionList.add(new CityConnection("ოზურგეთი", "ჩოხატაური", 33, 29000));
        cityConnectionList.add(new CityConnection("ლანჩხუთი", "ჩოხატაური", 45, 36100));
        cityConnectionList.add(new CityConnection("ქუთაისი", "საირმე", 67, 50700));
        cityConnectionList.add(new CityConnection("ზესტაფონი", "საირმე", 88, 72000));
        cityConnectionList.add(new CityConnection("ხონი", "ქუთაისი", 34, 32200));
        cityConnectionList.add(new CityConnection("ცაგერი", "ქუთაისი", 67, 71900));
        cityConnectionList.add(new CityConnection("ტყიბული", "ქუთაისი", 49, 40500));
        cityConnectionList.add(new CityConnection("ზესტაფონი", "ქუთაისი", 36, 39900));
        cityConnectionList.add(new CityConnection("ლენტეხი", "ცაგერი", 21, 22100));
        cityConnectionList.add(new CityConnection("ამბროლაური", "ცაგერი", 59, 55200));
        cityConnectionList.add(new CityConnection("ონი", "ამბროლაური", 26, 29400));
        cityConnectionList.add(new CityConnection("ტყიბული", "ამბროლაური", 34, 39400));
        cityConnectionList.add(new CityConnection("შოვი", "ონი", 30, 29000));
        cityConnectionList.add(new CityConnection("ზესტაფონი", "ტყიბული", 45, 37100));
        cityConnectionList.add(new CityConnection("ჭიათურა", "ზესტაფონი", 37, 38100));
        cityConnectionList.add(new CityConnection("ხაშური", "ზესტაფონი", 61, 66300));
        cityConnectionList.add(new CityConnection("სამტრედია", "ზესტაფონი", 56, 66900));
        cityConnectionList.add(new CityConnection("საჩხერე", "ჭიათურა", 12, 13900));
        cityConnectionList.add(new CityConnection("გომი", "საჩხერე", 50, 55800));
        cityConnectionList.add(new CityConnection("გომი", "ხაშური", 14, 13200));
        cityConnectionList.add(new CityConnection("ბორჯომი", "ხაშური", 31, 30000));
        cityConnectionList.add(new CityConnection("ბაკურიანი", "ბორჯომი", 39, 30800));
        cityConnectionList.add(new CityConnection("ახალციხე", "ბორჯომი", 45, 48500));
        cityConnectionList.add(new CityConnection("აბასთუმანი", "ახალციხე", 25, 22100));
        cityConnectionList.add(new CityConnection("ზარზმა", "ახალციხე", 42, 35000));
        cityConnectionList.add(new CityConnection("ახალქალაქი", "ახალციხე", 59, 71400));
        cityConnectionList.add(new CityConnection("აბასთუმანი", "ზარზმა", 35, 23600));
        cityConnectionList.add(new CityConnection("ხულო", "ზარზმა", 110, 48500));
        cityConnectionList.add(new CityConnection("შუახევი", "ხულო", 25, 16800));
        cityConnectionList.add(new CityConnection("ქედა", "შუახევი", 47, 28600));
        cityConnectionList.add(new CityConnection("ბათუმი", "ქედა", 45, 38300));
        cityConnectionList.add(new CityConnection("ცხინვალი", "გომი", 41, 41700));
        cityConnectionList.add(new CityConnection("თბილისი", "გომი", 89, 117000));
        cityConnectionList.add(new CityConnection("გორი", "გომი", 32, 35500));
        cityConnectionList.add(new CityConnection("ცხინვალი", "გორი", 34, 35000));
        cityConnectionList.add(new CityConnection("თბილისი", "გორი", 71, 87200));
        cityConnectionList.add(new CityConnection("მცხეთა", "გორი", 66, 72400));
        cityConnectionList.add(new CityConnection("ცხინვალი", "თბილისი", 92, 115000));
        cityConnectionList.add(new CityConnection("ანანური", "თბილისი", 66, 67500));
        cityConnectionList.add(new CityConnection("შატილი", "თბილისი", 190, 149000));
        cityConnectionList.add(new CityConnection("თიანეთი", "თბილისი", 76, 80500));
        cityConnectionList.add(new CityConnection("თელავი", "თბილისი", 100, 95000));
        cityConnectionList.add(new CityConnection("საგარეჯო", "თბილისი", 51, 50500));
        cityConnectionList.add(new CityConnection("რუსთავი", "თბილისი", 39, 30000));
        cityConnectionList.add(new CityConnection("მარნეული", "თბილისი", 45, 42700));
        cityConnectionList.add(new CityConnection("მანგლისი", "თბილისი", 65, 54600));
        cityConnectionList.add(new CityConnection("მცხეთა", "თბილისი", 25, 21500));
        cityConnectionList.add(new CityConnection("გუდაური", "ანანური", 56, 54600));
        cityConnectionList.add(new CityConnection("სტეფანწმინდა", "გუდაური", 31, 32300));
        cityConnectionList.add(new CityConnection("შატილი", "ანანური", 148, 105000));
        cityConnectionList.add(new CityConnection("თიანეთი", "ანანური", 34, 36500));
        cityConnectionList.add(new CityConnection("თიანეთი", "შატილი", 151, 112000));
        cityConnectionList.add(new CityConnection("ახმეტა", "თიანეთი", 37, 26500));
        cityConnectionList.add(new CityConnection("ომალო", "ახმეტა", 125, 91200));
        cityConnectionList.add(new CityConnection("თელავი", "ახმეტა", 31, 28000));
        cityConnectionList.add(new CityConnection("ყვარელი", "თელავი", 34, 39000));
        cityConnectionList.add(new CityConnection("გურჯაანი", "თელავი", 37, 36700));
        cityConnectionList.add(new CityConnection("საგარეჯო", "თელავი", 84, 79000));
        cityConnectionList.add(new CityConnection("რუსთავი", "თელავი", 97, 93800));
        cityConnectionList.add(new CityConnection("ლაგოდეხი", "ყვარელი", 44, 45400));
        cityConnectionList.add(new CityConnection("გურჯაანი", "ყვარელი", 28, 28600));
        cityConnectionList.add(new CityConnection("დედოფლისწყარო", "ლაგოდეხი", 55, 66600));
        cityConnectionList.add(new CityConnection("სიღნაღი", "ლაგოდეხი", 42, 46900));
        cityConnectionList.add(new CityConnection("საგარეჯო", "ლაგოდეხი", 90, 107000));
        cityConnectionList.add(new CityConnection("გურჯაანი", "ლაგოდეხი", 51, 47700));
        cityConnectionList.add(new CityConnection("გურჯაანი", "დედოფლისწყარო", 52, 52600));
        cityConnectionList.add(new CityConnection("სიღნაღი", "დედოფლისწყარო", 33, 34700));
        cityConnectionList.add(new CityConnection("საგარეჯო", "დედოფლისწყარო", 72, 85000));
        cityConnectionList.add(new CityConnection("გურჯაანი", "სიღნაღი", 27, 21000));
        cityConnectionList.add(new CityConnection("საგარეჯო", "სიღნაღი", 48, 60500));
        cityConnectionList.add(new CityConnection("გურჯაანი", "საგარეჯო", 52, 63700));
        cityConnectionList.add(new CityConnection("რუსთავი", "საგარეჯო", 48, 49300));
        cityConnectionList.add(new CityConnection("გარდაბანი", "რუსთავი", 14, 12400));
        cityConnectionList.add(new CityConnection("მარნეული", "რუსთავი", 58, 54300));
        cityConnectionList.add(new CityConnection("მანგლისი", "რუსთავი", 93, 90600));
        cityConnectionList.add(new CityConnection("ბოლნისი", "მარნეული", 25, 23200));
        cityConnectionList.add(new CityConnection("წალკა", "მარნეული", 77, 81500));
        cityConnectionList.add(new CityConnection("მანგლისი", "მარნეული", 61, 61000));
        cityConnectionList.add(new CityConnection("მანგლისი", "წალკა", 33, 37900));
        cityConnectionList.add(new CityConnection("ნინოწმინდა", "წალკა", 61, 73200));
        cityConnectionList.add(new CityConnection("ახალქალაქი", "ნინოწმინდა", 17, 18700));
        cityConnectionList.add(new CityConnection("ლანჩხუთი", "ოზურგეთი", 38, 33500));

        ArrayList<String> citylistName = new ArrayList<>();
        for (int i = 0; i < Constantebi.cityList.size(); i++) {
            citylistName.add(Constantebi.cityList.get(i).getNameGE());
        }

        for (int i = 0; i < cityConnectionList.size(); i++) {
            cityConnectionList.get(i).setCityA(String.valueOf(Constantebi.cityList.get(citylistName.indexOf(cityConnectionList.get(i).getCityA())).getC_id()));
            cityConnectionList.get(i).setCityB(String.valueOf(Constantebi.cityList.get(citylistName.indexOf(cityConnectionList.get(i).getCityB())).getC_id()));
        }

        HashMap<String, ArrayList<CityObj>> cityMap = new HashMap<>();

        for (int i = 0; i < Constantebi.cityList.size(); i++) {
            cityMap.put(String.valueOf(Constantebi.cityList.get(i).getC_id()), new ArrayList<CityObj>());
        }

        for (int i = 0; i < cityConnectionList.size(); i++) {
            cityMap.get(cityConnectionList.get(i).getCityA())
                    .add(new CityObj(
                            cityConnectionList.get(i).getCityB()
                            , ""
                            , cityConnectionList.get(i).getDistance()
                            , cityConnectionList.get(i).getTime()));
        }

        ArrayList<String> visiBleCitiesNames = new ArrayList<>();
        ArrayList<String> visiTedCitiesNames = new ArrayList<>();

        HashMap<String, CityObj> visiBleCities = new HashMap<>();
        HashMap<String, CityObj> visiTedCities = new HashMap<>();

        CityObj startCity = new CityObj(start_City, "", 0, 0);
        CityObj finishCity = new CityObj(finish_City, "", 0, 0);

        CityObj currCity = startCity;
        visiBleCities.put(currCity.getName(), currCity);
        visiBleCitiesNames.add(currCity.getName());
        Boolean finishFounded = false;

        do {
            // xilvadi qalaqebidan varchevt ufro axlos romelia, rom gadavidet masze shemdegi iteraciistvis
            int minTime = 10000000;
            String nextCity = "";
            for (int i = 0; i < visiBleCitiesNames.size(); i++) {
                if (visiBleCities.get(visiBleCitiesNames.get(i)).getTime() < minTime) {
                    minTime = visiBleCities.get(visiBleCitiesNames.get(i)).getTime();
                    nextCity = visiBleCitiesNames.get(i);
                }
            }

            // gadavdivart uaxloes qalaqze
            currCity = visiBleCities.get(nextCity);
            visiBleCities.remove(nextCity);
            visiBleCitiesNames.remove(nextCity);

            visiTedCities.put(currCity.getName(), currCity);
            visiTedCitiesNames.add(currCity.getName());

            if (currCity.getName().equals(finishCity.getName())) {
                finishFounded = true;
            }

            for (int i = 0; i < cityMap.get(currCity.getName()).size(); i++) {
                // mimdinare qalaqistvis avigot yvela mezobeli qalaqi da vamatebt xilvadi qalaqebis siashi

                if (visiBleCities.containsKey(cityMap.get(currCity.getName()).get(i).getName())) {
                    // tu mimdinare qalaqis i-uri mezobeli ukve aris xilvadobis areshi
                    // mashin gadavamowmot mandzili am qalaqamde da tu uketesia shevcvalot

                    if (visiBleCities.get(cityMap.get(currCity.getName()).get(i).getName()).getTime() > currCity.getTime() + cityMap.get(currCity.getName()).get(i).getTime()) {
                        visiBleCities.get(cityMap.get(currCity.getName()).get(i).getName()).setTime(currCity.getTime() + cityMap.get(currCity.getName()).get(i).getTime());
                        visiBleCities.get(cityMap.get(currCity.getName()).get(i).getName()).setDistance(currCity.getDistance() + cityMap.get(currCity.getName()).get(i).getDistance());
                        visiBleCities.get(cityMap.get(currCity.getName()).get(i).getName()).setPrevCity(currCity.getName());
                        // time
                    }

                } else {
                    if (!visiTedCities.containsKey(cityMap.get(currCity.getName()).get(i).getName())) {
                        // tu mimdinare qalaqis i-uri mezobeli jer ar yofila chven xelshi mashin vamatebt mas
                        // xilvad qalaqebshi (tu ratqmaunda ukve napovni aragvaqvs umoklesi gza am qalaqamde NOT IN visiTed List)

                        visiBleCities.put(cityMap.get(currCity.getName()).get(i).getName(), cityMap.get(currCity.getName()).get(i));
                        visiBleCities.get(cityMap.get(currCity.getName()).get(i).getName()).setPrevCity(currCity.getName());
                        visiBleCities.get(cityMap.get(currCity.getName()).get(i).getName()).setDistance(currCity.getDistance() + cityMap.get(currCity.getName()).get(i).getDistance());
                        visiBleCities.get(cityMap.get(currCity.getName()).get(i).getName()).setTime(currCity.getTime() + cityMap.get(currCity.getName()).get(i).getTime());

                        visiBleCitiesNames.add(cityMap.get(currCity.getName()).get(i).getName());
                    }
                }
            }


        } while (visiBleCities.size() > 0 && !finishFounded);


        ArrayList<String> result = new ArrayList<>();
        String tempCityName = finishCity.getName();


        while (!visiTedCities.get(tempCityName).getPrevCity().equals(startCity.getName())) {
            result.add(visiTedCities.get(tempCityName).getPrevCity());
            tempCityName = visiTedCities.get(tempCityName).getPrevCity();
        }

        pathLength = visiTedCities.get(finishCity.getName()).getDistance() / 1000;
        pathTime = visiTedCities.get(finishCity.getName()).getTime();
        pathItemsID = ",";

        citylistName.clear();
        for (int i = 0; i < Constantebi.cityList.size(); i++) {
            citylistName.add(String.valueOf(Constantebi.cityList.get(i).getC_id()));
        }

        for (int i = 1; i <= result.size(); i++) {
            if (i > 1) {
                pathItems += " - ";
            }

            pathItems += Constantebi.cityList.get(citylistName.indexOf(result.get(result.size() - i))).getNameGE();
            pathItemsID += result.get(result.size() - i) + ",";
        }

    }
}
