package com.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.R;
import com.managers.ApiCallResponseString;
import com.managers.BusinessManager;
import com.models.ScheduledReportsModel;
import com.views.ButtonBold;
import com.views.Click;
import com.views.Progress;
import com.views.TextViewRegular;

import java.util.List;

public class ScheduledReportsAdapter extends RecyclerSwipeAdapter<ScheduledReportsAdapter.SimpleViewHolder> {

    private FragmentActivity activity;
    private List<ScheduledReportsModel> arrayList;
    private Click onClick;

    public ScheduledReportsAdapter(FragmentActivity activity, List<ScheduledReportsModel> arrayList, Click click) {
        this.activity = activity;
        this.arrayList = arrayList;
        this.onClick = click;
    }

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_scheduled_reports, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SimpleViewHolder viewHolder, int position) {
        final ScheduledReportsModel model = arrayList.get(viewHolder.getAdapterPosition());
        viewHolder.reportsNameTextView.setText(model.getName());

        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        viewHolder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });

        viewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMessageApiCall(String.valueOf(model.getID()), viewHolder.getAdapterPosition(), viewHolder.swipeLayout);
            }
        });
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        private TextViewRegular reportsNameTextView;
        private SwipeLayout swipeLayout;
        private ButtonBold buttonDelete;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            reportsNameTextView = (TextViewRegular) itemView.findViewById(R.id.reportsNameTextView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipeLayout);
            buttonDelete = (ButtonBold) itemView.findViewById(R.id.buttonDelete);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipeLayout;
    }

    private void deleteMessageApiCall(String s, int position, SwipeLayout swipeLayout) {
        Progress.showLoadingDialog(activity);
        BusinessManager.postDeleteScheduledReports(s, new ApiCallResponseString() {
            @Override
            public void onSuccess(int statusCode, String responseObject) {
                Progress.dismissLoadingDialog();
                mItemManger.removeShownLayouts(swipeLayout);
                arrayList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, arrayList.size());
                mItemManger.closeAllItems();
                if (arrayList.size() == 0)
                    onClick.onClick();
            }

            @Override
            public void onFailure(int statusCode, String errorResponse) {
                Progress.dismissLoadingDialog();
            }
        });
    }

}
