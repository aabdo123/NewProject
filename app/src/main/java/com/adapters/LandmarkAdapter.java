package com.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.activities.AddReportsActivity;
import com.models.LandMarkerTypeModel;
import com.models.ListOfUsersModel;
import com.views.ClickOnList;
import com.views.TextViewLight;
import com.views.TextViewRegular;

import java.util.List;


/**
 * Created by Saferoad-Dev1 on 8/29/2017.
 */

public class LandmarkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private FragmentActivity activity;
    private List<LandMarkerTypeModel> arrayList;
    private ClickOnList clickOnList;
    private int lastPositionSelected = 0;

    public LandmarkAdapter(FragmentActivity activity, List<LandMarkerTypeModel> arrayList, ClickOnList clickOnList) {
        this.activity = activity;
        this.arrayList = arrayList;
        this.clickOnList = clickOnList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_landmark, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ItemViewHolder itemViewHolder = ((ItemViewHolder) holder);
        LandMarkerTypeModel model = arrayList.get(itemViewHolder.getAdapterPosition());

        itemViewHolder.landmarkIconImageView.setImageDrawable(model.getIcon());
        itemViewHolder.landmarkLabelTextView.setText(model.getName());

        if (lastPositionSelected == itemViewHolder.getAdapterPosition()) {
            itemViewHolder.landmarkLayout.setAlpha(1.0f);
        } else {
            itemViewHolder.landmarkLayout.setAlpha(0.3f);
        }

        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastPositionSelected = itemViewHolder.getAdapterPosition();
                clickOnList.onClick(lastPositionSelected);
                if (lastPositionSelected == itemViewHolder.getAdapterPosition()) {
                    lastPositionSelected = itemViewHolder.getAdapterPosition();
                    itemViewHolder.landmarkLayout.setAlpha(1.0f);
//                    model.setSelected(true);
                } else {
//                    model.setSelected(false);
                    itemViewHolder.landmarkLayout.setAlpha(0.3f);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout landmarkLayout;
        private ImageView landmarkIconImageView;
        private TextViewLight landmarkLabelTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            landmarkLayout = (LinearLayout) itemView.findViewById(R.id.landmarkLayout);
            landmarkIconImageView = (ImageView) itemView.findViewById(R.id.landmarkIconImageView);
            landmarkLabelTextView = (TextViewLight) itemView.findViewById(R.id.landmarkLabelTextView);
        }
    }
}