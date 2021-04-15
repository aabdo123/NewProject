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
import com.models.LandmarkModel;
import com.views.ButtonBold;
import com.views.Click;
import com.views.Progress;
import com.views.TextViewRegular;

import java.util.List;

public class LandmarkListAdapter extends RecyclerSwipeAdapter<LandmarkListAdapter.SimpleViewHolder> {

    private FragmentActivity activity;
    private List<LandmarkModel> arrayList;
    private Click onClick;

    public LandmarkListAdapter(FragmentActivity activity, List<LandmarkModel> arrayList, Click click) {
        this.activity = activity;
        this.arrayList = arrayList;
        this.onClick = click;
    }

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_landmark_list, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SimpleViewHolder viewHolder, int position) {
        final LandmarkModel model = arrayList.get(viewHolder.getAdapterPosition());
        viewHolder.landmarkNameTextView.setText(model.getPOIName());

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
        if (position == 0){
            viewHolder.dividerView.setVisibility(View.GONE);
        }
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        private TextViewRegular landmarkNameTextView;
        private SwipeLayout swipeLayout;
        private ButtonBold buttonDelete;
        private View dividerView;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            landmarkNameTextView = (TextViewRegular) itemView.findViewById(R.id.landmarkNameTextView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipeLayout);
            buttonDelete = (ButtonBold) itemView.findViewById(R.id.buttonDelete);
            dividerView = itemView.findViewById(R.id.dividerView);
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
        BusinessManager.postDeleteLandMark(s, new ApiCallResponseString() {
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
