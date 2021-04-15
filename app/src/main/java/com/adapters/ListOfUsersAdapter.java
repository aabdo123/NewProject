package com.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.activities.AddReportsActivity;
import com.models.ListOfUsersModel;
import com.views.TextViewRegular;

import java.util.List;


/**
 * Created by Saferoad-Dev1 on 8/29/2017.
 */

public class ListOfUsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private FragmentActivity activity;
    private List<ListOfUsersModel> arrayList;

    public ListOfUsersAdapter(FragmentActivity activity, List<ListOfUsersModel> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_of_users, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ItemViewHolder itemViewHolder = ((ItemViewHolder) holder);
        ListOfUsersModel model = arrayList.get(itemViewHolder.getAdapterPosition());

        if (model.isSelected()) {
            itemViewHolder.arrowImageView.setVisibility(View.VISIBLE);
        } else {
            itemViewHolder.arrowImageView.setVisibility(View.INVISIBLE);
        }
        itemViewHolder.reportsTypeTextView.setText(model.getName());
        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!model.isSelected()) {
                    model.setSelected(true);
                    AddReportsActivity.wizardModel.addUsers(model);
                    itemViewHolder.arrowImageView.setVisibility(View.VISIBLE);
                } else {
                    model.setSelected(false);
                    AddReportsActivity.wizardModel.removeUsers(model);
                    itemViewHolder.arrowImageView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout selectedLayout;
        private TextViewRegular reportsTypeTextView;
        private ImageView arrowImageView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            selectedLayout = (RelativeLayout) itemView.findViewById(R.id.selectedLayout);
            reportsTypeTextView = (TextViewRegular) itemView.findViewById(R.id.reportsTypeTextView);
            arrowImageView = (ImageView) itemView.findViewById(R.id.arrowImageView);
        }
    }
}