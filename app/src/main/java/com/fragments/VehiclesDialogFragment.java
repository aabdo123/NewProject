package com.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.R;
import com.adapters.VehiclesGeoFenceAdapter;
import com.models.ListOfVehiclesModel;
import com.utilities.AnimationUtils;
import com.utilities.LogHelper;
import com.utilities.Utils;
import com.utilities.constants.AppConstant;
import com.views.ButtonBold;

import java.util.ArrayList;
import java.util.List;

public class VehiclesDialogFragment extends DialogFragment {

    private Context context;
    private RecyclerView vehiclesRecyclerView;
    private ArrayList<ListOfVehiclesModel> listOfVehiclesForGeoFence = new ArrayList<>();
    ;
    private ButtonBold doneTextView;
    private ImageView cancelImageView;
    private OnFinishSelectVehicles onFinishSelectVehicles;
    private VehiclesGeoFenceAdapter adapter;
    private List<String> vehiclesLabels;
    private List<String> vehiclesIds;

    public interface OnFinishSelectVehicles {
        void onSelectedVehiclesDone(String vehiclesLabels, String vehiclesIds);

        void onSelectedVehiclesCanceled();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        // check if target Fragment implements listener
        try {
            if (getTargetFragment() instanceof OnFinishSelectVehicles) {
                onFinishSelectVehicles = (OnFinishSelectVehicles) getTargetFragment();
            } else {
                onFinishSelectVehicles = (OnFinishSelectVehicles) context;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        onFinishSelectVehicles = null;
    }


    public static VehiclesDialogFragment newInstance(ArrayList<ListOfVehiclesModel> listOfVehiclesForGeoFence) {
        VehiclesDialogFragment fragment = new VehiclesDialogFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(AppConstant.VEHICLES_LIST_FOR_GEO_FENCE_ARGS, listOfVehiclesForGeoFence);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mBundle = this.getArguments();
        if (mBundle != null) {
            LogHelper.LOG_D("VEHICLES_LIST_GEO_FENCE", "getArgs");
            listOfVehiclesForGeoFence = mBundle.getParcelableArrayList(AppConstant.VEHICLES_LIST_FOR_GEO_FENCE_ARGS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vehicle_dialog, container, false);
        initView(rootView);
        initListeners();
        initAdapter();
        return rootView;
    }

    private void initView(View rootView) {
        vehiclesRecyclerView = (RecyclerView) rootView.findViewById(R.id.vehiclesRecyclerView);
        doneTextView = (ButtonBold) rootView.findViewById(R.id.doneTextView);
        cancelImageView = (ImageView) rootView.findViewById(R.id.cancelImageView);
    }

    private void initListeners() {
        doneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinishSelectVehicles.onSelectedVehiclesDone(getItemsComma(getVehiclesIdsList()), getItemsCommaSpace(getVehiclesLabelsList()));
                dismiss();
            }
        });

        cancelImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinishSelectVehicles.onSelectedVehiclesCanceled();
                dismiss();
            }
        });
    }

    private void initAdapter() {
        if (Utils.isNotEmptyList(listOfVehiclesForGeoFence)) {
            if (listOfVehiclesForGeoFence != null && listOfVehiclesForGeoFence.size() > 0 && listOfVehiclesForGeoFence.get(0).getVehicleModel() != null)
            if (Utils.isNotEmptyList(listOfVehiclesForGeoFence.get(0).getVehicleModel())) {
                adapter = new VehiclesGeoFenceAdapter(context, listOfVehiclesForGeoFence);
                vehiclesRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                vehiclesRecyclerView.setAdapter(adapter);
                adapter.expandParent(listOfVehiclesForGeoFence.get(0));
                AnimationUtils.loadListAnimation(vehiclesRecyclerView);
            }
        }
    }

    private List<String> getVehiclesIdsList() {
        vehiclesIds = new ArrayList<>();
        if (listOfVehiclesForGeoFence != null && listOfVehiclesForGeoFence.size() > 0 && listOfVehiclesForGeoFence.get(0).getVehicleModel() != null)
            for (ListOfVehiclesModel.VehicleModel ids : listOfVehiclesForGeoFence.get(0).getVehicleModel()) {
                if (ids.isSelected())
                    vehiclesIds.add(String.valueOf(ids.getVehicleID()));
            }
        return vehiclesIds;
    }

    private List<String> getVehiclesLabelsList() {
        vehiclesLabels = new ArrayList<>();
        if (listOfVehiclesForGeoFence != null && listOfVehiclesForGeoFence.size() > 0 && listOfVehiclesForGeoFence.get(0).getVehicleModel() != null)
        for (ListOfVehiclesModel.VehicleModel ids : listOfVehiclesForGeoFence.get(0).getVehicleModel()) {
            if (ids.isSelected())
                vehiclesLabels.add(ids.getLabel());
        }
        return vehiclesLabels;
    }

    private String getItemsComma(List<?> list) {
        if (Utils.isNotEmptyList(list)) {
            return android.text.TextUtils.join(",", list);
        } else {
            return "";
        }
    }

    private String getItemsCommaSpace(List<?> list) {
        if (Utils.isNotEmptyList(list)) {
            return android.text.TextUtils.join(" ,", list);
        } else {
            return "";
        }
    }
}