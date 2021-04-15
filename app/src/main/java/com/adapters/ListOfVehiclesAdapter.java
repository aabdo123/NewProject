package com.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.core.content.ContextCompat;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.R;
import com.activities.MainActivity;
import com.fragments.MapOfVehicleFragment;
import com.managers.PreferencesManager;
import com.models.ListOfVehiclesModel;
import com.utilities.AnimationUtils;
import com.utilities.AppUtils;
import com.utilities.LogHelper;
import com.utilities.ToastHelper;
import com.utilities.Utils;
import com.utilities.constants.AppConstant;
import com.utilities.constants.SharesPrefConstants;
import com.views.TextViewRegular;
import com.views.TextViewLight;

import java.util.ArrayList;

/**
 * Created by Saferoad-Dev1 on 8/27/2017.
 */

public class ListOfVehiclesAdapter extends ExpandableRecyclerAdapter<ListOfVehiclesModel, ListOfVehiclesModel.VehicleModel, ListOfVehiclesAdapter.ViewHolderParent, ListOfVehiclesAdapter.ViewHolderChild> {

    private LayoutInflater mInflater;
    private Activity activity;
    private Context context;
    private onClickSearched clickSearched;

    public ListOfVehiclesAdapter(Activity activity, ArrayList<ListOfVehiclesModel> recipeList, onClickSearched clickSearched) {
        super(recipeList);
        try {
            this.activity = activity;
            this.context = activity;
            this.clickSearched = clickSearched;
            mInflater = LayoutInflater.from(context);
        } catch (Exception ex) {
            ex.getMessage();
        }
    }


    public interface onClickSearched {

        void clicked();

    }

    @UiThread
    @Override
    public ViewHolderParent onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View recipeView = mInflater.inflate(R.layout.adapter_list_of_vehicles_parent, parentViewGroup, false);
        return new ViewHolderParent(recipeView);
    }

    @UiThread
    @Override
    public ViewHolderChild onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View ingredientView = mInflater.inflate(R.layout.adapter_list_of_vehicles_child, childViewGroup, false);
        return new ViewHolderChild(ingredientView);
    }

    @UiThread
    @Override
    public void onBindParentViewHolder(@NonNull ViewHolderParent holder, int parentPosition, @NonNull ListOfVehiclesModel model) {
        holder.headerNameTextView.setText(model.getHeader());
    }

    @UiThread
    @Override
    public void onBindChildViewHolder(@NonNull ViewHolderChild holder, int parentPosition, int childPosition, final ListOfVehiclesModel.VehicleModel model) {
        if (model.getLastLocation() != null) {
            if (model.getLastLocation().getLatitude() != 0.0 || model.getLastLocation().getLongitude() != 0.0) {
                String[] recordDateTime = Utils.getDateUtcToSameFormat(model.getLastLocation().getRecordDateTime()).split("T");
//        String[] recordDateTime = model.getLastLocation().getRecordDateTime().split("T");
                String date = recordDateTime[0].trim();
                String time = recordDateTime[1].trim();

                holder.timeTextView.setVisibility(View.VISIBLE);
                holder.dateTextView.setVisibility(View.VISIBLE);

                holder.timeTextView.setText(time);
                holder.dateTextView.setText(date);
            } else {
                holder.timeTextView.setVisibility(View.INVISIBLE);
                holder.dateTextView.setVisibility(View.INVISIBLE);
            }
        } else {
            holder.divView.setVisibility(View.INVISIBLE);
        }

        holder.plateNumberTextView.setText(model.getLabel() != null ? model.getLabel() : "");

        if (model.getLastLocation() != null) {
            if (model.getLastLocation().getIsOnline() == true ) {
                holder.rowLinearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                holder.statusCircleImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_circle_green));
            } else {
                holder.rowLinearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.app_background));
                holder.statusCircleImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_circle_gray));
            }
        } else {
            holder.rowLinearLayout.setVisibility(View.INVISIBLE);
            holder.statusCircleImageView.setVisibility(View.INVISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppUtils.checkLocationPermissions(activity))
                    if (model.getLastLocation() != null) {
                        clickSearched.clicked();
                        ((MainActivity) activity).call(MapOfVehicleFragment.newInstance(model), context.getString(R.string.real_time_tracking));
                        PreferencesManager.getInstance().setIntegerValue(model.getVehicleID(), SharesPrefConstants.LAST_VIEW_VEHICLE_ID);
                        LogHelper.LOG_D("VehicleID", "VehicleID   <><><><><><><><> " + model.getVehicleID());
                        LogHelper.LOG_D("VehicleID", "PreferencesManager   <><><><><><><><> " + PreferencesManager.getInstance().getIntegerValue(SharesPrefConstants.LAST_VIEW_VEHICLE_ID));
                    } else {
                        ToastHelper.toastMessageLong(activity, context.getResources().getString(R.string.no_available_data));
                    }
            }
        });
    }

    public class ViewHolderChild extends ChildViewHolder {

        LinearLayout rowLinearLayout;
        ImageView statusCircleImageView;
        TextViewRegular plateNumberTextView;
        TextViewLight timeTextView;
        TextViewLight dateTextView;
        View divView;

        private ViewHolderChild(@NonNull View itemView) {
            super(itemView);
            rowLinearLayout = (LinearLayout) itemView.findViewById(R.id.rowLinearLayout);
            statusCircleImageView = (ImageView) itemView.findViewById(R.id.statusCircleImageView);
            plateNumberTextView = (TextViewRegular) itemView.findViewById(R.id.plateNumberTextView);
            timeTextView = (TextViewLight) itemView.findViewById(R.id.timeTextView);
            dateTextView = (TextViewLight) itemView.findViewById(R.id.dateTextView);
            divView = itemView.findViewById(R.id.divView);
        }
    }

    public class ViewHolderParent extends ParentViewHolder {

        private final ImageView mArrowExpandImageView;
        public TextViewRegular headerNameTextView;

        private ViewHolderParent(@NonNull View itemView) {
            super(itemView);
            headerNameTextView = (TextViewRegular) itemView.findViewById(R.id.headerNameTextView);
            mArrowExpandImageView = (ImageView) itemView.findViewById(R.id.mArrowExpandImageView);
        }

        @SuppressLint("NewApi")
        @Override
        public void setExpanded(boolean expanded) {
            super.setExpanded(expanded);
            if (expanded) {
                mArrowExpandImageView.setRotation(AppConstant.ROTATED_POSITION);
            } else {
                mArrowExpandImageView.setRotation(AppConstant.INITIAL_POSITION);
            }
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
        }

        @Override
        public void onExpansionToggled(boolean expanded) {
            super.onExpansionToggled(expanded);
            AnimationUtils.rotateAnimation(expanded, mArrowExpandImageView);
        }
    }
}
