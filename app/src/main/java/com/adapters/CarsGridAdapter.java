package com.adapters;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.R;
import com.activities.MainActivity;
import com.managers.map_managers.MyStartMapViewManager;
import com.utilities.constants.AppConstant;
import com.views.TextViewRegular;

import java.util.List;

/**
 * Created by Saferoad-Dev1 on 8/29/2017.
 */

public class CarsGridAdapter extends BaseAdapter {

    private List<String> itemsList;
    private Activity activity;

    public CarsGridAdapter(Activity activity, List<String> itemsList) {
        this.itemsList = itemsList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return itemsList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.adapter_cars_grid, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        onBindViewHolder(holder, position);
        return convertView;
    }

    public class ViewHolder {
        private ImageView carAvailableImageView;
        private TextViewRegular carNumbersTextView;
        private TextViewRegular carTypeTextView;
        private CardView cardView;

        public ViewHolder(View itemView) {
            carAvailableImageView = (ImageView) itemView.findViewById(R.id.carAvailableImageView);
            carNumbersTextView = (TextViewRegular) itemView.findViewById(R.id.carNumbersTextView);
            carTypeTextView = (TextViewRegular) itemView.findViewById(R.id.carTypeTextView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
        }
    }


    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {
            String total = itemsList.get(position);
            switch (position) {
                case 0:
//                    holder.carAvailableImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.all));
                    holder.carAvailableImageView.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.all));
                    holder.carNumbersTextView.setText(total);
                    holder.carTypeTextView.setText(activity.getResources().getText(R.string.all));
                    holder.cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            ((MainActivity) activity).displayView(R.string.nav_list_of_vehicles, activity.getResources().getString(R.string.nav_list_of_vehicles), AppConstant.ALL_CARS);
                            new MyStartMapViewManager(activity);
                        }
                    });
                    break;

                case 1:
                    holder.carAvailableImageView.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.offline));
                    holder.carNumbersTextView.setTextColor(ContextCompat.getColor(activity, R.color.car_yellow));
                    holder.carNumbersTextView.setText(total);
                    holder.carTypeTextView.setText(activity.getResources().getText(R.string.offline));
                    holder.cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((MainActivity) activity).displayView(R.string.nav_list_of_vehicles, activity.getResources().getString(R.string.nav_list_of_vehicles), AppConstant.OFFLINE_CARS);
                        }
                    });
                    break;

                case 2:
                    holder.carAvailableImageView.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.online));
                    holder.carNumbersTextView.setTextColor(ContextCompat.getColor(activity, R.color.car_green));
                    holder.carNumbersTextView.setText(total);
                    holder.carTypeTextView.setText(activity.getResources().getText(R.string.online));
                    holder.cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((MainActivity) activity).displayView(R.string.nav_list_of_vehicles, activity.getResources().getString(R.string.nav_list_of_vehicles), AppConstant.ONLINE_CARS);
                        }
                    });
                    break;

                case 3:
                    holder.carAvailableImageView.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.notification));
                    holder.carNumbersTextView.setTextColor(ContextCompat.getColor(activity, R.color.car_red));
                    holder.carNumbersTextView.setText(total);
                    holder.carTypeTextView.setText(activity.getResources().getText(R.string.alarm));
                    holder.cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((MainActivity) activity).displayView(R.string.alarm_notification, activity.getResources().getString(R.string.nav_alarm_notification), "");
                        }
                    });
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}