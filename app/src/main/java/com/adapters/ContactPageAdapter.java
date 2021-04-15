package com.adapters;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.activities.AddReportsActivity;
import com.utilities.AnimationUtils;
import com.utilities.ToastHelper;
import com.utilities.Utils;
import com.views.TextViewRegular;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saferoad-Dev1 on 8/29/2017.
 */

public class ContactPageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_EMAILS = 0;
    private final int VIEW_EMAILS_ADDITIONAL = 0;
    private final int VIEW_PHONES = 1;

    private FragmentActivity activity;
    private List<String> arrayList;
    private List<String> phonesList = new ArrayList<>();
    private List<String> usersList = new ArrayList<>();
    private List<String> additionalEmailsList = new ArrayList<>();

    public ContactPageAdapter(List<String> arrayList, FragmentActivity activity) {
        this.activity = activity;
        this.arrayList = arrayList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return VIEW_EMAILS_ADDITIONAL;
            case 1:
                return VIEW_PHONES;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View view;
//        if (viewType == VIEW_EMAILS) {
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_emails_view, parent, false);
//            vh = new UsersViewHolder(view);
//            return vh;
//        } else
        if (viewType == VIEW_EMAILS_ADDITIONAL) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_additional_emails_view, parent, false);
            vh = new AdditionalEmailsViewHolder(view);
            return vh;
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_phones_view, parent, false);
            vh = new PhonesViewHolder(view);
            return vh;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//        if (holder instanceof UsersViewHolder) {
//            UsersViewHolder usersViewHolder = ((UsersViewHolder) holder);
//            usersViewHolder.initEmailsAdapter();
//
//            usersViewHolder.addUsersTextView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ListOfUsersDialogFragment listOfUsersDialogFragment = new ListOfUsersDialogFragment();
//                    listOfUsersDialogFragment.show(activity.getSupportFragmentManager(), "");
//                    activity.getSupportFragmentManager().executePendingTransactions();
//                    listOfUsersDialogFragment.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
//                        @Override
//                        public void onDismiss(DialogInterface dialog) {
//                            usersViewHolder.notifyAdapter();
//                        }
//                    });
//                }
//            });
//        } else
        if (holder instanceof AdditionalEmailsViewHolder) {
            AdditionalEmailsViewHolder emailsViewHolder = ((AdditionalEmailsViewHolder) holder);
            emailsViewHolder.initEmailsAdapter();

            emailsViewHolder.addEmailImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    emailsViewHolder.onClickAddEmail();
                }
            });

            emailsViewHolder.desEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
//                    emailsViewHolder.onClickAddDes();
                    AddReportsActivity.wizardModel.setDescription(s.toString());
                }
            });

            emailsViewHolder.emailEditText.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    emailsViewHolder.onClickAddEmail();
                    return true;
                }
                return false;
            });
        } else if (holder instanceof PhonesViewHolder) {
            PhonesViewHolder phonesViewHolder = ((PhonesViewHolder) holder);
            phonesViewHolder.initPhonesAdapter();

            phonesViewHolder.addPhoneImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    phonesViewHolder.onClickAdd();
                }
            });
            phonesViewHolder.phonesEditText.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    phonesViewHolder.onClickAdd();
                    return true;
                }
                return false;
            });

            phonesViewHolder.notifySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        AnimationUtils.expand(phonesViewHolder.phonesLayout);
                    } else {
                        AnimationUtils.collapse(phonesViewHolder.phonesLayout);
                    }
                    AddReportsActivity.wizardModel.setNotifyBySms(isChecked);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class UsersViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView usersRecyclerView;
        private TextViewRegular addUsersTextView;
        private ReportContactAdapter usersAdapter;

        public UsersViewHolder(View itemView) {
            super(itemView);
            addUsersTextView = (TextViewRegular) itemView.findViewById(R.id.addUsersTextView);
            usersRecyclerView = (RecyclerView) itemView.findViewById(R.id.usersRecyclerView);
            usersRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        }

        private void initEmailsAdapter() {
            usersAdapter = new ReportContactAdapter(activity, usersList, false);
            usersRecyclerView.setAdapter(usersAdapter);
        }

