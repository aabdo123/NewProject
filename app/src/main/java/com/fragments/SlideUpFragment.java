package com.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.R;
import com.adapters.SlideUpAdapter;
import com.managers.PreferencesManager;
import com.models.SlideUpItemsModel;
import com.utilities.Utils;
import com.utilities.constants.SharesPrefConstants;
import com.views.ClickOnList;
import com.views.ClickStatus;

import java.util.ArrayList;
import java.util.List;

public class SlideUpFragment extends Fragment {

    private FragmentActivity activity;
    private RecyclerView sliderRecyclerView;
    private List<SlideUpItemsModel> list;
    private SlideUpAdapter slideUpAdapter;
    private OnChildFragmentInteractionListener mParentListener;

    public interface OnChildFragmentInteractionListener {
        void onClickShow(boolean isShowCliched, boolean isAddCliched, int itemId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // check if parent Fragment implements listener
        if (getParentFragment() instanceof OnChildFragmentInteractionListener) {
            mParentListener = (OnChildFragmentInteractionListener) getParentFragment();
        } else {
            throw new RuntimeException("The parent fragment must implement OnChildFragmentInteractionListener");
        }
    }


    public SlideUpFragment() {
        // Required empty public constructor
    }

    public static SlideUpFragment newInstance() {
        return new SlideUpFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slide_up, container, false);
        initViews(view);
        implementAdapter();
        return view;
    }


    private void initViews(View rootView) {
        sliderRecyclerView = (RecyclerView) rootView.findViewById(R.id.sliderRecyclerView);
        sliderRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
    }

    private void implementAdapter() {
        list = new ArrayList<>();
        list = getSlideUpList();
         slideUpAdapter = new SlideUpAdapter(activity, list, (isShowCliched, position) -> {
            SlideUpItemsModel model = list.get(position);
            mParentListener.onClickShow(isShowCliched, false, model.getId());
        }, position -> {
            SlideUpItemsModel model = list.get(position);
            if (position == 0) {
                list.get(1).setShowClicked(false);// cluster
//                    list.get(3).setShowClicked(false);// landmark
//                    list.get(4).setShowClicked(false);// geo-fence
            }
            mParentListener.onClickShow(false, true, model.getId());
        });
        sliderRecyclerView.setAdapter(slideUpAdapter);
        slideUpAdapter.notifyDataSetChanged();
    }

    public void notifyAdapterItemOneCluster() {
      try {
          if (slideUpAdapter!=null){
              PreferencesManager.getInstance().setBooleanValue(false, SharesPrefConstants.IS_CLUSTER_SHOW_SLIDE_MENU);
              SlideUpItemsModel model = list.get(1);
              model.setShowClicked(false);
              slideUpAdapter.notifyDataSetChanged();
          }
      }catch (Exception ex){
          ex.printStackTrace();
      }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mParentListener = null;
    }

    private List<SlideUpItemsModel> getSlideUpList() {
        List<SlideUpItemsModel> list = new ArrayList<>();
        list.add(new SlideUpItemsModel(0, true, false, Utils.colorInt("#F3A536"), getString(R.string.locate), getString(R.string.locate_des), getResources().getDrawable(R.drawable.ic_locate), getResources().getDrawable(R.drawable.button_shape_black_tanrs)));
        list.add(new SlideUpItemsModel(1, false, true, Utils.colorInt("#58E2C2"), getString(R.string.clustering), getString(R.string.dispaly_the_number_of_aggregated_vehicles), getResources().getDrawable(R.drawable.ic_clustering), getResources().getDrawable(R.drawable.button_shape_black_tanrs)));
        list.add(new SlideUpItemsModel(2, false, true, Utils.colorInt("#BB27DD"), getString(R.string.traffic), getString(R.string.traffic_des), getResources().getDrawable(R.drawable.ic_traffic_light), getResources().getDrawable(R.drawable.button_shape_gray_tanrs)));
        list.add(new SlideUpItemsModel(3, true, true, Utils.colorInt("#CD0B24"), getString(R.string.landmark), getString(R.string.recognizable_objects_for_places_on_the_map), getResources().getDrawable(R.drawable.ic_landmark), getResources().getDrawable(R.drawable.button_shape_gray_tanrs)));
        list.add(new SlideUpItemsModel(4, true, true, Utils.colorInt("#B9E78B"), getResources().getString(R.string.geo_fence), getResources().getString(R.string.geo_fence_des), getResources().getDrawable(R.drawable.ic_globe_location), getResources().getDrawable(R.drawable.button_shape_black_tanrs)));
        return list;
    }
}
