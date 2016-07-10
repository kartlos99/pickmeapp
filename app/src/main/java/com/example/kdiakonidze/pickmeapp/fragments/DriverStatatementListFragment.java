package com.example.kdiakonidze.pickmeapp.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.kdiakonidze.pickmeapp.R;
import com.example.kdiakonidze.pickmeapp.adapters.DriverListAdapter;
import com.example.kdiakonidze.pickmeapp.adapters.DriverListAdapterRc;
import com.example.kdiakonidze.pickmeapp.datebase.DBmanager;
import com.example.kdiakonidze.pickmeapp.models.DriverStatement;
import com.example.kdiakonidze.pickmeapp.utils.Constantebi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
/**
 * Created by k.diakonidze on 7/10/2016.
 */
public class DriverStatatementListFragment extends Fragment {

    private JSONObject myobj;
    private ProgressDialog progress;
    private ArrayList<DriverStatement> driverStatements;
    private DriverListAdapter driverListAdapter;
    //    private ListView driverStatementList;
    private String location = "";
    private SwipeRefreshLayout swRefresh;
    private RecyclerView statementListView;
    private DriverListAdapterRc driverListAdapterRc;
    private LinearLayoutManager linearLayoutManager;

    private RequestQueue queue;
    private JsonArrayRequest request;

