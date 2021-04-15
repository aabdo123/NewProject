package com.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.adapters.ExpandableAdapter;
import com.adapters.ReportsSingleVehiclesAdapter;
import com.google.gson.Gson;
import com.managers.ApiCallResponseString;
import com.managers.BusinessManager;
import com.managers.ShortTermManager;
import com.models.Item;
import com.multilevelview.MultiLevelRecyclerView;
import com.multilevelview.models.RecyclerViewItem;
import com.utilities.AppUtils;
import com.utilities.Utils;
import com.views.Progress;
import com.views.TextViewRegular;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.activities.AddReportsSingleActivity.wizardModel;

public class ReportsSingleVehiclesFragment extends Fragment {

    public static ReportsSingleVehiclesFragment reportsVehiclesFragment;
    private Activity activity;
    private List<Item> list;
    private List<Item> mainChecks;
    private List<Item> itemLists;
    private List<Item> mainItemsListValue;
    private MultiLevelRecyclerView multiLevelRecyclerView;
    private ExpandableAdapter expandableAdapter;
    private ArrayList<Item> itemArrayListCallas;
    private List<Item> mainArrayOfVehiclesList = new ArrayList<>();
    private RecyclerView vehiclesRecyclerView;
    private ReportsSingleVehiclesAdapter singleVehiclesAdapter;
    private SearchView searchView;
    private AutoCompleteTextView searchEditText;
    private List<Item> itemList;

    public static ReportsSingleVehiclesFragment newInstance() {
        return new ReportsSingleVehiclesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reports_single_vehicles, container, false);
        initView(rootView);
        gettingDataByBundle();
        expandable(activity.getString(R.string.all));
        Utils.hideKeyboardByView(activity, searchView);
        return rootView;
    }


    public void changeFilterType() {
        try {
            String value = wizardModel.getSelectedType();
            //if (value == null) {
            list = null;
            mainChecks = null;
            itemLists = null;
            if (expandableAdapter != null)
                expandableAdapter = null;
            searchView.clearFocus();
            vehiclesRecyclerView.setVisibility(View.GONE);
            multiLevelRecyclerView.setVisibility(View.VISIBLE);
            ShortTermManager.getInstance().clearRequestMapsExpendableList();
            expandable(getStatusType(value == null ? activity.getString(R.string.all) : value));
            //  }
        } catch (Exception ex) {
            ex.getMessage();
        }
    }


    private String getStatusType(String value) {
        String state = null;
        if (value.equalsIgnoreCase(activity.getString(R.string.all))) {
            state = activity.getString(R.string.all);
        } else if (value.equalsIgnoreCase(activity.getString(R.string.offline))) {
            state = "5";
        } else if (value.equalsIgnoreCase(activity.getString(R.string.running))) {
            state = "1";
        } else if (value.equalsIgnoreCase(activity.getString(R.string.stopped))) {
            state = "0";
        } else if (value.equalsIgnoreCase(activity.getString(R.string.idle))) {
            state = "2";
        } else if (value.equalsIgnoreCase(activity.getString(R.string.over_speed))) {
            state = "101";
        } else if (value.equalsIgnoreCase(activity.getString(R.string.over_street_speed))) {
            state = "100";
        }
        return state;
    }


    private void initView(View rootView) {
        searchView = (SearchView) rootView.findViewById(R.id.cityNameSearchView);
        multiLevelRecyclerView = (MultiLevelRecyclerView) rootView.findViewById(R.id.rv_list);
        multiLevelRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        multiLevelRecyclerView.setVisibility(View.VISIBLE);
        vehiclesRecyclerView = (RecyclerView) rootView.findViewById(R.id.vehiclesRecyclerView);
        vehiclesRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mainItemsListValue = new ArrayList<>();
    }


    protected void gettingDataByBundle() {
        searchView.setQueryHint(getString(R.string.search));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        searchView.setIconified(false);
                    }
                });
            }
        }, 100);
        searchEditText = (AutoCompleteTextView) searchView.findViewById(R.id.search_src_text);
        searchEditText.setBackgroundColor(Color.WHITE);
        searchEditText.setTextColor(Color.BLACK);
        searchEditText.setDropDownBackgroundResource(android.R.color.white);
        searchEditText.setThreshold(1);
        final View dropDownAnchor = searchView.findViewById(searchEditText.getDropDownAnchor());
        if (dropDownAnchor != null) {
            dropDownAnchor.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    int point[] = new int[2];
                    dropDownAnchor.getLocationOnScreen(point);
                    int dropDownPadding = point[0] + searchEditText.getDropDownHorizontalOffset();
                    //
                    Rect screenSize = new Rect();
                    getActivity().getWindowManager().getDefaultDisplay().getRectSize(screenSize);
                    int screenWidth = screenSize.width();
                    searchEditText.setDropDownWidth((screenWidth - dropDownPadding * 2));
                }
            });
        }
        searchEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                String queryString = (String) adapterView.getItemAtPosition(itemIndex);
                searchEditText.setText(String.format(Locale.getDefault(), "%s", queryString));
                if (!TextUtils.isEmpty(queryString.trim())) {

                }
