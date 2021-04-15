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
import com.activities.AddReportsSingleActivity;
import com.models.ReportsTypeModel;
import com.views.TextViewRegular;

import java.util.List;

public class ReportsIncludedSingleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //    private final static int VIEW_TEXT = 0;
    private final static int VIEW_ITEM = 1;
    private FragmentActivity activity;
    private List<ReportsTypeModel> arrayList;

    public ReportsIncludedSingleAdapter(FragmentActivity activity, List<ReportsTypeModel> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
    }

    @Override
    public int getItemViewType(int position) {
//        if (position == 0) {
//            return VIEW_TEXT;
//        } else {
        return VIEW_ITEM;
//        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if (viewType == VIEW_TEXT) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_reports_included_text, parent, false);
//            return new TextViewHolder(view);
//        }else {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_reports_included_item, parent, false);
        return new ReportsIncludedSingleAdapter.ItemViewHolder(view);
//        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
//        if (holder instanceof TextViewHolder) {
//            TextViewHolder textViewHolder = ((TextViewHolder) holder);
//            textViewHolder.desTextView.setText(R.string.select);
//
//        } else {
        ReportsIncludedSingleAdapter.ItemViewHolder itemViewHolder = ((ReportsIncludedSingleAdapter.ItemViewHolder) holder);
        ReportsTypeModel model = arrayList.get(itemViewHolder.getAdapterPosition());
        if (model.isSelected()) {
            itemViewHolder.arrowImageView.setVisibility(View.VISIBLE);
        } else {
            itemViewHolder.arrowImageView.setVisibility(View.INVISIBLE);
        }
        itemViewHolder.reportsTypeTextView.setText(model.getName());
        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    for (ReportsTypeModel reportsTypeModel : arrayList) {
                        reportsTypeModel.setSelected(false);
                    }
                    AddReportsSingleActivity.wizardModel.getReportsType().clear();
                    itemViewHolder.arrowImageView.setVisibility(View.INVISIBLE);
                    notifyDataSetChanged();
                    if (!model.isSelected()) {
                        model.setSelected(true);
                        AddReportsSingleActivity.wizardModel.addReportsType(model);
                        itemViewHolder.arrowImageView.setVisibility(View.VISIBLE);
                    } else {
                        model.setSelected(false);
                        AddReportsSingleActivity.wizardModel.removeReportsType(model);
                        itemViewHolder.arrowImageView.setVisibility(View.INVISIBLE);
                    }
                }catch (Exception ex){
                    ex.getMessage();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class TextViewHolder extends RecyclerView.ViewHolder {

        private TextViewRegular desTextView;

        public TextViewHolder(View itemView) {
            super(itemView);
            desTextView = (TextViewRegular) itemView.findViewById(R.id.desTextView);
        }
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
