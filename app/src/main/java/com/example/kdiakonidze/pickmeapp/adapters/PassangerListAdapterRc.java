package com.example.kdiakonidze.pickmeapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kdiakonidze.pickmeapp.DetailPagePassanger;
import com.example.kdiakonidze.pickmeapp.R;
import com.example.kdiakonidze.pickmeapp.models.PassangerStatement;
import com.example.kdiakonidze.pickmeapp.utils.Constantebi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
/**
 * Created by k.diakonidze on 7/10/2016.
 */
public class PassangerListAdapterRc extends RecyclerView.Adapter<PassangerListAdapterRc.PassangerViewHolder> {
    private ArrayList<PassangerStatement> data;
    private LayoutInflater inflater;
    private Context context;
    String location="";

    public PassangerListAdapterRc(ArrayList<PassangerStatement> data, Context context, String location) {
        this.data = data;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.location = location;
    }

    @Override
    public PassangerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.passenger_statement_layout, parent, false);
        PassangerViewHolder viewHolder = new PassangerViewHolder(view);
        return viewHolder;
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

    @Override
    public void onBindViewHolder(PassangerViewHolder holder, int position) {
        PassangerStatement currentStatement = data.get(position);

        holder.city.setText(cityAtID(currentStatement.getCityFrom()) + " - " + cityAtID(currentStatement.getCityTo()));
        holder.date.setText(currentStatement.getDate());
        holder.name.setText(currentStatement.getName() + " " + currentStatement.getSurname());
        holder.cost.setText(String.valueOf(currentStatement.getPrice()));

        holder.kacunebiConteiner.removeAllViews();
        ImageView[] imageView = new ImageView[currentStatement.getFreeSpace()];
        // kacunebi ra zomis amochdnen, sxvanairad arvici rogor miutito zoma
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(50, ViewGroup.LayoutParams.MATCH_PARENT); //(Constantebi.MAN_WIDTH, Constantebi.MAN_HIEGHT);
        for (int i = 0; i < currentStatement.getFreeSpace(); i++) {
            imageView[i] = new ImageView(context);
            imageView[i].setImageResource(R.drawable.man_full);
            imageView[i].setLayoutParams(lp);
            imageView[i].setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            holder.kacunebiConteiner.addView(imageView[i]);
        }

        Picasso.with(context)
                .load("https://graph.facebook.com/" + currentStatement.getUserID() + "/picture?type=large")
                .into(holder.imig);
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }





    class PassangerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView city, date, name, cost;
        ImageView imig;
        LinearLayout kacunebiConteiner;

        public PassangerViewHolder(View itemToShow) {
            super(itemToShow);
            imig = (ImageView) itemToShow.findViewById(R.id.list2_image);
            city = (TextView) itemToShow.findViewById(R.id.list2_city_text);
            date = (TextView) itemToShow.findViewById(R.id.list2_date);
            name = (TextView) itemToShow.findViewById(R.id.list2_name_text);
            cost = (TextView) itemToShow.findViewById(R.id.list2_price);
            kacunebiConteiner = (LinearLayout) itemToShow.findViewById(R.id.list2_kacunebi);

            itemToShow.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, DetailPagePassanger.class);
            intent.putExtra("driver_st", data.get(getAdapterPosition()));
            intent.putExtra("from", location);
            context.startActivity(intent);
        }
    }
}
