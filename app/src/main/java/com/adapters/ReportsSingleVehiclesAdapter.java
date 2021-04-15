package com.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.activities.AddReportsActivity;
import com.models.Item;
import com.models.ListOfUsersModel;
import com.utilities.AppUtils;
import com.views.TextViewRegular;

import java.util.List;

public class ReportsSingleVehiclesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private FragmentActivity activity;
    private List<Item> arrayList;
    private SelectedItems selectedItems;

    public ReportsSingleVehiclesAdapter(FragmentActivity activity, List<Item> itemLists, SelectedItems selectedItems) {
        this.activity = activity;
        this.arrayList = itemLists;
        this.selectedItems = selectedItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_expandable, parent, false);
        return new ReportsSingleVehiclesAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        try {
            ReportsSingleVehiclesAdapter.ItemViewHolder itemViewHolder = ((ReportsSingleVehiclesAdapter.ItemViewHolder) holder);
            Item model = arrayList.get(itemViewHolder.getAdapterPosition());
            if (model != null) {
                if (model.getVehicleStatus() != null) {
                    itemViewHolder.carImageView.setBackground(AppUtils.getCarIconDrawable(activity, model.getVehicleStatus()));
                }
                if (model.getName() != null)
                    itemViewHolder.title.setText(model.getName());
                itemViewHolder.mainItemViewLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedItems.selectedMainItem(model, itemViewHolder.getAdapterPosition());
                    }
                });
                itemViewHolder.checkBoxCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedItems.selectedMainItem(model, itemViewHolder.getAdapterPosition());
                    }
                });
                itemViewHolder.checkBoxCheckBox.setChecked(model.isChecked());


            }
        } catch (Exception ex) {
            ex.getMessage();
        }
    }


    public interface SelectedItems {
        void selectedMainItem(Item model, int position);
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView carImageView;
        private TextView title;
        private CheckBox checkBoxCheckBox;
        private LinearLayout mainItemViewLinearLayout;

        public ItemViewHolder(View itemView) {
            super(itemView);
            carImageView = (ImageView) itemView.findViewById(R.id.carImageView);
            title = (TextView) itemView.findViewById(R.id.title);
            checkBoxCheckBox = (CheckBox) itemView.findViewById(R.id.checkBoxCheckBox);
            mainItemViewLinearLayout = (LinearLayout) itemView.findViewById(R.id.mainItemViewLinearLayout);
        }
    }
}