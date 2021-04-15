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

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.R;
import com.activities.AddReportsActivity;
import com.models.ListOfVehiclesSmallModel;
import com.views.TextViewRegular;

import java.util.ArrayList;

/**
 * Created by Saferoad-Dev1 on 8/27/2017.
 */

public class ReportsVehiclesAdapter extends ExpandableRecyclerAdapter<ListOfVehiclesSmallModel, ListOfVehiclesSmallModel.Vehicles, ReportsVehiclesAdapter.ViewHolderParent, ReportsVehiclesAdapter.ViewHolderChild> {

    private LayoutInflater mInflater;
    private Activity activity;
    private Context context;

    public ReportsVehiclesAdapter(Activity activity, ArrayList<ListOfVehiclesSmallModel> recipeList) {
        super(recipeList);
        this.activity = activity;
        this.context = activity;
        mInflater = LayoutInflater.from(context);
    }

    @UiThread
    @Override
    public ViewHolderParent onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View recipeView = mInflater.inflate(R.layout.adapter_vehicles_reporsts_parent, parentViewGroup, false);
        return new ViewHolderParent(recipeView);
    }

    @UiThread
    @Override
    public ViewHolderChild onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View ingredientView = mInflater.inflate(R.layout.adapter_vehicles_reports_child, childViewGroup, false);
        return new ViewHolderChild(ingredientView);
    }

    @UiThread
    @Override
    public void onBindParentViewHolder(@NonNull ViewHolderParent holder, int parentPosition, @NonNull ListOfVehiclesSmallModel model) {
        holder.headerNameTextView.setText(model.getHeader());
    }

    @UiThread
    @Override
    public void onBindChildViewHolder(@NonNull ViewHolderChild holder, int parentPosition, int childPosition, @NonNull ListOfVehiclesSmallModel.Vehicles model) {
        holder.plateNumberTextView.setText(model.getName());

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
                    AddReportsActivity.wizardModel.addVehicles(model);
                    holder.arrowImageView.setVisibility(View.VISIBLE);
                } else {
                    model.setSelected(false);
                    AddReportsActivity.wizardModel.removeVehicles(model);
                    holder.arrowImageView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public class ViewHolderChild extends ChildViewHolder {

        LinearLayout rowLinearLayout;
        TextViewRegular plateNumberTextView;
        ImageView statusCircleImageView;
        ImageView arrowImageView;

        private ViewHolderChild(@NonNull View itemView) {
            super(itemView);
            rowLinearLayout = (LinearLayout) itemView.findViewById(R.id.rowLinearLayout);
            plateNumberTextView = (TextViewRegular) itemView.findViewById(R.id.plateNumberTextView);
            statusCircleImageView = (ImageView) itemView.findViewById(R.id.statusCircleImageView);
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
            RotateAnimation rotateAnimation;
            if (expanded) { // rotate clockwise
                rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            } else { // rotate counterclockwise
                rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            }

            rotateAnimation.setDuration(200);
            rotateAnimation.setFillAfter(true);
            mArrowExpandImageView.startAnimation(rotateAnimation);
        }

    }
}