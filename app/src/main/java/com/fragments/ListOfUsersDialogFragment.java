//package com.fragments;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.DialogFragment;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.R;
//import com.activities.AddReportsActivity;
//import com.adapters.ListOfUsersAdapter;
//import com.models.ReportsModel;
//import com.utilities.Utils;
//import com.views.ButtonBold;
//import com.views.TextViewRegular;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ListOfUsersDialogFragment extends DialogFragment implements View.OnClickListener {
//
//    private Context context;
//    private ButtonBold doneButton;
//    private ButtonBold cancelButton;
//    private TextViewRegular titleTextView;
//    private TextViewRegular emptyListTextView;
//    private RecyclerView listOfUsersRecyclerView;
//    private static List<ReportsModel> reportsModelList;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        context = getContext();
//        AddReportsActivity.wizardModel.removeAllUsersList();
//        parsingData();
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_list_of_users_dialog, container, false);
//        initView(rootView);
//        initListeners();
//        initAdapter();
//        return rootView;
//    }
//
//    private void initView(View rootView) {
//        doneButton = (ButtonBold) rootView.findViewById(R.id.doneButton);
//        cancelButton = (ButtonBold) rootView.findViewById(R.id.cancelButton);
//        titleTextView = (TextViewRegular) rootView.findViewById(R.id.titleTextView);
//        emptyListTextView = (TextViewRegular) rootView.findViewById(R.id.emptyListTextView);
//        listOfUsersRecyclerView = (RecyclerView) rootView.findViewById(R.id.listOfUsersRecyclerView);
//        listOfUsersRecyclerView.setLayoutManager(new LinearLayoutManager(context));
//    }
//
//    private void initListeners() {
//        doneButton.setOnClickListener(this);
//        cancelButton.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.doneButton:
//                getDialog().dismiss();
//                break;
//
//            case R.id.cancelButton:
//                getDialog().dismiss();
//                break;
//        }
//    }
//
//    private void initAdapter() {
//        ListOfUsersAdapter adapter = new ListOfUsersAdapter(getActivity(), reportsModelList);
//        listOfUsersRecyclerView.setAdapter(adapter);
//    }
//
//    private void parsingData() {
//        reportsModelList = new ArrayList<>();
//        try {
//            JSONArray jsonArray = new JSONArray(Utils.loadJSONFile(context, "report.json"));
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObjectFile = jsonArray.getJSONObject(i);
//                int id = jsonObjectFile.getInt("id");
//                String reportKey = jsonObjectFile.getString("report_key");
//                String reportName = jsonObjectFile.getString("report_name");
//                ReportsModel model = new ReportsModel();
//                model.setId(id);
//                model.setReportKey(reportKey);
//                model.setReportName(reportName);
//                model.setChecked(false);
//                reportsModelList.add(model);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onDismiss(final DialogInterface dialog) {
//        super.onDismiss(dialog);
//        final Activity activity = getActivity();
//        if (activity instanceof DialogInterface.OnDismissListener) {
//            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
//        }
//    }
//}
