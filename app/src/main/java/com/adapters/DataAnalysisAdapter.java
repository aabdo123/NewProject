package com.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.models.DataAnalysisModel;
import com.utilities.Utils;
import com.views.TextViewLight;

import java.util.List;


/**
 * Created by Saferoad-Dev1 on 8/29/2017.
 */

public class DataAnalysisAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DataAnalysisModel> arrayList;

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    public DataAnalysisAdapter(List<DataAnalysisModel> arrayList, RecyclerView recyclerView) {
        this.arrayList = arrayList;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    @Override
    public int getItemViewType(int position) {
        return arrayList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_data_analysis, parent, false);
            vh = new ItemViewHolder(view);
            return vh;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_progress_item, parent, false);
            vh = new ProgressViewHolder(view);
            return vh;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = ((ItemViewHolder) holder);
            DataAnalysisModel model = arrayList.get(position);
            String[] recordDateTime =  Utils.getDateUtcToSameFormat(model.getRecordDateTime()).split("T");
            String date = recordDateTime[0].trim();
            String time = recordDateTime[1].trim();

            itemViewHolder.dateTextView.setText(date);
            itemViewHolder.timeTextView.setText(time);
            itemViewHolder.addressTextView.setText(model.getAddress());

//            if (model.getIsOnline()) {
//                itemViewHolder.carStateTextView.setText(context.getResources().getString(R.string.online));
//                Glide.with(context).load(R.drawable.timeline_off).into(itemViewHolder.timeLineImageView);
//                itemViewHolder.dateImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_calendar_green));
//                itemViewHolder.timeImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_clock_green));
//                itemViewHolder.timeLineView.setBackgroundColor(context.getResources().getColor(R.color.car_green));
//            } else {
                itemViewHolder.carStateTextView.setText(model.getVehicleStatus());
//            }
        } else if (holder instanceof ProgressViewHolder) {
            ProgressViewHolder progressViewHolder = ((ProgressViewHolder) holder);
            progressViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextViewLight dateTextView;
        private TextViewLight timeTextView;
        private TextViewLight addressTextView;
        private TextViewLight carStateTextView;
        private ImageView timeLineImageView;
        private ImageView dateImageView;
        private ImageView timeImageView;
        private View timeLineView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            dateTextView = (TextViewLight) itemView.findViewById(R.id.dateTextView);
            timeTextView = (TextViewLight) itemView.findViewById(R.id.timeTextView);
            addressTextView = (TextViewLight) itemView.findViewById(R.id.addressTextView);
            carStateTextView = (TextViewLight) itemView.findViewById(R.id.carStateTextView);
            timeLineImageView = (ImageView) itemView.findViewById(R.id.timeLineImageView);
            dateImageView = (ImageView) itemView.findViewById(R.id.dateImageView);
            timeImageView = (ImageView) itemView.findViewById(R.id.timeImageView);
            timeLineView = itemView.findViewById(R.id.timeLineView);
        }
    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
}
