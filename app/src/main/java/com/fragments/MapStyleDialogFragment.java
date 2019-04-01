package com.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.R;
import com.adapters.MapStyleAdapter;
import com.managers.PreferencesManager;
import com.managers.ShortTermManager;
import com.models.MapStyleModel;
import com.utilities.constants.SharesPrefConstants;
import com.views.ButtonBold;
import com.views.ClickOnList;

import java.util.ArrayList;
import java.util.List;

public class MapStyleDialogFragment extends DialogFragment {

    private Context context;
    private RecyclerView stylingRecyclerView;
    private GridLayoutManager layoutManager;
    private ButtonBold selectButton;
    private ButtonBold cancelButton;
    private OnSelectMapStyle onSelectMapStyle;

    private List<MapStyleModel> styleList;
    private int selectedListItem = PreferencesManager.getInstance().getIntegerValue(SharesPrefConstants.MAP_STYLE_INDEX);
    private MapStyleAdapter mapStyleAdapter;

    public interface OnSelectMapStyle {
        void onSelectMapStyle(int selectedItemId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        // check if target Fragment implements listener
        try {
            if (getTargetFragment() instanceof OnSelectMapStyle) {
                onSelectMapStyle = (OnSelectMapStyle) getTargetFragment();
            } else {
                onSelectMapStyle = (OnSelectMapStyle) context;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        onSelectMapStyle = null;
    }


    public static MapStyleDialogFragment newInstance() {
        return new MapStyleDialogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_styling_dialog, container, false);
        initView(rootView);
        initListeners();
        initAdapter();
        return rootView;
    }

    private void initView(View rootView) {
        stylingRecyclerView = (RecyclerView) rootView.findViewById(R.id.stylingRecyclerView);
        selectButton = (ButtonBold) rootView.findViewById(R.id.selectButton);
        cancelButton = (ButtonBold) rootView.findViewById(R.id.cancelButton);
        layoutManager = new GridLayoutManager(context, 2);
        stylingRecyclerView.setLayoutManager(layoutManager);
    }

    private void initListeners() {
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ShortTermManager.getInstance().setMapsStyle(selectedListItem);
                PreferencesManager.getInstance().setIntegerValue(selectedListItem, SharesPrefConstants.MAP_STYLE_INDEX);
                onSelectMapStyle.onSelectMapStyle(styleList.get(selectedListItem).getId());
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                //ShortTermManager.getInstance().setMapsStyle(0);
            }
        });
    }

    private void initAdapter() {
        mapStyleAdapter = new MapStyleAdapter(context, getStylesList(), new ClickOnList() {
            @Override
            public void onClick(int position) {
                selectedListItem = position;
            }
        });
        stylingRecyclerView.setAdapter(mapStyleAdapter);
    }

    private List<MapStyleModel> getStylesList() {
        styleList = new ArrayList<>();
        styleList.add(new MapStyleModel(R.string.standard, context.getString(R.string.standard), context.getDrawable(R.drawable.map_style_standard)));
        styleList.add(new MapStyleModel(R.string.aubergine, context.getString(R.string.aubergine), context.getDrawable(R.drawable.map_style_aubergine)));
        styleList.add(new MapStyleModel(R.string.silver, context.getString(R.string.silver), context.getDrawable(R.drawable.map_style_silver)));
        styleList.add(new MapStyleModel(R.string.retro, context.getString(R.string.retro), context.getDrawable(R.drawable.map_style_retro)));
        styleList.add(new MapStyleModel(R.string.night, context.getString(R.string.night), context.getDrawable(R.drawable.map_style_night)));
        styleList.add(new MapStyleModel(R.string.dark, context.getString(R.string.dark), context.getDrawable(R.drawable.map_style_dark)));
        return styleList;
    }
}