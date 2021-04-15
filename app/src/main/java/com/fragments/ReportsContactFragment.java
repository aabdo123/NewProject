package com.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.adapters.ContactPageAdapter;
import com.adapters.ReportContactAdapter;
import com.utilities.AnimationUtils;

import java.util.ArrayList;
import java.util.List;

public class ReportsContactFragment extends Fragment {

    public static ReportsContactFragment contactFragment;
    private FragmentActivity activity;
    private RecyclerView contactRecyclerView;
//    // OLD DESIGN
//    private RecyclerView emailRecyclerView;
//    private RecyclerView phonesRecyclerView;
//    private AppCompatEditText emailEditText;
//    private AppCompatEditText phonesEditText;
//    private CheckBox notifyCheckBox;
//    private LinearLayout phonesLayout;
//    private ReportContactAdapter emailsAdapter;
//    private ReportContactAdapter phonesAdapter;
//    private List<String> emailsList = new ArrayList<>();
//    private List<String> phonesList = new ArrayList<>();

    public ReportsContactFragment() {
        // Required empty public constructor
    }

    public static ReportsContactFragment newInstance() {
        if (contactFragment == null)
            contactFragment = new ReportsContactFragment();
        return contactFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reports_contact, container, false);
        initView(rootView);
//        initListener();
//        initEmailsAdapter();
//        initPhonesAdapter();
        return rootView;
    }

    private void initView(View rootView) {
        contactRecyclerView = (RecyclerView) rootView.findViewById(R.id.contactRecyclerView);
        contactRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        iniAdapter();
    }

    private void iniAdapter() {
        List<String> list = new ArrayList<>();
        list.add("0");
        list.add("1");
        ContactPageAdapter contactPageAdapter = new ContactPageAdapter(list, activity);
        contactRecyclerView.setAdapter(contactPageAdapter);
    }

//    private void initViewOld(View rootView) {
//        emailEditText = (AppCompatEditText) rootView.findViewById(R.id.emailEditText);
//        phonesEditText = (AppCompatEditText) rootView.findViewById(R.id.phonesEditText);
//
//        notifyCheckBox = (CheckBox) rootView.findViewById(R.id.notifyCheckBox);
//
//        phonesLayout = (LinearLayout) rootView.findViewById(R.id.phonesLayout);
//
//        emailRecyclerView = (RecyclerView) rootView.findViewById(R.id.emailRecyclerView);
//        emailRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
//        phonesRecyclerView = (RecyclerView) rootView.findViewById(R.id.phonesRecyclerView);
//        phonesRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
//    }
//
//    private void initListener() {
//        emailEditText.setOnEditorActionListener((v, actionId, event) -> {
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                if (!emailEditText.getText().toString().isEmpty()) {
//                    addNewEmail(emailEditText.getText().toString().trim());
//                    emailEditText.setText("");
//                }
//                return true;
//            }
//            return false;
//        });
//
//        phonesEditText.setOnEditorActionListener((v, actionId, event) -> {
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                if (!phonesEditText.getText().toString().isEmpty()) {
//                    addNewPhone(phonesEditText.getText().toString().trim());
//                    phonesEditText.setText("");
//                }
//                return true;
//            }
//            return false;
//        });
//
//        notifyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    AnimationUtils.expand(phonesLayout);
//                } else {
//                    AnimationUtils.collapse(phonesLayout);
//                }
//            }
//        });
//    }
//
//    private void initEmailsAdapter() {
//        emailsAdapter = new ReportContactAdapter(activity, emailsList);
//        emailRecyclerView.setAdapter(emailsAdapter);
//    }
//
//    private void addNewEmail(String email) {
//        emailsList.add(email);
//        emailsAdapter.notifyDataSetChanged();
//    }
//
//    private void initPhonesAdapter() {
//        phonesAdapter = new ReportContactAdapter(activity, phonesList);
//        phonesRecyclerView.setAdapter(phonesAdapter);
//    }
//
//    private void addNewPhone(String phone) {
//        phonesList.add(phone);
//        phonesAdapter.notifyDataSetChanged();
//    }
}