    private int dataStartPoint = 0, dataPageSize = 10;
    private Boolean loading = false;
    private Boolean loadneeding = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_a, container, false);

        statementListView = (RecyclerView) v.findViewById(R.id.recyclerList1);
        swRefresh = (SwipeRefreshLayout) v.findViewById(R.id.driverRefresh);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        switch (location) {
            case Constantebi.MY_OWN_STAT:
                DBmanager.initialaize(getActivity());
                DBmanager.openReadable();
                driverStatements = DBmanager.getDriverList(Constantebi.MY_STATEMENT);
                DBmanager.close();
                driverListAdapterRc = new DriverListAdapterRc(driverStatements, getActivity(), location);
                statementListView.setAdapter(driverListAdapterRc);
                break;
            case Constantebi.FAVORIT_STAT:
                DBmanager.initialaize(getActivity());
                DBmanager.openReadable();
                driverStatements = DBmanager.getDriverList(Constantebi.FAV_STATEMENT);
                DBmanager.close();
                driverListAdapterRc = new DriverListAdapterRc(driverStatements, getActivity(), location);
                statementListView.setAdapter(driverListAdapterRc);
                break;
            case Constantebi.FILTERED_STAT:

                driverListAdapterRc = new DriverListAdapterRc(driverStatements, getActivity(), location);
                statementListView.setAdapter(driverListAdapterRc);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        driverStatements = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        swRefresh.setColorSchemeColors(getResources().getColor(R.color.fab_color));
        // vin gamoiZaxa es forma
        location = getArguments().getString("location");

        final View v;

        // Listis gaketeba
        statementListView.setHasFixedSize(true);
        driverListAdapterRc = new DriverListAdapterRc(driverStatements, getActivity(), location);
        statementListView.setAdapter(driverListAdapterRc);
        statementListView.setLayoutManager(linearLayoutManager);

        switch (location) {
            case Constantebi.ALL_STAT:
                getDriversStatements();
                v = getActivity().findViewById(R.id.main_content);
                break;
            case Constantebi.FILTERED_STAT:
                getDriversStatements();
                v = getActivity().findViewById(R.id.main_content);
                break;
            default:
                v = statementListView;
        }

        // snakbarma fab Button -i rom ar dafaros


        swRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                switch (location) {
                    case Constantebi.ALL_STAT:
                        int oldsize = driverStatements.size();
                        dataStartPoint = 0;
                        driverStatements.clear();
                        driverListAdapterRc.notifyItemRangeRemoved(0, oldsize);
                        break;
                    case Constantebi.FAVORIT_STAT:
                        getDriversStatements();
                        break;
                    case Constantebi.MY_OWN_STAT:
                        swRefresh.setRefreshing(false);
                }
            }
        });

        // აქ ვარკვევთ ლისტის ბოლოში ვართ თუარა და ინფო წამოვიგოტ ტუ არა
        statementListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (location.equals(Constantebi.ALL_STAT)) {

                    int lastVisibleItemIndex = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = driverListAdapterRc.getItemCount();

                    if (totalItemCount - lastVisibleItemIndex > 2 || totalItemCount < 4)
                        loadneeding = true;

                    if (lastVisibleItemIndex >= totalItemCount - 1 && !loading && loadneeding) {
                        loading = true;
                        loadneeding = false;
                        // marto refreshis dros rom amoagdos shetyobineba
                        if (dataStartPoint == 0) {
                            Snackbar.make(v, "Loading...", Snackbar.LENGTH_LONG)
                                    .setActionTextColor(Color.CYAN)
                                    .setAction("STOP", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            queue.cancelAll("stList");
                                        }
                                    })
                                    .show();
                        }
                        swRefresh.setRefreshing(true);
                        getDriversStatements();
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });


    }

    //8******************************************************************
    public void getDriversStatements() {

        String url = "";
        // romeli info wamovigo serveridan
        switch (location) {
            case Constantebi.ALL_STAT:
                url = Constantebi.URL_GET_ST_LIST + "stType=1&start_row=" + dataStartPoint + "&page_size=" + dataPageSize;
                break;
            case Constantebi.FILTERED_STAT:
                if (Constantebi.FILTER_DR) {
                    url = Constantebi.URL_GET_FILTER + "stType=1" + Constantebi.FILTER_CRITERIUM;
                    Toast.makeText(getActivity(), url, Toast.LENGTH_LONG).show();
                }
                break;
            case Constantebi.FAVORIT_STAT:
                // http://back.meet.ge/get.php?type=FAV&sub_type=1&id=158,161
                if (Constantebi.FAV_STAT_DRIVER.size() > 0) {
                    url = "http://back.meet.ge/get.php?type=FAV&sub_type=1&id=";
                    for (int i = 0; i < Constantebi.FAV_STAT_DRIVER.size(); i++) {
                        url += String.valueOf(Constantebi.FAV_STAT_DRIVER.get(i));
                        if (i < Constantebi.FAV_STAT_DRIVER.size() - 1) url += ",";
                    }
                } else {
                    url = "http://back.meet.ge/get.php?type=FAV&sub_type=1&id=1";
                }
                break;
        }


        queue = Volley.newRequestQueue(getActivity());

        DefaultRetryPolicy myPolicy = new DefaultRetryPolicy(5000, 3, 1.0f);

        request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {

                        ArrayList<DriverStatement> newData = new ArrayList<>();

                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {

                                    DriverStatement newDriverStatement = new DriverStatement(
                                            jsonArray.getJSONObject(i).getString("userID"),
                                            jsonArray.getJSONObject(i).getInt("freespace"),
                                            jsonArray.getJSONObject(i).getInt("price"),
                                            jsonArray.getJSONObject(i).getString("date"),
                                            jsonArray.getJSONObject(i).getString("cityFrom"),
                                            jsonArray.getJSONObject(i).getString("cityTo"));

                                    newDriverStatement.setCityPath(jsonArray.getJSONObject(i).getString("cityPath"));
                                    newDriverStatement.setTime(jsonArray.getJSONObject(i).getString("time"));
                                    newDriverStatement.setMarka(jsonArray.getJSONObject(i).getInt("cartype")); //int

//                                    newDriverStatement.setColor(jsonArray.getJSONObject(i).getInt("color"));
                                    newDriverStatement.setCarpicture(jsonArray.getJSONObject(i).getString("image"));
                                    newDriverStatement.setKondencioneri(jsonArray.getJSONObject(i).getInt("kondincioneri"));
                                    newDriverStatement.setSigareti(jsonArray.getJSONObject(i).getInt("sigareti"));
                                    newDriverStatement.setSabarguli(jsonArray.getJSONObject(i).getInt("sabarguli"));
                                    newDriverStatement.setAtHome(jsonArray.getJSONObject(i).getInt("adgilzemisvla"));
                                    newDriverStatement.setCxovelebi(jsonArray.getJSONObject(i).getInt("cxoveli"));
                                    newDriverStatement.setPlaceX(jsonArray.getJSONObject(i).getLong("placex"));
                                    newDriverStatement.setPlaceY(jsonArray.getJSONObject(i).getLong("placey"));
                                    newDriverStatement.setAgeTo(jsonArray.getJSONObject(i).getInt("ageTo"));
                                    newDriverStatement.setGender(jsonArray.getJSONObject(i).getInt("gender"));
                                    newDriverStatement.setComment(jsonArray.getJSONObject(i).getString("comment"));

                                    newDriverStatement.setName(jsonArray.getJSONObject(i).getString("name"));
//                                    newDriverStatement.setSurname(jsonArray.getJSONObject(i).getString("lastname"));
                                    newDriverStatement.setNumber(jsonArray.getJSONObject(i).getString("mobile"));

                                    newDriverStatement.setId(jsonArray.getJSONObject(i).getInt("id"));
                                    newDriverStatement.setCarpicture(jsonArray.getJSONObject(i).getString("image"));

                                    newData.add(newDriverStatement);


                                } catch (JSONException e) {
                                    e.printStackTrace();


                                }
                            }
                        }

                        if (newData.size() > 0) {
                            if (location.equals(Constantebi.ALL_STAT)) {

//                                driverListAdapterRc.insertItems(newData);

                                for (int i = 0; i < newData.size(); i++) {
                                    driverStatements.add(newData.get(i));
                                }
                                dataStartPoint += dataPageSize;
                                if (dataStartPoint > driverStatements.size())
                                    dataStartPoint = driverStatements.size();

                                driverListAdapterRc.notifyItemRangeInserted(driverStatements.size() - newData.size(), newData.size());
                            }

                            if (location.equals(Constantebi.FAVORIT_STAT)) {
                                driverStatements = newData;
                                driverListAdapterRc = new DriverListAdapterRc(driverStatements, getActivity(), location);
                                statementListView.setAdapter(driverListAdapterRc);

                                statementListView.setLayoutManager(linearLayoutManager);
                            }

                            if (location.equals(Constantebi.FILTERED_STAT)) {
                                driverStatements = newData;
                                driverListAdapterRc = new DriverListAdapterRc(driverStatements, getActivity(), location);
                                statementListView.setAdapter(driverListAdapterRc);

                                statementListView.setLayoutManager(linearLayoutManager);
                            }

//                            driverListAdapterRc = new DriverListAdapterRc(driverStatements, getActivity(), location);
//                            linearLayoutManager = new LinearLayoutManager(getActivity());

//                            statementListView.setAdapter(driverListAdapterRc);
//                            statementListView.setLayoutManager(linearLayoutManager);

//                            driverStatements = newData;
//                            driverListAdapter = new DriverListAdapter(getActivity(), driverStatements);
//                            driverStatementList.setAdapter(driverListAdapter);
                        }
                        loading = false;
                        swRefresh.setRefreshing(false);
//                        progress.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
                        loading = false;
                        swRefresh.setRefreshing(false);
//                        progress.dismiss();
                    }
                }
        );

        request.setRetryPolicy(myPolicy);
        request.setTag("stList");
        if (!swRefresh.isRefreshing()) {
//            progress = ProgressDialog.show(getActivity(), "ჩამოტვირთვა1", "გთხოვთ დაიცადოთ");
        }
        queue.add(request);
    }
}