//        public void notifyAdapter() {
//            if (Utils.isNotEmptyList(AddReportsActivity.wizardModel.getUsersList())) {
//                for (ListOfUsersModel model : AddReportsActivity.wizardModel.getUsersList()) {
//                    usersList.add(model.getName());
//                }
//                HashSet<String> hashSet = new HashSet<String>();
//                hashSet.addAll(usersList);
//                usersList.clear();
//                usersList.addAll(hashSet);
//                usersAdapter.notifyDataSetChanged();
//            }
//        }
    }

    public class AdditionalEmailsViewHolder extends RecyclerView.ViewHolder {

//        private ImageView addDesImageView;
        private ImageView addEmailImageView;
        private RecyclerView additionalEmailsRecyclerView;
        private AppCompatEditText desEditText;
        private AppCompatEditText emailEditText;
        private ReportContactAdapter additionalEmailsAdapter;

        public AdditionalEmailsViewHolder(View itemView) {
            super(itemView);
//            addDesImageView = (ImageView) itemView.findViewById(R.id.addDesImageView);
            addEmailImageView = (ImageView) itemView.findViewById(R.id.addEmailImageView);
            desEditText = (AppCompatEditText) itemView.findViewById(R.id.desEditText);
            emailEditText = (AppCompatEditText) itemView.findViewById(R.id.emailEditText);
            additionalEmailsRecyclerView = (RecyclerView) itemView.findViewById(R.id.additionalEmailsRecyclerView);
            additionalEmailsRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        }

        private void initEmailsAdapter() {
            additionalEmailsAdapter = new ReportContactAdapter(activity, additionalEmailsList, false);
            additionalEmailsRecyclerView.setAdapter(additionalEmailsAdapter);
        }

        private boolean addNewEmail(String email) {
            if (!additionalEmailsList.contains(email)) {
                additionalEmailsList.add(email);
                AddReportsActivity.wizardModel.addEmail(email);
                additionalEmailsAdapter.notifyDataSetChanged();
                return true;
            } else {
                ToastHelper.toastWarning(activity, activity.getString(R.string.this_email_has_been_added_before));
                return false;
            }
        }

        private void onClickAddDes() {
            if (!desEditText.getText().toString().isEmpty()) {
                AddReportsActivity.wizardModel.setDescription(desEditText.getText().toString().trim());
            } else {
                desEditText.setError(activity.getString(R.string.filed_is_required));
            }
        }

        private void onClickAddEmail() {
            if (!emailEditText.getText().toString().isEmpty()) {
                if (Utils.isEmailValid(emailEditText.getText())) {
                    if (addNewEmail(emailEditText.getText().toString().trim()))
                        emailEditText.setText("");
                } else {
                    ToastHelper.toastWarning(activity, activity.getString(R.string.invalid_email));
                }
            }
        }
    }


    public class PhonesViewHolder extends RecyclerView.ViewHolder {
        private ImageView addPhoneImageView;
        private RecyclerView phonesRecyclerView;
        private AppCompatEditText phonesEditText;
        private Switch notifySwitch;
        private LinearLayout phonesLayout;
        private ReportContactAdapter phonesAdapter;

        PhonesViewHolder(View v) {
            super(v);
            addPhoneImageView = (ImageView) itemView.findViewById(R.id.addPhoneImageView);
            phonesEditText = (AppCompatEditText) itemView.findViewById(R.id.phonesEditText);
            notifySwitch = (Switch) itemView.findViewById(R.id.notifySwitch);
            phonesLayout = (LinearLayout) itemView.findViewById(R.id.phonesLayout);
            phonesRecyclerView = (RecyclerView) itemView.findViewById(R.id.phonesRecyclerView);
            phonesRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        }

        private void initPhonesAdapter() {
            phonesAdapter = new ReportContactAdapter(activity, phonesList, true);
            phonesRecyclerView.setAdapter(phonesAdapter);
        }

        private boolean addNewPhone(String phone) {
            if (!phonesList.contains(phone)) {
                phonesList.add(phone);
                AddReportsActivity.wizardModel.addPhones(phone);
                phonesAdapter.notifyDataSetChanged();
                return true;
            } else {
                ToastHelper.toastWarning(activity, activity.getString(R.string.this_number_has_been_added_before));
                return false;
            }
        }

        private void onClickAdd() {
            if (!phonesEditText.getText().toString().isEmpty()) {
                if (phonesEditText.getText().length() > 7) {
                    if (addNewPhone(phonesEditText.getText().toString().trim())) {
                        phonesEditText.setText("");
                    }
                } else {
                    ToastHelper.toastInfo(activity, activity.getString(R.string.error_phone_number_is_too_short));
                }
            }
        }
    }
}
