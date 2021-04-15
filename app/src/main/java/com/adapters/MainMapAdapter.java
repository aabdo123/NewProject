package com.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.managers.PreferencesManager;
import com.models.AllVehiclesInHashModel;
import com.models.MapStyleModel;
import com.utilities.constants.SharesPrefConstants;
import com.views.ClickOnList;
import com.views.TextViewLight;
import com.views.TextViewRegular;

import java.util.ArrayList;
import java.util.List;

public class MainMapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<AllVehiclesInHashModel.AllVehicleModel> vehiclesList;
    private ClickOnListTap clickOnList;
    private int lastPositionSelected = 0;

    public MainMapAdapter(Context context, ArrayList<AllVehiclesInHashModel.AllVehicleModel> vehiclesList, ClickOnListTap clickOnList) {
        this.context = context;
        this.vehiclesList = vehiclesList;
        this.clickOnList = clickOnList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_of_vehicles_child, parent, false);
        vh = new MainMapAdapter.ItemViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {
            MainMapAdapter.ItemViewHolder itemViewHolder = ((MainMapAdapter.ItemViewHolder) holder);
            AllVehiclesInHashModel.AllVehicleModel model = vehiclesList.get(itemViewHolder.getAdapterPosition());
            if (model!=null) {
                if (model.getLastLocation() != null && model.getLastLocation().getOnline() != null) {
                    if (model.getLastLocation().getOnline()) {
                        itemViewHolder.rowLinearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.app_background));
                        itemViewHolder.statusCircleImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_circle_green));
                    } else {
                        itemViewHolder.rowLinearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.app_background));
                        itemViewHolder.statusCircleImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_circle_gray));
                    }
                }
                itemViewHolder.plateNumberTextView.setText(model.getLabel() != null ? model.getLabel() : "");
                itemViewHolder.rowLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickOnList.onClick(itemViewHolder.getAdapterPosition(), model);
                    }
                });
                itemViewHolder.rowLinearLayout.setVisibility(View.VISIBLE);}
//            }else {
//                itemViewHolder.rowLinearLayout.setVisibility(View.GONE);
//            }
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    @Override
    public int getItemCount() {
        return vehiclesList.size();
    }

    public interface ClickOnListTap {
        void onClick(int position, AllVehiclesInHashModel.AllVehicleModel model);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        LinearLayout rowLinearLayout;
        ImageView statusCircleImageView;
        TextViewRegular plateNumberTextView;
        TextViewLight timeTextView;
        TextViewLight dateTextView;
        View divView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            rowLinearLayout = (LinearLayout) itemView.findViewById(R.id.rowLinearLayout);
            statusCircleImageView = (ImageView) itemView.findViewById(R.id.statusCircleImageView);
            plateNumberTextView = (TextViewRegular) itemView.findViewById(R.id.plateNumberTextView);
            timeTextView = (TextViewLight) itemView.findViewById(R.id.timeTextView);
            dateTextView = (TextViewLight) itemView.findViewById(R.id.dateTextView);
            divView = itemView.findViewById(R.id.divView);
        }
    }
}
