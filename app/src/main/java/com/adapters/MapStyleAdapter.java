package com.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.R;
import com.managers.PreferencesManager;
import com.models.MapStyleModel;
import com.utilities.constants.SharesPrefConstants;
import com.views.ClickOnList;
import com.views.TextViewRegular;

import java.util.List;


/**
 * Created by Saferoad-Dev1 on 8/29/2017.
 */

public class MapStyleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<MapStyleModel> arrayList;
    private ClickOnList clickOnList;
    private int lastPositionSelected = 0;

    public MapStyleAdapter(Context context, List<MapStyleModel> arrayList, ClickOnList clickOnList) {
        this.context = context;
        this.arrayList = arrayList;
        this.clickOnList = clickOnList;
        if (PreferencesManager.getInstance().getIntegerValue(SharesPrefConstants.MAP_STYLE_INDEX) > 0)
            lastPositionSelected = PreferencesManager.getInstance().getIntegerValue(SharesPrefConstants.MAP_STYLE_INDEX);
        else {
            lastPositionSelected = 0;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_map_style, parent, false);
        vh = new ItemViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = ((ItemViewHolder) holder);
        MapStyleModel model = arrayList.get(itemViewHolder.getAdapterPosition());

        itemViewHolder.styleImageView.setImageDrawable(model.getIcon());
        itemViewHolder.styleNameTextView.setText(model.getName());

        if (lastPositionSelected == itemViewHolder.getAdapterPosition()) {
            itemViewHolder.checkedImageView.setVisibility(View.VISIBLE);
        } else {
            itemViewHolder.checkedImageView.setVisibility(View.INVISIBLE);
        }

        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastPositionSelected = itemViewHolder.getAdapterPosition();
                clickOnList.onClick(lastPositionSelected);
                if (lastPositionSelected == itemViewHolder.getAdapterPosition()) {
                    lastPositionSelected = itemViewHolder.getAdapterPosition();
                    itemViewHolder.checkedImageView.setVisibility(View.VISIBLE);
                } else {
                    itemViewHolder.checkedImageView.setVisibility(View.INVISIBLE);
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

        private ImageView styleImageView;
        private ImageView checkedImageView;
        private TextViewRegular styleNameTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            styleImageView = (ImageView) itemView.findViewById(R.id.styleImageView);
            checkedImageView = (ImageView) itemView.findViewById(R.id.checkedImageView);
            styleNameTextView = (TextViewRegular) itemView.findViewById(R.id.styleNameTextView);
        }
    }
}