//                    apiCall(queryString);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (searchView != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                    searchViewListValidation(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchViewListValidation(newText);
                return false;
            }
        });

    }

    private void searchViewListValidation(String value) {
        List<Item> searchListItems = new ArrayList<>();
        if (!TextUtils.isEmpty(value.trim())) {
            expandVisibility(false);
            mainItemsListValue.clear();
            mainItemsListValue.addAll(checkG(itemLists));
            for (Item item : mainItemsListValue) {
                if (item.getName().contains(value)) {
                    searchListItems.add(item);
                }
            }
            initAdapter(searchListItems);
        } else {
            expandVisibility(true);
            mainItemsListValue.clear();
            mainItemsListValue.addAll(checkG(itemLists));
            initAdapter(mainItemsListValue);
        }
    }

    private List<Item> checkG(List<Item> itemLists) {
        List<Item> values = new ArrayList<>();
        for (Item main : itemLists) {
            if (main.getID() != null && main.getID().startsWith("V")) {
                values.add(main);
            }
        }
        return values;
    }


    private void expandVisibility(boolean isExpandTrue) {
        if (isExpandTrue) {
            vehiclesRecyclerView.setVisibility(View.GONE);
            multiLevelRecyclerView.setVisibility(View.VISIBLE);
        } else {
            vehiclesRecyclerView.setVisibility(View.VISIBLE);
            multiLevelRecyclerView.setVisibility(View.GONE);
        }
    }


    private void expandable(String status) {
        if (ShortTermManager.getInstance().getRequestMapsExpendableList() == null ) {
            // Progress.showLoadingDialog(activity);
            BusinessManager.getMainVehiclesListWithStates(status, new ApiCallResponseString() {
                @Override
                public void onSuccess(int statusCode, String responseObject) {
                    try {
                        if (responseObject != null) {
                            ShortTermManager.getInstance().setRequestMapsExpendableList(responseObject);
                        }
                        mainApiCall(responseObject);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, String errorResponse) {
                    Progress.dismissLoadingDialog();
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            mainApiCall(ShortTermManager.getInstance().getRequestMapsExpendableList());
        }

    }

    private void initSingleTreeAdapter(List<Item> itemLists) {
        try {
            getItemsOnly(itemLists.get(0).getChildren());
            initAdapter(mainItemsListValue);
        } catch (Exception ex) {
            ex.getMessage();
        }
    }


    private void initAdapter(List<Item> itemLists) {
        singleVehiclesAdapter = new ReportsSingleVehiclesAdapter(getActivity(), itemLists, new ReportsSingleVehiclesAdapter.SelectedItems() {
            @Override
            public void selectedMainItem(Item model, int position) {
                model.setChecked(!model.isChecked());
                mainItemsListValue.set(position, model);
                singleVehiclesAdapter.notifyItemChanged(position);
                if (model.isChecked()) {
                    isCheckedValueSingleList(model, list, "vehicle");
                } else {
                    unCheckedValueSingleList(model, list, "vehicle");
                }
                addToSelectedList();
            }
        });
        vehiclesRecyclerView.setAdapter(singleVehiclesAdapter);
        singleVehiclesAdapter.notifyDataSetChanged();
    }

    private void addToSelectedList() {
        try {
            if (itemList == null) {
                itemList = new ArrayList<>();
            } else {
                itemList.clear();
            }
            for (Item items : checkG(itemLists)) {
                if (items.isChecked()) {
                    itemList.add(items);
                }
            }
            wizardModel.setItemList(itemList);
        } catch (Exception ex) {
            ex.getMessage();
        }
    }


    private void isCheckedValueSingleList(Item item, List<Item> itemLists, String clickState) {
        Progress.showLoadingDialog(activity);
        BusinessManager.getMainVehiclesListWithQuery(item.getID(), new ApiCallResponseString() {
            @Override
            public void onSuccess(int statusCode, String responseObject) {
                Progress.dismissLoadingDialog();
                try {

                    //
                    Item[] vehicleModel = new Gson().fromJson(responseObject, Item[].class);
                    List<Item> arrayFromApi = Arrays.asList(vehicleModel);
                    for (int x = 0; x < arrayFromApi.size(); x++) {
                        arrayFromApi.get(x).setLevel(item.getLevel());
                    }

                    //
                    Item mainItemsList = item;
                    if (mainItemsList != null && mainItemsList.getChildren() != null && clickState.equalsIgnoreCase("grope")) {

//                        if (mainArrayOfVehiclesList != null && mainArrayOfVehiclesList.size() > 0) {
//                            mainArrayOfVehiclesList.clear();
//                        }

                        List<?> arrayFromApiCasting = mainItemsList.getChildren();
                        List<Item> arrayFrom = (List<Item>) arrayFromApiCasting;
                        if (arrayFrom.size() == arrayFromApi.size()) {
                            for (int x = 0; x < arrayFromApi.size(); x++) {
                                arrayFromApi.get(x).setName(arrayFrom.get(x).getName());
                                arrayFromApi.get(x).setID(arrayFrom.get(x).getID());
                                arrayFromApi.get(x).setChecked(arrayFrom.get(x).isChecked());
                                arrayFromApi.get(x).setClicked(arrayFrom.get(x).isClicked());
                                arrayFromApi.get(x).setParent(item);
                            }
                        }

                    } else if (mainItemsList != null) {
                        if (clickState.equalsIgnoreCase("vehicle")) {
                            if (arrayFromApi.size() > 0) {
                                arrayFromApi.get(0).setName(mainItemsList.getName());
                                arrayFromApi.get(0).setID(mainItemsList.getID());
                                arrayFromApi.get(0).setChecked(mainItemsList.isChecked());
                                arrayFromApi.get(0).setClicked(mainItemsList.isClicked());
                            }
                        }
                    }
                    if (clickState.equalsIgnoreCase("grope")) {
                        if (item.isChecked()) {
                            for (int x = 0; x < arrayFromApi.size(); x++) {
                                arrayFromApi.get(x).setChecked(true);
                            }
                        } else {
                            for (int x = 0; x < arrayFromApi.size(); x++) {
                                arrayFromApi.get(x).setChecked(false);
                            }
                        }
                    } else {
                        if (arrayFromApi.size() > 0)
                            if (item.isChecked()) {
                                arrayFromApi.get(0).setChecked(true);
                            } else {
                                arrayFromApi.get(0).setChecked(false);
                                item.setGroupChecked(true);
                            }
                    }
                    if (mainItemsList != null && mainItemsList.getChildren() != null && clickState.equalsIgnoreCase("grope")) {
                        List<?> newRepositoryArrayListFromChildrenToCHilde = arrayFromApi;
                        mainItemsList.addChildren((List<RecyclerViewItem>) newRepositoryArrayListFromChildrenToCHilde);
                        ArrayList<Item> arrayList = new ArrayList<>(arrayFromApi);
                        mainItemsList.setChilds(arrayList);
                        item.setChilds(arrayList);
                        item.addChildren((List<RecyclerViewItem>) newRepositoryArrayListFromChildrenToCHilde);
                    }

                    for (int x = 0; x < arrayFromApi.size(); x++) {
                        for (int y = 0; y < itemLists.size(); y++) {
                            if (arrayFromApi.get(x).getID().equalsIgnoreCase(itemLists.get(y).getID())) {
                                if (itemLists.get(y).getParent() != null) {
                                    arrayFromApi.get(x).setParent(itemLists.get(y).getParent());
                                }
                                itemLists.set(y, arrayFromApi.get(x));
                            }
                        }
                    }

                    // sime check
                    if (clickState.equalsIgnoreCase("vehicle")) {
                        if (item.getParent() != null) {
                            for (int y = 0; y < itemLists.size(); y++) {
                                if (item.getID().equalsIgnoreCase(itemLists.get(y).getID())) {
                                    if (item.getParent() != null && item.getParent().getChilds() != null && item.getParent().getChilds().size() > 0) {
                                        int isCheckedState = 0;
                                        for (int x = 0; x < item.getParent().getChilds().size(); x++) {
                                            for (int z = 0; z < itemLists.size(); z++) {
                                                if (item.getParent().getChilds().get(x).getID().equalsIgnoreCase(itemLists.get(z).getID())) {
                                                    if (itemLists.get(z).isChecked()) {
                                                        isCheckedState = isCheckedState + 1;
                                                    }
                                                }

                                            }
                                        }
                                        if (isCheckedState == item.getParent().getChilds().size()) {
                                            item.getParent().setGroupChecked(false);
                                            item.getParent().setChecked(true);
                                            for (int zv = 0; zv < itemLists.size(); zv++) {
                                                if (item.getParent().getID().equalsIgnoreCase(itemLists.get(zv).getID())) {
                                                    itemLists.get(zv).setGroupChecked(false);
                                                    itemLists.get(zv).setChecked(true);
                                                }
                                            }
                                        } else if (isCheckedState > 0) {
                                            item.getParent().setGroupChecked(true);
                                            item.getParent().setChecked(false);
                                            for (int zv = 0; zv < itemLists.size(); zv++) {
                                                if (item.getParent().getID().equalsIgnoreCase(itemLists.get(zv).getID())) {
                                                    itemLists.get(zv).setGroupChecked(true);
                                                    itemLists.get(zv).setChecked(false);
                                                }
                                            }
                                        } else {
                                            item.getParent().setGroupChecked(false);
                                            item.getParent().setChecked(false);
                                            for (int zv = 0; zv < itemLists.size(); zv++) {
                                                if (item.getParent().getID().equalsIgnoreCase(itemLists.get(zv).getID())) {
                                                    itemLists.get(zv).setGroupChecked(false);
                                                    itemLists.get(zv).setChecked(false);
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    } else if (clickState.equalsIgnoreCase("grope")) {
                        if (item.isGroupChecked()) {
                            item.setGroupChecked(false);
                            for (int y = 0; y < itemLists.size(); y++) {
                                if (item.getID().equalsIgnoreCase(itemLists.get(y).getID())) {
                                    itemLists.get(y).setGroupChecked(false);
                                }
                            }
                        }
                    }
                    boolean state = false;
                    for (int y = 1; y < itemLists.size(); y++) {
                        if (itemLists.get(y).isChecked()) {
                            state = true;
                        } else {
                            state = false;
                            break;
                        }
                    }
                    if (state) {
                        itemLists.get(0).setGroupChecked(false);
                        itemLists.get(0).setChecked(true);
                    } else {
                        itemLists.get(0).setGroupChecked(true);
                        itemLists.get(0).setChecked(false);
                    }

                    for (int x = 0; x < arrayFromApi.size(); x++) {
                        if (arrayFromApi.get(x).isChecked()) {
                            String firstOne = arrayFromApi.get(x).getID().substring(0, 1);
                            if (firstOne.equalsIgnoreCase("V")) {
                                mainArrayOfVehiclesList.add(arrayFromApi.get(x));
                            }
                        }
                    }


                    HashMap<String, Item> productMap = new HashMap<String, Item>();
                    for (Item p : mainArrayOfVehiclesList) {
                        if (p != null && p.getVehicleID() != null)
                            productMap.put(String.valueOf(p.getVehicleID()), p);
                    }
                    mainArrayOfVehiclesList.clear();
                    mainArrayOfVehiclesList.addAll(productMap.values());


                    expandableAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, String errorResponse) {
                try {
                    Progress.dismissLoadingDialog();
                    Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }


    private void unCheckedValueSingleList(Item item, List<Item> itemLists, String clickState) {
        try {
            if (item != null && clickState.equalsIgnoreCase("grope")) {
                if (item.getChildren() != null) {
                    List<?> arrayFromApiCasting = item.getChildren();
                    List<Item> arrayFrom = (List<Item>) arrayFromApiCasting;
                    for (int x = 0; x < arrayFrom.size(); x++) {
                        arrayFrom.get(x).setChecked(false);
                    }
                    ArrayList<Item> mainArrayList = new ArrayList<>(arrayFrom);
                    item.setChilds(mainArrayList);
                    List<?> arrays = (List<Item>) mainArrayList;
                    item.addChildren((List<RecyclerViewItem>) arrays);
                    item.setChilds(mainArrayList);
                    for (int x = 0; x < item.getChilds().size(); x++) {
                        for (int y = 0; y < itemLists.size(); y++) {
                            if (item.getChilds().get(x).getID().equalsIgnoreCase(itemLists.get(y).getID())) {
                                itemLists.set(y, item.getChilds().get(x));
                            }
                        }
                    }
                }

                if (item.getChildren() != null && item.getChildren().size() > 0) {
                    List<?> arrayFromApiCastings = item.getChildren();
                    List<Item> arrayFroms = (List<Item>) arrayFromApiCastings;
                    for (int x = 0; x < arrayFroms.size(); x++) {
                        for (int y = 0; y < mainArrayOfVehiclesList.size(); y++) {
                            if (arrayFroms.get(x).getID().equalsIgnoreCase(mainArrayOfVehiclesList.get(y).getID())) {
                                mainArrayOfVehiclesList.remove(y);
                            }
                        }
                    }
                }
                int isCheckedState = 0;
                if (itemLists != null) {
                    for (int y = 1; y < itemLists.size(); y++) {
                        if (itemLists.get(y).isChecked()) {
                            isCheckedState = isCheckedState + 1;
                        }
                    }
                    if (isCheckedState == itemLists.size()) {
                        itemLists.get(0).setGroupChecked(false);
                        itemLists.get(0).setChecked(true);
                    } else if (isCheckedState > 0) {
                        itemLists.get(0).setGroupChecked(true);
                        itemLists.get(0).setChecked(false);
                    } else {
                        itemLists.get(0).setGroupChecked(false);
                        itemLists.get(0).setChecked(false);
                    }
                    expandableAdapter.notifyDataSetChanged();
                }
            } else if (clickState.equalsIgnoreCase("vehicle")) {
                for (int y = 0; y < itemLists.size(); y++) {
                    if (item != null && item.getID().equalsIgnoreCase(itemLists.get(y).getID())) {
                        itemLists.set(y, item);
                    }
                }
                boolean state = false;
                for (int y = 0; y < itemLists.size(); y++) {
                    if (item != null && item.getParent() != null && item.getParent().getID() != null && item.getParent().getID().equalsIgnoreCase(itemLists.get(y).getID())) {
                        if (item.getParent().getChilds() != null && item.getParent().getChilds().size() > 0) {
                            for (int x = 0; x < item.getParent().getChilds().size(); x++) {
                                for (int v = 0; v < itemLists.size(); v++) {
                                    if (item.getParent().getChilds().get(x).getID().equalsIgnoreCase(itemLists.get(v).getID())) {
                                        if (itemLists.get(v).isChecked()) {
                                            state = true;
                                            if (itemLists.get(v).getParent().getID().equalsIgnoreCase(itemLists.get(y).getID())) {
                                                itemLists.get(y).setChecked(false);
                                                itemLists.get(y).setGroupChecked(true);
                                            }
                                        } else {
                                            if (!state) {
                                                if (x == item.getParent().getChilds().size() - 1) {
                                                    if (itemLists.get(v).getParent().getID().equalsIgnoreCase(itemLists.get(y).getID())) {
                                                        itemLists.get(y).setChecked(false);
                                                        itemLists.get(y).setGroupChecked(false);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                int isCheckedState = 0;
                for (int y = 1; y < itemLists.size(); y++) {
                    if (itemLists.get(y).isChecked()) {
                        isCheckedState = isCheckedState + 1;
                    }
                }
                if (isCheckedState == itemLists.size()) {
                    itemLists.get(0).setGroupChecked(false);
                    itemLists.get(0).setChecked(true);
                } else if (isCheckedState > 0) {
                    itemLists.get(0).setGroupChecked(true);
                    itemLists.get(0).setChecked(false);
                } else {
                    itemLists.get(0).setGroupChecked(false);
                    itemLists.get(0).setChecked(false);
                }
                for (int y = 0; y < mainArrayOfVehiclesList.size(); y++) {
                    if (item != null)
                        if (item.getID().equalsIgnoreCase(mainArrayOfVehiclesList.get(y).getID())) {
                            mainArrayOfVehiclesList.remove(y);
                        }
                }

                if (item != null && item.getParent() != null) { // add the selected car with all its new data like selected and checked to ites parent by replasing its child
                    for (int y = 0; y < itemLists.size(); y++) {
                        if (item.getParent().getID().equalsIgnoreCase(itemLists.get(y).getID())) {
                            if (itemLists.get(y) != null && itemLists.get(y).getChildren() != null && itemLists.get(y).getChildren().size() > 0) {
                                for (int x = 0; x < itemLists.get(y).getChildren().size(); x++) {
                                    List<?> array = itemLists.get(y).getChildren();
                                    List<Item> arrayItm = (List<Item>) array;
                                    if (item.getID().equalsIgnoreCase(arrayItm.get(x).getID())) {
                                        arrayItm.set(x, item);
                                    }
                                }
                            }
                        }
                    }
                }
                expandableAdapter.notifyDataSetChanged();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void mainApiCall(String responseObject) {
        try {
            if (list == null)
                list = new ArrayList<>();
            if (mainChecks == null)
                mainChecks = new ArrayList<>();
            if (itemLists == null) {
                JSONObject jsonObject = new JSONObject(responseObject);
                JSONObject mainAdd = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObject);
                mainAdd.put("Childs", jsonArray);
                mainAdd.put("ID", "0");
                mainAdd.put("Name", "o0");
                mainAdd.put("VehicleStatus", "0");
                itemLists = (List<Item>) nestedLoop(mainAdd, 0);
                list = itemLists;
                try {
                    if (itemLists != null && itemLists.size() > 0) {
                        initSingleTreeAdapter(itemLists);
                    }
                } catch (Exception ex) {
                    ex.getMessage();
                }
            }
            Progress.dismissLoadingDialog();
            if (expandableAdapter == null) {
                expandableAdapter = new ExpandableAdapter(activity, itemLists, multiLevelRecyclerView, new ExpandableAdapter.ActionsInterface() {
                    @Override
                    public void ItemClicked(Item item, int position, boolean checkedState, String clickState) {
                        if (!checkedState) {
                            if (position == 0) {
                                unCheckAll(item);
                            } else {
                                unChecked(item, itemLists, clickState);
                            }
                        } else {
                            if (position == 0) {
                                isCheckedValueAll(item, itemLists);
                            } else {
                                isCheckedValue(item, itemLists, position, clickState);
                            }
                        }
                        addToSelectedList();
                        //getItemsOnly(itemLists.get(0).getChildren());
                    }
                });
                multiLevelRecyclerView.setAdapter(expandableAdapter);
                for (int x = 0; x < itemLists.size(); x++) {

                    String vehicle = itemLists.get(x).getID();
                    String firstOne = vehicle.substring(0, 1);
                    if (firstOne.equalsIgnoreCase("G")) {
                        multiLevelRecyclerView.openTill(x);
                    }
                }
            }
            multiLevelRecyclerView.setToggleItemOnClick(false);
            multiLevelRecyclerView.setAccordion(false);
            multiLevelRecyclerView.scrollToPosition(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void unCheckAll(Item item) {
        try {
            if (itemLists != null) {
                if (item.isExpanded()) {
                    for (int x = 0; x < itemLists.size(); x++) {
                        itemLists.get(x).setChecked(false);
                        itemLists.get(x).setGroupChecked(false);
                    }
                    for (int x = 0; x < list.size(); x++) {
                        list.get(x).setChecked(false);
                        list.get(x).setGroupChecked(false);
                    }
                } else {
                    if (itemLists != null && itemLists.size() > 0 && itemLists.get(0).getChildren() != null && itemLists.get(0).getChildren().size() > 0) {
                        itemArrayListCallas = new ArrayList<>();
                        getLength(itemLists.get(0).getChildren(), false, false);
                    }
                }
            }
            if (mainArrayOfVehiclesList.size() > 0) {
                mainArrayOfVehiclesList.clear();
            }
            expandableAdapter.notifyDataSetChanged();
        } catch (Exception ex) {
            ex.getMessage();
        }
    }


    private void isCheckedValue(Item item, List<Item> itemLists, int position, String clickState) {
        Progress.showLoadingDialog(activity);
        BusinessManager.getMainVehiclesListWithQuery(item.getID(), new ApiCallResponseString() {
            @Override
            public void onSuccess(int statusCode, String responseObject) {
                Progress.dismissLoadingDialog();
                try {

                    //
                    Item[] vehicleModel = new Gson().fromJson(responseObject, Item[].class);
                    List<Item> arrayFromApi = Arrays.asList(vehicleModel);
                    for (int x = 0; x < arrayFromApi.size(); x++) {
                        arrayFromApi.get(x).setLevel(item.getLevel());
                    }

                    //
                    Item mainItemsList = itemLists.get(position);
                    if (mainItemsList != null && mainItemsList.getChildren() != null && clickState.equalsIgnoreCase("grope")) {

//                        if (mainArrayOfVehiclesList != null && mainArrayOfVehiclesList.size() > 0) {
//                            mainArrayOfVehiclesList.clear();
//                        }

                        List<?> arrayFromApiCasting = mainItemsList.getChildren();
                        List<Item> arrayFrom = (List<Item>) arrayFromApiCasting;
                        if (arrayFrom.size() == arrayFromApi.size()) {
                            for (int x = 0; x < arrayFromApi.size(); x++) {
                                arrayFromApi.get(x).setName(arrayFrom.get(x).getName());
                                arrayFromApi.get(x).setID(arrayFrom.get(x).getID());
                                arrayFromApi.get(x).setChecked(arrayFrom.get(x).isChecked());
                                arrayFromApi.get(x).setClicked(arrayFrom.get(x).isClicked());
                                arrayFromApi.get(x).setParent(item);
                            }
                        }

                    } else if (mainItemsList != null) {
                        if (clickState.equalsIgnoreCase("vehicle")) {
                            if (arrayFromApi.size() > 0) {
                                arrayFromApi.get(0).setName(mainItemsList.getName());
                                arrayFromApi.get(0).setID(mainItemsList.getID());
                                arrayFromApi.get(0).setChecked(mainItemsList.isChecked());
                                arrayFromApi.get(0).setClicked(mainItemsList.isClicked());
                            }
                        }
                    }
                    if (clickState.equalsIgnoreCase("grope")) {
                        if (item.isChecked()) {
                            for (int x = 0; x < arrayFromApi.size(); x++) {
                                arrayFromApi.get(x).setChecked(true);
                            }
                        } else {
                            for (int x = 0; x < arrayFromApi.size(); x++) {
                                arrayFromApi.get(x).setChecked(false);
                            }
                        }
                    } else {
                        if (arrayFromApi.size() > 0)
                            if (item.isChecked()) {
                                arrayFromApi.get(0).setChecked(true);
                            } else {
                                arrayFromApi.get(0).setChecked(false);
                                item.setGroupChecked(true);
                            }
                    }
                    if (mainItemsList != null && mainItemsList.getChildren() != null && clickState.equalsIgnoreCase("grope")) {
                        List<?> newRepositoryArrayListFromChildrenToCHilde = arrayFromApi;
                        mainItemsList.addChildren((List<RecyclerViewItem>) newRepositoryArrayListFromChildrenToCHilde);
                        ArrayList<Item> arrayList = new ArrayList<>(arrayFromApi);
                        mainItemsList.setChilds(arrayList);
                        itemLists.get(position).setChilds(arrayList);
                        itemLists.get(position).addChildren((List<RecyclerViewItem>) newRepositoryArrayListFromChildrenToCHilde);
                    }

                    for (int x = 0; x < arrayFromApi.size(); x++) {
                        for (int y = 0; y < itemLists.size(); y++) {
                            if (arrayFromApi.get(x).getID().equalsIgnoreCase(itemLists.get(y).getID())) {
                                if (itemLists.get(y).getParent() != null) {
                                    arrayFromApi.get(x).setParent(itemLists.get(y).getParent());
                                }
                                itemLists.set(y, arrayFromApi.get(x));
                            }
                        }
                    }

                    // sime check
                    if (clickState.equalsIgnoreCase("vehicle")) {
                        if (item.getParent() != null) {
                            for (int y = 0; y < itemLists.size(); y++) {
                                if (item.getID().equalsIgnoreCase(itemLists.get(y).getID())) {
                                    if (item.getParent() != null && item.getParent().getChilds() != null && item.getParent().getChilds().size() > 0) {
                                        int isCheckedState = 0;
                                        for (int x = 0; x < item.getParent().getChilds().size(); x++) {
                                            for (int z = 0; z < itemLists.size(); z++) {
                                                if (item.getParent().getChilds().get(x).getID().equalsIgnoreCase(itemLists.get(z).getID())) {
                                                    if (itemLists.get(z).isChecked()) {
                                                        isCheckedState = isCheckedState + 1;
                                                    }
                                                }

                                            }
                                        }
                                        if (isCheckedState == item.getParent().getChilds().size()) {
                                            item.getParent().setGroupChecked(false);
                                            item.getParent().setChecked(true);
                                            for (int zv = 0; zv < itemLists.size(); zv++) {
                                                if (item.getParent().getID().equalsIgnoreCase(itemLists.get(zv).getID())) {
                                                    itemLists.get(zv).setGroupChecked(false);
                                                    itemLists.get(zv).setChecked(true);
                                                }
                                            }
                                        } else if (isCheckedState > 0) {
                                            item.getParent().setGroupChecked(true);
                                            item.getParent().setChecked(false);
                                            for (int zv = 0; zv < itemLists.size(); zv++) {
                                                if (item.getParent().getID().equalsIgnoreCase(itemLists.get(zv).getID())) {
                                                    itemLists.get(zv).setGroupChecked(true);
                                                    itemLists.get(zv).setChecked(false);
                                                }
                                            }
                                        } else {
                                            item.getParent().setGroupChecked(false);
                                            item.getParent().setChecked(false);
                                            for (int zv = 0; zv < itemLists.size(); zv++) {
                                                if (item.getParent().getID().equalsIgnoreCase(itemLists.get(zv).getID())) {
                                                    itemLists.get(zv).setGroupChecked(false);
                                                    itemLists.get(zv).setChecked(false);
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    } else if (clickState.equalsIgnoreCase("grope")) {
                        if (item.isGroupChecked()) {
                            item.setGroupChecked(false);
                            for (int y = 0; y < itemLists.size(); y++) {
                                if (item.getID().equalsIgnoreCase(itemLists.get(y).getID())) {
                                    itemLists.get(y).setGroupChecked(false);
                                }
                            }
                        }
                    }
                    boolean state = false;
                    for (int y = 1; y < itemLists.size(); y++) {
                        if (itemLists.get(y).isChecked()) {
                            state = true;
                        } else {
                            state = false;
                            break;
                        }
                    }
                    if (state) {
                        itemLists.get(0).setGroupChecked(false);
                        itemLists.get(0).setChecked(true);
                    } else {
                        itemLists.get(0).setGroupChecked(true);
                        itemLists.get(0).setChecked(false);
                    }

                    for (int x = 0; x < arrayFromApi.size(); x++) {
                        if (arrayFromApi.get(x).isChecked()) {
                            String firstOne = arrayFromApi.get(x).getID().substring(0, 1);
                            if (firstOne.equalsIgnoreCase("V")) {
                                mainArrayOfVehiclesList.add(arrayFromApi.get(x));
                            }
                        }
                    }


                    HashMap<String, Item> productMap = new HashMap<String, Item>();
                    for (Item p : mainArrayOfVehiclesList) {
                        if (p != null && p.getVehicleID() != null)
                            productMap.put(String.valueOf(p.getVehicleID()), p);
                    }
                    mainArrayOfVehiclesList.clear();
                    mainArrayOfVehiclesList.addAll(productMap.values());


                    expandableAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, String errorResponse) {
                try {
                    Progress.dismissLoadingDialog();
                    Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void isCheckedValueAll(Item item, List<Item> itemLists) {
        Progress.showLoadingDialog(activity);
        BusinessManager.getMainVehiclesListWithQuery(item.getID(), new ApiCallResponseString() {
            @Override
            public void onSuccess(int statusCode, String responseObject) {
                if (item.isExpanded()) {
                    try {
//                        handler = new Handler();
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
                        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //
                                Item[] vehicleModel = new Gson().fromJson(responseObject, Item[].class);
                                List<Item> arrayFromApis = Arrays.asList(vehicleModel);

                                HashMap<String, Item> productMap = new HashMap<String, Item>();
                                try {
                                    for (Item p : arrayFromApis) {
                                        if (p != null && p.getVehicleID() != null)
                                            productMap.put(String.valueOf(p.getVehicleID()), p);
                                    }
//                                        for (Item item1Main : arrayFromApi) {// long time
                                    for (Item itemListMain : itemLists) {
                                        String vehicle = itemListMain.getID();
                                        //int vehicleId = item1Main.getVehicleID();
                                        String firstOne = vehicle.substring(0, 1);
                                        if (firstOne.equalsIgnoreCase("V")) {
                                            vehicle = vehicle.substring(2, vehicle.length());
                                            Item itemsK = productMap.get(vehicle);
                                            itemsK.setLevel(itemListMain.getLevel());
                                            itemsK.setChecked(itemListMain.isChecked());
                                            itemsK.setClicked(itemListMain.isClicked());
                                            itemsK.setID(itemListMain.getID());
                                            itemsK.setName(itemListMain.getName());
                                        }
                                    }
                                } catch (Exception ex) {
                                    ex.getMessage();
                                }
                                List<Item> arrayFromApi = new ArrayList<>(productMap.values());
//                                        }

                                for (Item mainYItems : itemLists) {
                                    String grope = mainYItems.getID();
                                    String firstOne = grope.substring(0, 1);
                                    if (firstOne.equalsIgnoreCase("G")) {
                                        if (!mainYItems.isExpanded()) {
                                            getLength(mainYItems.getChildren(), true, false);
                                            for (Item itemsX : arrayFromApi) {
                                                if (mainYItems.getChildren() != null)
                                                    for (int h = 0; h < mainYItems.getChildren().size(); h++) {
                                                        List<?> array = mainYItems.getChildren();
                                                        List<Item> mainArray = (List<Item>) array;
                                                        String vehicle = mainArray.get(h).getID();
                                                        int vehicleId = itemsX.getVehicleID();
                                                        String firstOnes = vehicle.substring(0, 1);
                                                        if (firstOnes.equalsIgnoreCase("V")) {
                                                            vehicle = vehicle.substring(2, vehicle.length());
                                                            if (Integer.valueOf(vehicle) == vehicleId) {
                                                                itemsX.setLevel(mainArray.get(h).getLevel());
                                                                itemsX.setChecked(mainArray.get(h).isChecked());
                                                                itemsX.setClicked(mainArray.get(h).isClicked());
                                                                itemsX.setID(mainArray.get(h).getID());
                                                                itemsX.setName(mainArray.get(h).getName());
                                                            }
                                                        }
                                                    }
                                            }
                                        }
                                    }
                                }

                                if (itemArrayListCallas != null && itemArrayListCallas.size() > 0)
                                    for (int x = 0; x < arrayFromApi.size(); x++) {
                                        for (int y = 0; y < itemArrayListCallas.size(); y++) {
                                            String vehicle = itemArrayListCallas.get(y).getID();
                                            int vehicleId = arrayFromApi.get(x).getVehicleID();
                                            String firstOne = vehicle.substring(0, 1);
                                            if (firstOne.equalsIgnoreCase("V")) {
                                                vehicle = vehicle.substring(2, vehicle.length());
                                                if (Integer.valueOf(vehicle) == vehicleId) {
                                                    arrayFromApi.get(x).setLevel(itemArrayListCallas.get(y).getLevel());
                                                    arrayFromApi.get(x).setChecked(itemArrayListCallas.get(y).isChecked());
                                                    arrayFromApi.get(x).setClicked(itemArrayListCallas.get(y).isClicked());
                                                    arrayFromApi.get(x).setID(itemArrayListCallas.get(y).getID());
                                                    arrayFromApi.get(x).setName(itemArrayListCallas.get(y).getName());
                                                }
                                            }
                                        }
                                    }

                                // int x = 0;
                                // for (Item itemX : arrayFromApi) {// long time

                                HashMap<String, Item> productMaps = new HashMap<String, Item>();

                                for (Item p : arrayFromApi) {
                                    if (p != null && p.getVehicleID() != null)
                                        productMaps.put(String.valueOf(p.getVehicleID()), p);
                                }

                                int y = 0;
                                for (Item itemY : itemLists) {
                                    String vehicle = itemY.getID();
                                    //int vehicleId = itemX.getVehicleID();
                                    String firstOne = vehicle.substring(0, 1);
                                    if (firstOne.equalsIgnoreCase("V")) {
                                        vehicle = vehicle.substring(2, vehicle.length());
//                                                    if (Integer.valueOf(vehicle) == vehicleId) {
                                        Item itemX = productMaps.get(vehicle);
                                        if (itemY.getParent() != null) {
                                            itemX.setParent(itemY.getParent());
                                        }
                                        Item items = itemX;
                                        itemLists.set(y, items);
//                                                    }
                                    } else {
                                        if (itemY.getChildren() != null) {
                                            List<?> array = itemY.getChildren();
                                            ArrayList<?> arrayList = new ArrayList<>(array);
                                            Item items = itemY;
                                            items.setChilds((ArrayList<Item>) arrayList);
                                            itemLists.set(y, items);
                                        }
                                    }
                                    y++;
                                }
                                // x++;
                                //}

                                for (Item itemX : itemLists) {
                                    itemX.setGroupChecked(false);
                                    itemX.setChecked(true);
                                }

                                List<?> newRepositoryArray = itemLists.get(0).getChildren();
                                List<Item> arrayList = (List<Item>) newRepositoryArray; // get all grupes

                                int x1 = 0;
                                for (Item itemX : arrayList) {
                                    List<?> children = itemX.getChildren(); // get childe of the grupe
                                    List<Item> mainChildren = (List<Item>) children;
                                    if (mainChildren != null && mainChildren.size() > 0) {
                                        int y1 = 0;
                                        for (Item itemY : mainChildren) {
                                            int z1 = 0;
                                            for (Item itemZ : itemLists) {
                                                String vehicle = itemZ.getID();
                                                String vehicleId = itemY.getID();
                                                if (vehicle.equalsIgnoreCase(vehicleId)) {
                                                    mainChildren.set(y1, itemZ);
                                                }
                                                z1++;
                                            }
                                            y1++;
                                        }
                                    }
                                    x1++;
                                }

                                if (mainArrayOfVehiclesList.size() > 0)
                                    mainArrayOfVehiclesList.clear();
                                mainArrayOfVehiclesList.addAll(arrayFromApi);
                                expandableAdapter.notifyDataSetChanged();
                                Progress.dismissLoadingDialog();
                            }
                        });
//                            }
//                        }).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        Item[] vehicleModel = new Gson().fromJson(responseObject, Item[].class);
                        List<Item> arrayFromApi = Arrays.asList(vehicleModel);
                        if (itemLists != null && itemLists.size() > 0 && itemLists.get(0).getChildren() != null && itemLists.get(0).getChildren().size() > 0) {
                            itemArrayListCallas = new ArrayList<>();
                            getLength(itemLists.get(0).getChildren(), true, false);
                            if (mainArrayOfVehiclesList.size() > 0) {
                                mainArrayOfVehiclesList.clear();
                            }
                            if (arrayFromApi.size() == itemArrayListCallas.size()) {
                                for (int x = 0; x < arrayFromApi.size(); x++) {
                                    Item mainItem = itemArrayListCallas.get(x);
                                    Item mainItemStyle = arrayFromApi.get(x);
                                    if (mainItem != null && mainItem.getID() != null)
                                        mainItemStyle.setID(mainItem.getID());
                                    if (mainItem != null && mainItem.getName() != null)
                                        mainItemStyle.setName(mainItem.getName());
                                    if (mainItem != null && mainItem.getParent() != null)
                                        mainItemStyle.setParent(mainItem.getParent());
                                    if (mainItem != null)
                                        mainItemStyle.setLevel(mainItem.getLevel());
                                    if (mainItem != null)
                                        mainItemStyle.setPosition(mainItem.getPosition());
                                    if (mainItem != null)
                                        mainItemStyle.setExpanded(mainItem.isExpanded());
                                    if (mainItem != null)
                                        mainItemStyle.setClicked(mainItem.isClicked());
                                    if (mainItem != null)
                                        mainItemStyle.setChecked(mainItem.isChecked());
                                    arrayFromApi.set(x, mainItemStyle);
                                    mainArrayOfVehiclesList.add(arrayFromApi.get(x));
                                }
                            } else {
                                Toast.makeText(activity, "ERROR", Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    Progress.dismissLoadingDialog();
                }

            }

            @Override
            public void onFailure(int statusCode, String errorResponse) {
                try {
                    Progress.dismissLoadingDialog();
                    Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }


    private void unChecked(Item item, List<Item> itemLists, String clickState) {
        try {
            if (item != null && clickState.equalsIgnoreCase("grope")) {
                if (item.getChildren() != null) {
                    List<?> arrayFromApiCasting = item.getChildren();
                    List<Item> arrayFrom = (List<Item>) arrayFromApiCasting;
                    for (int x = 0; x < arrayFrom.size(); x++) {
                        arrayFrom.get(x).setChecked(false);
                    }
                    ArrayList<Item> mainArrayList = new ArrayList<>(arrayFrom);
                    item.setChilds(mainArrayList);
                    List<?> arrays = (List<Item>) mainArrayList;
                    item.addChildren((List<RecyclerViewItem>) arrays);
                    item.setChilds(mainArrayList);
                    for (int x = 0; x < item.getChilds().size(); x++) {
                        for (int y = 0; y < itemLists.size(); y++) {
                            if (item.getChilds().get(x).getID().equalsIgnoreCase(itemLists.get(y).getID())) {
                                itemLists.set(y, item.getChilds().get(x));
                            }
                        }
                    }
                }

                if (item.getChildren() != null && item.getChildren().size() > 0) {
                    List<?> arrayFromApiCastings = item.getChildren();
                    List<Item> arrayFroms = (List<Item>) arrayFromApiCastings;
                    for (int x = 0; x < arrayFroms.size(); x++) {
                        for (int y = 0; y < mainArrayOfVehiclesList.size(); y++) {
                            if (arrayFroms.get(x).getID().equalsIgnoreCase(mainArrayOfVehiclesList.get(y).getID())) {
                                mainArrayOfVehiclesList.remove(y);
                            }
                        }
                    }
                }
                int isCheckedState = 0;
                if (itemLists != null) {
                    for (int y = 1; y < itemLists.size(); y++) {
                        if (itemLists.get(y).isChecked()) {
                            isCheckedState = isCheckedState + 1;
                        }
                    }
                    if (isCheckedState == itemLists.size()) {
                        itemLists.get(0).setGroupChecked(false);
                        itemLists.get(0).setChecked(true);
                    } else if (isCheckedState > 0) {
                        itemLists.get(0).setGroupChecked(true);
                        itemLists.get(0).setChecked(false);
                    } else {
                        itemLists.get(0).setGroupChecked(false);
                        itemLists.get(0).setChecked(false);
                    }
                    expandableAdapter.notifyDataSetChanged();
                }
            } else if (clickState.equalsIgnoreCase("vehicle")) {
                for (int y = 0; y < itemLists.size(); y++) {
                    if (item != null && item.getID().equalsIgnoreCase(itemLists.get(y).getID())) {
                        itemLists.set(y, item);
                    }
                }
                boolean state = false;
                for (int y = 0; y < itemLists.size(); y++) {
                    if (item != null && item.getParent() != null && item.getParent().getID() != null && item.getParent().getID().equalsIgnoreCase(itemLists.get(y).getID())) {
                        if (item.getParent().getChilds() != null && item.getParent().getChilds().size() > 0) {
                            for (int x = 0; x < item.getParent().getChilds().size(); x++) {
                                for (int v = 0; v < itemLists.size(); v++) {
                                    if (item.getParent().getChilds().get(x).getID().equalsIgnoreCase(itemLists.get(v).getID())) {
                                        if (itemLists.get(v).isChecked()) {
                                            state = true;
                                            if (itemLists.get(v).getParent().getID().equalsIgnoreCase(itemLists.get(y).getID())) {
                                                itemLists.get(y).setChecked(false);
                                                itemLists.get(y).setGroupChecked(true);
                                            }
                                        } else {
                                            if (!state) {
                                                if (x == item.getParent().getChilds().size() - 1) {
                                                    if (itemLists.get(v).getParent().getID().equalsIgnoreCase(itemLists.get(y).getID())) {
                                                        itemLists.get(y).setChecked(false);
                                                        itemLists.get(y).setGroupChecked(false);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                int isCheckedState = 0;
                for (int y = 1; y < itemLists.size(); y++) {
                    if (itemLists.get(y).isChecked()) {
                        isCheckedState = isCheckedState + 1;
                    }
                }
                if (isCheckedState == itemLists.size()) {
                    itemLists.get(0).setGroupChecked(false);
                    itemLists.get(0).setChecked(true);
                } else if (isCheckedState > 0) {
                    itemLists.get(0).setGroupChecked(true);
                    itemLists.get(0).setChecked(false);
                } else {
                    itemLists.get(0).setGroupChecked(false);
                    itemLists.get(0).setChecked(false);
                }
                for (int y = 0; y < mainArrayOfVehiclesList.size(); y++) {
                    if (item != null)
                        if (item.getID().equalsIgnoreCase(mainArrayOfVehiclesList.get(y).getID())) {
                            mainArrayOfVehiclesList.remove(y);
                        }
                }

                if (item != null && item.getParent() != null) { // add the selected car with all its new data like selected and checked to ites parent by replasing its child
                    for (int y = 0; y < itemLists.size(); y++) {
                        if (item.getParent().getID().equalsIgnoreCase(itemLists.get(y).getID())) {
                            if (itemLists.get(y) != null && itemLists.get(y).getChildren() != null && itemLists.get(y).getChildren().size() > 0) {
                                for (int x = 0; x < itemLists.get(y).getChildren().size(); x++) {
                                    List<?> array = itemLists.get(y).getChildren();
                                    List<Item> arrayItm = (List<Item>) array;
                                    if (item.getID().equalsIgnoreCase(arrayItm.get(x).getID())) {
                                        arrayItm.set(x, item);
                                    }
                                }
                            }
                        }
                    }
                }
                expandableAdapter.notifyDataSetChanged();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void getItemsOnly(List<RecyclerViewItem> items) {
        try {
            // mainItemsListValue.clear();
            for (int y = 0; y < items.size(); y++) {
                if (items.get(y).getChildren() != null && items.get(y).getChildren().size() > 0) {
                    getItemsOnly(items.get(y).getChildren());
                }
                List<?> newRepositoryArray = items;
                List<Item> arrayList = (List<Item>) newRepositoryArray;
                List<?> newArray = arrayList.get(y).getChildren();
                if (newArray != null && newArray.size() > 0 && mainItemsListValue != null) {
                    mainItemsListValue.addAll((List<Item>) newArray);
                }
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
    }


    private void getLength(List<RecyclerViewItem> items, boolean isChecked, boolean isGroupChecked) {
        try {
            for (int y = 0; y < items.size(); y++) {
                if (items.get(y).getChildren() != null && items.get(y).getChildren().size() > 0) {
                    getLength(items.get(y).getChildren(), isChecked, isGroupChecked);
                }
                List<?> newRepositoryArray = items;
                List<Item> arrayList = (List<Item>) newRepositoryArray;
                arrayList.get(y).setChecked(isChecked);
                arrayList.get(y).setGroupChecked(isGroupChecked);
                List<?> newArray = arrayList.get(y).getChildren();
                if (newArray != null && newArray.size() > 0 && itemArrayListCallas != null) {
                    itemArrayListCallas.addAll((List<Item>) newArray);
                }
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private List<?> nestedLoop(JSONObject levelList, int level) {
        try {
            Item parentItems = null;
            List<RecyclerViewItem> itemList = new ArrayList<>();
            if (levelList != null)
                parentItems = new Gson().fromJson(String.valueOf(levelList), Item.class);
            JSONArray jsonArrayStringList = levelList.getJSONArray("Childs");
            int length = jsonArrayStringList.length();
            for (int i = 0; i < length; i++) {
                JSONObject itemObject = jsonArrayStringList.getJSONObject(i);
                if (itemObject.has("Childs") && !itemObject.isNull("Childs")) {
                    Item items = new Item(level);
                    int childrenSize = itemObject.getJSONArray("Childs").length();
                    if (childrenSize > 0) {
                        level = level + 1;
                        items.addChildren((List<RecyclerViewItem>) nestedLoop(itemObject, level));
                    }
                    items.setName(itemObject.getString("Name"));
                    items.setID(itemObject.getString("ID"));
                    items.setVehicleStatus(itemObject.getString("VehicleStatus"));
                    items.setClicked(true);
                    items.setParent(parentItems);
                    itemList.add(items);
                } else {
                    Item items = new Item(level);
                    items.setName(itemObject.getString("Name"));
                    items.setID(itemObject.getString("ID"));
                    items.setVehicleStatus(itemObject.getString("VehicleStatus"));
                    items.setClicked(true);
                    items.setParent(parentItems);
                    itemList.add(items);
                }
            }
            return itemList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
