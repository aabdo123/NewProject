package com.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.adapters.ListOfUsersAdapter;
import com.managers.ApiCallResponse;
import com.managers.BusinessManager;
import com.models.ListOfUsersModel;
import com.utilities.ToastHelper;
import com.utilities.Utils;
import com.views.Progress;
import com.views.TextViewRegular;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListOfUsersFragment extends Fragment {

    public static ListOfUsersFragment usersFragment;
    private FragmentActivity activity;
    private TextViewRegular emptyListTextView;
    private RecyclerView listOfUsersRecyclerView;
    private List<ListOfUsersModel> listOfUsersList;

    public static ListOfUsersFragment newInstance() {
        if (usersFragment == null)
            usersFragment = new ListOfUsersFragment();
        return usersFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_of_users, container, false);
        initView(rootView);
        getUserListApiCall();
        return rootView;
    }

    private void initView(View rootView) {
        emptyListTextView = (TextViewRegular) rootView.findViewById(R.id.emptyListTextView);
        listOfUsersRecyclerView = (RecyclerView) rootView.findViewById(R.id.listOfUsersRecyclerView);
        listOfUsersRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
    }

    private void initAdapter() {
        ListOfUsersAdapter adapter = new ListOfUsersAdapter(getActivity(), listOfUsersList);
        listOfUsersRecyclerView.setAdapter(adapter);
    }

    private void getUserListApiCall() {
//        Progress.showLoadingDialog(activity);
        BusinessManager.getListOfUsers(new ApiCallResponse() {
            @Override
            public void onSuccess(int statusCode, Object responseObject) {
                ListOfUsersModel[] model = (ListOfUsersModel[]) responseObject;
                listOfUsersList = new ArrayList<>();
                Collections.addAll(listOfUsersList, model);
                if (Utils.isNotEmptyList(listOfUsersList)) {
                    initAdapter();
                } else {
                    emptyListTextView.setVisibility(View.VISIBLE);
                }
//                Progress.dismissLoadingDialog();
            }

            @Override
            public void onFailure(int statusCode, String errorResponse) {
//                Progress.dismissLoadingDialog();
                ToastHelper.toastInfo(activity, getString(R.string.something_went_worng));
            }
        });
    }
}

