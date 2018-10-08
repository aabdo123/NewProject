package com.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.R;
import com.managers.PreferencesManager;
import com.models.SlideUpItemsModel;
import com.utilities.constants.SharesPrefConstants;
import com.views.ButtonBold;
import com.views.ClickOnList;
import com.views.ClickStatus;
import com.views.TextViewRegular;

import java.util.List;


/**
 * Created by Saferoad-Dev1 on 8/29/2017.
 */

public class SlideUpAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SlideUpItemsModel> arrayList;
    private FragmentActivity activity;
    private ClickStatus showClickStatus;
    private ClickOnList addClickStatus;

    public SlideUpAdapter(FragmentActivity activity, List<SlideUpItemsModel> arrayList, ClickStatus showClickStatus, ClickOnList addClickStatus) {
        this.arrayList = arrayList;
        this.activity = activity;
        this.showClickStatus = showClickStatus;
        this.addClickStatus = addClickStatus;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_slide_up, parent, false);
        vh = new ItemViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = ((ItemViewHolder) holder);
        SlideUpItemsModel model = arrayList.get(holder.getAdapterPosition());

        itemViewHolder.iconImageView.setImageDrawable(model.getDrawableIcon());
        itemViewHolder.titleTextView.setText(model.getTitle());
        itemViewHolder.desTextView.setText(model.getDes());
        itemViewHolder.backgroundLayout.setBackgroundColor(model.getColor());
        itemViewHolder.showButton.setBackground(model.getDrawableButton());
        itemViewHolder.addButton.setBackground(model.getDrawableButton());

        if (model.isShowButtonVisible()) {
            itemViewHolder.showButton.setVisibility(View.VISIBLE);
        }

        if (model.isAddButtonVisible()) {
            itemViewHolder.addButton.setVisibility(View.VISIBLE);
        }

//        if (PreferencesManager.getInstance().getBooleanValue(SharesPrefConstants.IS_GEOFENCE_SHOW_SLIDE_MENU))
            if (model.isShowClicked()) {
                itemViewHolder.showButton.setText(R.string.hide);
            } else {
                itemViewHolder.showButton.setText(R.string.show);
            }

//        if (PreferencesManager.getInstance().getBooleanValue(SharesPrefConstants.IS_LANDMARK_SHOW_SLIDE_MENU))
//            if (model.isShowClicked()) {
//                itemViewHolder.showButton.setText(R.string.hide);
//            } else {
//                itemViewHolder.showButton.setText(R.string.show);
//            }


        itemViewHolder.showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.isShowClicked()) {
                    model.setShowClicked(false);
                    showClickStatus.onClick(false, holder.getAdapterPosition());
                    if (model.getId() == 1) // cluster
                        PreferencesManager.getInstance().setBooleanValue(false, SharesPrefConstants.IS_CLUSTER_SHOW_SLIDE_MENU);
                    if (model.getId() == 3) // landmark
                        PreferencesManager.getInstance().setBooleanValue(false, SharesPrefConstants.IS_LANDMARK_SHOW_SLIDE_MENU);
                    if (model.getId() == 4) // geo-fence
                        PreferencesManager.getInstance().setBooleanValue(false, SharesPrefConstants.IS_GEOFENCE_SHOW_SLIDE_MENU);
                } else {
                    model.setShowClicked(true);
                    showClickStatus.onClick(true, holder.getAdapterPosition());
                    if (model.getId() == 1) // cluster
                        PreferencesManager.getInstance().setBooleanValue(true, SharesPrefConstants.IS_CLUSTER_SHOW_SLIDE_MENU);
                    if (model.getId() == 3) // landmark
                        PreferencesManager.getInstance().setBooleanValue(true, SharesPrefConstants.IS_LANDMARK_SHOW_SLIDE_MENU);
                    if (model.getId() == 4) // geo-fence
                        PreferencesManager.getInstance().setBooleanValue(true, SharesPrefConstants.IS_GEOFENCE_SHOW_SLIDE_MENU);
                }
                notifyDataSetChanged();
            }
        });
        itemViewHolder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClickStatus.onClick(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout backgroundLayout;
        private ImageView iconImageView;
        private TextViewRegular titleTextView;
        private TextViewRegular desTextView;
        private ButtonBold showButton;
        private ButtonBold addButton;

        public ItemViewHolder(View itemView) {
            super(itemView);
            backgroundLayout = (LinearLayout) itemView.findViewById(R.id.backgroundLayout);
            iconImageView = (ImageView) itemView.findViewById(R.id.iconImageView);
            titleTextView = (TextViewRegular) itemView.findViewById(R.id.titleTextView);
            desTextView = (TextViewRegular) itemView.findViewById(R.id.desTextView);
            showButton = (ButtonBold) itemView.findViewById(R.id.showButton);
            addButton = (ButtonBold) itemView.findViewById(R.id.addButton);
        }
    }
}
