package com.example.kdiakonidze.pickmeapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.kdiakonidze.pickmeapp.R;
import com.example.kdiakonidze.pickmeapp.adapters.PassangerListAdapterRc;
import com.example.kdiakonidze.pickmeapp.datebase.DBmanager;
import com.example.kdiakonidze.pickmeapp.models.PassangerStatement;
import com.example.kdiakonidze.pickmeapp.utils.Constantebi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
/**
 * Created by k.diakonidze on 7/10/2016.
 */
public class PassengerStatatementListFragment extends Fragment {

    private ArrayList<PassangerStatement> passangerStatements;
    private ListView passangerStatementList;
//    private PassangerListAdapter passangerListAdapter;
    private PassangerListAdapterRc passangerListAdapterRc;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swRefresh;
    private RecyclerView statementListView;
    private ProgressDialog progress;
    private JSONObject myobj;
    private String location = "";

    private int dataStartPoint = 0, dataPageSize = 10;
    private Boolean loading = false;
    private Boolean loadneeding = true;
    private RequestQueue queue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_b, container,false);

//        passangerStatementList = (ListView) v.findViewById(R.id.statement_2_list);
        statementListView = (RecyclerView) v.findViewById(R.id.recyclerList2);
        swRefresh = (SwipeRefreshLayout) v.findViewById(R.id.passangerRefresh);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        switch (location) {
            case Constantebi.MY_OWN_STAT:
                DBmanager.initialaize(getActivity());
                DBmanager.openReadable();
                passangerStatements = DBmanager.getPassangerList(Constantebi.MY_STATEMENT);
                DBmanager.close();
                passangerListAdapterRc = new PassangerListAdapterRc(passangerStatements, getActivity(), location);
                statementListView.setAdapter(passangerListAdapterRc);
                break;
            case Constantebi.FAVORIT_STAT:
                DBmanager.initialaize(getActivity());
                DBmanager.openReadable();
                passangerStatements = DBmanager.getPassangerList(Constantebi.FAV_STATEMENT);
                DBmanager.close();
                passangerListAdapterRc = new PassangerListAdapterRc(passangerStatements, getActivity(), location);
                statementListView.setAdapter(passangerListAdapterRc);
                break;
            default:
                break;
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        passangerStatements = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        swRefresh.setColorSchemeColors(getResources().getColor(R.color.fab_color));
        // vin gamoiZaxa es forma
        location = getArguments().getString("location");

        final View v;

        // Listis gaketeba
        statementListView.setHasFixedSize(true);
        passangerListAdapterRc = new PassangerListAdapterRc(passangerStatements, getActivity(), location);
        statementListView.setAdapter(passangerListAdapterRc);
        statementListView.setLayoutManager(linearLayoutManager);

        switch (location) {
            case Constantebi.ALL_STAT:
                getPassengersStatements();
                v = getActivity().findViewById(R.id.main_content);
                break;
            case Constantebi.FILTERED_STAT:
                getPassengersStatements();
                v = getActivity().findViewById(R.id.main_content);
                break;
            default:
                v = statementListView;
        }

        swRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                switch (location) {
                    case Constantebi.ALL_STAT:
                        int oldsize = passangerStatements.size();
                        dataStartPoint = 0;
                        passangerStatements.clear();
                        passangerListAdapterRc.notifyItemRangeRemoved(0, oldsize);
                        break;
                    case Constantebi.FAVORIT_STAT:
                        getPassengersStatements();
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
                    int totalItemCount = passangerListAdapterRc.getItemCount();

                    if (totalItemCount - lastVisibleItemIndex > 2 || totalItemCount < 4) loadneeding = true;

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
                        getPassengersStatements();
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

    }

    private void getPassengersStatements() {

        String url = "";
        // romeli info wamovigo serveridan
        switch (location){
            case Constantebi.ALL_STAT:
                url = Constantebi.URL_GET_ST_LIST + "stType=2&start_row=" + dataStartPoint + "&page_size=" + dataPageSize;
                Toast.makeText(getActivity(), url, Toast.LENGTH_LONG).show();
                break;
            case Constantebi.FILTERED_STAT:
                url = Constantebi.URL_GET_FILTER + "stType=2" + Constantebi.FILTER_CRITERIUM;
                Toast.makeText(getActivity(), url, Toast.LENGTH_LONG).show();
                break;
            case Constantebi.FAVORIT_STAT:
//                url = "http://back.meet.ge/get.php?type=2";
                url = "http://back.meet.ge/get.php?type=FAV&sub_type=2&id=";
                for (int i = 0; i < Constantebi.FAV_STAT_PASSANGER.size(); i++) {
                    url += String.valueOf(Constantebi.FAV_STAT_PASSANGER.get(i));
                    if (i < Constantebi.FAV_STAT_PASSANGER.size() - 1) url += ",";
                }
                break;
        }

        queue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {

                        ArrayList<PassangerStatement> newData = new ArrayList<>();

                        if(jsonArray.length()>0){
                            for(int i=0; i<jsonArray.length(); i++){
                                try {

                                    PassangerStatement newPassangerStatement = new PassangerStatement(
                                            jsonArray.getJSONObject(i).getString("userID"),
                                            jsonArray.getJSONObject(i).getInt("freespace"),
                                            jsonArray.getJSONObject(i).getInt("price"),
                                            jsonArray.getJSONObject(i).getString("cityFrom"),
                                            jsonArray.getJSONObject(i).getString("cityTo"),
                                            jsonArray.getJSONObject(i).getString("date"));

                                    newPassangerStatement.setTime(jsonArray.getJSONObject(i).getString("time"));
                                    newPassangerStatement.setKondencioneri(jsonArray.getJSONObject(i).getInt("kondincioneri"));
                                    newPassangerStatement.setSigareti(jsonArray.getJSONObject(i).getInt("sigareti"));
                                    newPassangerStatement.setSabarguli(jsonArray.getJSONObject(i).getInt("sabarguli"));
                                    newPassangerStatement.setAtHome(jsonArray.getJSONObject(i).getInt("adgilzemisvla"));
                                    newPassangerStatement.setCxovelebi(jsonArray.getJSONObject(i).getInt("cxoveli"));
                                    newPassangerStatement.setPlaceX(jsonArray.getJSONObject(i).getLong("placex"));
                                    newPassangerStatement.setPlaceY(jsonArray.getJSONObject(i).getLong("placey"));
                                    newPassangerStatement.setComment(jsonArray.getJSONObject(i).getString("comment"));

                                    newPassangerStatement.setName(jsonArray.getJSONObject(i).getString("name"));
//                                    newPassangerStatement.setSurname(jsonArray.getJSONObject(i).getString("lastname"));
                                    newPassangerStatement.setNumber(jsonArray.getJSONObject(i).getString("mobile"));

                                    newPassangerStatement.setId(jsonArray.getJSONObject(i).getInt("id"));

                                    newData.add(newPassangerStatement);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        if (newData.size() > 0) {           // tu erti mowyobiloba mainc aris mashin vaxarisxebt lists
                            if (location.equals(Constantebi.ALL_STAT)) {

                                for (int i = 0; i < newData.size(); i++) {
                                    passangerStatements.add(newData.get(i));
                                }
                                dataStartPoint += dataPageSize;
                                if (dataStartPoint > passangerStatements.size())
                                    dataStartPoint = passangerStatements.size();

                                passangerListAdapterRc.notifyItemRangeInserted(passangerStatements.size() - newData.size(), newData.size());
                            }
                            if (location.equals(Constantebi.FAVORIT_STAT)) {
                                passangerStatements = newData;
                                passangerListAdapterRc = new PassangerListAdapterRc(passangerStatements, getActivity(), location);
                                statementListView.setAdapter(passangerListAdapterRc);

                                statementListView.setLayoutManager(linearLayoutManager);
                            }
                            if (location.equals(Constantebi.FILTERED_STAT)) {
                                passangerStatements = newData;
                                passangerListAdapterRc = new PassangerListAdapterRc(passangerStatements, getActivity(), location);
                                statementListView.setAdapter(passangerListAdapterRc);

                                statementListView.setLayoutManager(linearLayoutManager);
                            }
                        }
                        loading = false;
                        swRefresh.setRefreshing(false);
//                        progress.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
//                          Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
                        loading = false;
                        swRefresh.setRefreshing(false);
//                        progress.dismiss();
                    }
                }
        );

//        progress = ProgressDialog.show(getActivity(), "ჩამოტვირთვა2", "გთხოვთ დაიცადოთ");
        request.setTag("stList");
        queue.add(request);
    }

}
