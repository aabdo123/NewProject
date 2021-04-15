package com.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.R;

import java.util.ArrayList;



/**
 * Created by usamaomar on 8/15/17.
 */

public class RealtyCityAdapter extends RecyclerView.Adapter<RealtyCityAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> cityModelArrayList;
    private RealtyCityAdapter.Clicks clicks;

    public RealtyCityAdapter(Context context, ArrayList<String> cityModelArrayList, RealtyCityAdapter.Clicks clicks) {
        this.cityModelArrayList = cityModelArrayList;
        this.context = context;
        this.clicks = clicks;
    }

    @Override
    public RealtyCityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_string_realty, parent, false);
        return new RealtyCityAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RealtyCityAdapter.ViewHolder holder, final int position) {
        holder.titleTextView.setText(cityModelArrayList.get(holder.getAdapterPosition()));
        holder.titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicks.click(cityModelArrayList.get(holder.getAdapterPosition()), holder.getAdapterPosition());
            }
        });
    }

    //ApartmentsForRent
    //VillaForSale
    //LandForSale
    //ApartmentForSale
    //HouseForSale
    //HomeForRent


    public interface Clicks {
        void click(String cityModel , int position);
    }


    @Override
    public int getItemCount() {
        return cityModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;


        public ViewHolder(View view) {
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        }
    }
}

