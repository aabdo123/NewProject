package com.adapters;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.R;
import com.managers.ApiCallResponseString;
import com.managers.BusinessManager;
import com.models.GeoFenceModel;
import com.utilities.Utils;
import com.views.AlertDialogView;
import com.views.Click;
import com.views.TextViewLight;

import java.util.List;


/**
 * Created by Saferoad-Dev1 on 8/29/2017.
 */

public class GeoFenceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<GeoFenceModel> arrayList;

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private FragmentActivity activity;

    public GeoFenceAdapter(FragmentActivity activity, List<GeoFenceModel> arrayList, RecyclerView recyclerView) {
        this.arrayList = arrayList;
        this.activity = activity;

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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_geo_fence, parent, false);
            vh = new ItemViewHolder(view);
            return vh;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_progress_item, parent, false);
            vh = new ProgressViewHolder(view);
            return vh;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = ((ItemViewHolder) holder);
            GeoFenceModel model = arrayList.get(position);

            itemViewHolder.geoNameTextView.setText(model.getGeofenceName());
            itemViewHolder.deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Utils.isNotEmptyList(arrayList)) {
                        alertDeleteDialog(model, itemViewHolder.getAdapterPosition());
                    }
                }
            });
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

        private TextViewLight geoNameTextView;
        private ImageView deleteImageView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            geoNameTextView = (TextViewLight) itemView.findViewById(R.id.geoNameTextView);
            deleteImageView = (ImageView) itemView.findViewById(R.id.deleteImageView);
        }
    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

    private void alertDeleteDialog(GeoFenceModel model, int position) {
        AlertDialogView.yesNoButtonDialog(activity, activity, activity.getString(R.string.are_you_sure_you_want_to_delete), activity.getString(R.string.delete), new Click() {
            @Override
            public void onClick() {
                arrayList.remove(model);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, arrayList.size());
                deleteGeofenceApiCall(model.getID());
            }
        });
    }

    private void deleteGeofenceApiCall(int geoId) {
//        Progress.showLoadingDialog(activity);
        BusinessManager.postDeleteGeoFence(String.valueOf(geoId), new ApiCallResponseString() {
            @Override
            public void onSuccess(int statusCode, String responseObject) {
//                Progress.dismissLoadingDialog();
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, String errorResponse) {
//                Progress.dismissLoadingDialog();
                notifyDataSetChanged();
            }
        });

    }
}
