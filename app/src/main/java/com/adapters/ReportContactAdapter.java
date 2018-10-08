package com.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.R;
import com.activities.AddReportsActivity;
import com.views.TextViewRegular;

import java.util.List;

public class ReportContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private FragmentActivity activity;
    private List<String> arrayList;
    private boolean isPhone;

    public ReportContactAdapter(FragmentActivity activity, List<String> arrayList, boolean isPhone) {
        this.activity = activity;
        this.arrayList = arrayList;
        this.isPhone = isPhone;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_report_contact, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = ((ItemViewHolder) holder);
        final String item = arrayList.get(itemViewHolder.getAdapterPosition());
        itemViewHolder.emailTextView.setText(item);
        itemViewHolder.deleteImageView.setOnClickListener(view -> deleteEmail(item, itemViewHolder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextViewRegular emailTextView;
        private ImageView deleteImageView;

        private ItemViewHolder(View itemView) {
            super(itemView);
            emailTextView = (TextViewRegular) itemView.findViewById(R.id.emailTextView);
            deleteImageView = (ImageView) itemView.findViewById(R.id.deleteImageView);
        }
    }

    private void deleteEmail(String item, int position) {
        arrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, arrayList.size());
        if (isPhone) {
            AddReportsActivity.wizardModel.removePhones(item);
        } else {
            AddReportsActivity.wizardModel.removeEmail(item);
        }
    }
}
