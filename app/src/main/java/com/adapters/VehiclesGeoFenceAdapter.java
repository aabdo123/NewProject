package com.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.R;
import com.models.ListOfVehiclesModel;
import com.utilities.AnimationUtils;
import com.utilities.AppUtils;
import com.views.TextViewRegular;

import java.util.ArrayList;

public class VehiclesGeoFenceAdapter  extends ExpandableRecyclerAdapter<ListOfVehiclesModel, ListOfVehiclesModel.VehicleModel, VehiclesGeoFenceAdapter.ViewHolderParent, VehiclesGeoFenceAdapter.ViewHolderChild> {

    private LayoutInflater mInflater;

    public VehiclesGeoFenceAdapter(Context context, ArrayList<ListOfVehiclesModel> recipeList) {
        super(recipeList);
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @UiThread
    @Override
    public ViewHolderParent onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View recipeView = mInflater.inflate(R.layout.adapter_vehicles_reporsts_parent, parentViewGroup, false);
        return new ViewHolderParent(recipeView);
    }

    @NonNull
    @UiThread
    @Override
    public ViewHolderChild onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View ingredientView = mInflater.inflate(R.layout.adapter_vehicles_geofence_child, childViewGroup, false);
        return new ViewHolderChild(ingredientView);
    }

    @UiThread
    @Override
    public void onBindParentViewHolder(@NonNull ViewHolderParent parentViewHolder, int parentPosition, @NonNull ListOfVehiclesModel parent) {
        parentViewHolder.headerNameTextView.setText(parent.getHeader());

    }

    @UiThread
    @Override
    public void onBindChildViewHolder(@NonNull ViewHolderChild holder, int parentPosition, int childPosition, @NonNull ListOfVehiclesModel.VehicleModel model) {
        holder.plateNumberTextView.setText(model.getLabel());
        holder.iconImageView.setImageResource(AppUtils.getCarIconDrawableID(model.getLastLocation().getVehicleStatus()));

        if (model.isSelected()) {
            holder.arrowImageView.setVisibility(View.VISIBLE);
        } else {
            holder.arrowImageView.setVisibility(View.INVISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!model.isSelected()) {
                    model.setSelected(true);
                    holder.arrowImageView.setVisibility(View.VISIBLE);
                } else {
                    model.setSelected(false);
                    holder.arrowImageView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public class ViewHolderChild extends ChildViewHolder {

        LinearLayout rowLinearLayout;
        TextViewRegular plateNumberTextView;
        ImageView statusCircleImageView;
        ImageView iconImageView;
        ImageView arrowImageView;

        private ViewHolderChild(@NonNull View itemView) {
            super(itemView);
            rowLinearLayout = (LinearLayout) itemView.findViewById(R.id.rowLinearLayout);
            plateNumberTextView = (TextViewRegular) itemView.findViewById(R.id.plateNumberTextView);
            statusCircleImageView = (ImageView) itemView.findViewById(R.id.statusCircleImageView);
            iconImageView = (ImageView) itemView.findViewById(R.id.iconImageView);
            arrowImageView = (ImageView) itemView.findViewById(R.id.arrowImageView);
        }
    }

    public class ViewHolderParent extends ParentViewHolder {

        private static final float INITIAL_POSITION = 0.0f;
        private static final float ROTATED_POSITION = 180f;

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
                mArrowExpandImageView.setRotation(ROTATED_POSITION);
            } else {
                mArrowExpandImageView.setRotation(INITIAL_POSITION);
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