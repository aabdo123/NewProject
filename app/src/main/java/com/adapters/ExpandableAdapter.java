package com.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.R;
import com.models.Item;
import com.multilevelview.MultiLevelAdapter;
import com.multilevelview.MultiLevelRecyclerView;
import com.utilities.AppUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ExpandableAdapter extends MultiLevelAdapter {

    private Holder mViewHolder;
    private Context mContext;
    private List<Item> mListItems = new ArrayList<>();
    private Item mItem;
    private MultiLevelRecyclerView mMultiLevelRecyclerView;
    private ActionsInterface actionsInterface;
    private int mainOfPosition;

    public ExpandableAdapter(Context mContext, List<Item> mListItems, MultiLevelRecyclerView mMultiLevelRecyclerView, ActionsInterface actionsInterface) {
        super(mListItems);
        this.mListItems = mListItems;
        this.mContext = mContext;
        this.mMultiLevelRecyclerView = mMultiLevelRecyclerView;
        this.actionsInterface = actionsInterface;
    }

    private void setExpandButton(ImageView expandButton, boolean isExpanded) {
        // set the icon based on the current state
        expandButton.setImageResource(isExpanded ? R.drawable.ic_keyboard_arrow_up_black : R.drawable.ic_keyboard_arrow_down_black);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_expandable, parent, false));
    }

    public interface ActionsInterface {
        void ItemClicked(Item item, int position, boolean checkedState, String clickedState);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        mViewHolder = (Holder) holder;
        mItem = mListItems.get(position);
        try {
            if (mItem.getID() != null) {
                mViewHolder.carImageView.setVisibility(View.GONE);
            } else {
                mViewHolder.carImageView.setVisibility(View.VISIBLE);
                if (mItem.getVehicleStatus() != null) {
                    mViewHolder.carImageView.setBackground(AppUtils.getCarIconDrawable(mContext, mItem.getVehicleStatus()));
                }
            }
            if (mItem.hasChildren() && mItem.getChildren().size() > 0) {
//                setExpandButton(mViewHolder.mExpandIcon, mItem.isExpanded());
                mViewHolder.mExpandButton.setVisibility(View.VISIBLE);
            } else {
                mViewHolder.mExpandButton.setVisibility(View.GONE);
            }
            mViewHolder.mTitle.setText(String.format(Locale.getDefault(), "%s", mItem.getName() != null ? mItem.getName() : mItem.getVehicleDisplayName() != null ? mItem.getVehicleDisplayName() : ""));
//            mViewHolder.mSubtitle.setText(String.format(Locale.getDefault(), "%s", mItem.getID() != null ? mItem.getID() : mItem.getVehicleID() > 0 ? mItem.getVehicleID() : ""));

            if (mItem.isClicked()) {
                mViewHolder.mExpandIcon.setRotation(-180);
            } else {
                mViewHolder.mExpandIcon.setRotation(0);
            }

            if (mItem.isChecked()) {
                mViewHolder.checkBoxCheckBox.setChecked(true);
            } else {
                mViewHolder.checkBoxCheckBox.setChecked(false);
            }



//            Item item = mListItems.get(position);
//            switch (item.getLevel()) {
//                case 1:
//                    mViewHolder.mainItemViewLinearLayout.setBackgroundColor(Color.parseColor("#efefef"));
//                    break;
//                case 2:
//                    mViewHolder.mainItemViewLinearLayout.setBackgroundColor(Color.parseColor("#dedede"));
//                    break;
//                default:
//                    mViewHolder.mainItemViewLinearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
//                    break;
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

//        switch (getItemViewType(position)) {
//            case 1:
//                holder.itemView.setBackgroundColor(Color.parseColor("#efefef"));
//                break;
//            case 2:
//                holder.itemView.setBackgroundColor(Color.parseColor("#dedede"));
//                break;
//            default:
//                holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
//                break;
//        }

//        mViewHolder.checkBoxCheckBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
////                    if (!mItem.isChecked()) {
////                        mItem.setChecked(false);
////                    } else {
////                        mItem.setChecked(true);
////                    }
////                    mMultiLevelRecyclerView.toggleItemsGroup(position);
////                    notifyDataSetChanged();
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//        });

//        mViewHolder.mExpandButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//
////                    if (mItem.isChecked()) {
////                        mItem.setChecked(false);
////                    }
//                      mMultiLevelRecyclerView.toggleItemsGroup(position);
//        mViewHolder.mExpandIcon.animate().rotation(mListItems.get(holder.getAdapterPosition()).isChecked() ? -180 : 0).start();
//                    //  Toast.makeText(mContext, String.format(Locale.ENGLISH, "Item at position %d is expanded: %s", position, mItem.isExpanded()), Toast.LENGTH_SHORT).show();
//                    // notifyItemChanged(position);
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//        });


//        Log.e("MuditLog", mItem.getLevel() + " " + mItem.getPosition() + " " + mItem.isExpanded() + "");

        // indent child items
        // Note: the parent item should start at zero to have no indentation
        // e.g. in populateFakeData(); the very first Item shold be instantiate like this: Item item = new Item(0);
//        float density = mContext.getResources().getDisplayMetrics().density;
//        ((ViewGroup.MarginLayoutParams) mViewHolder.mTextBox.getLayoutParams()).leftMargin = (int) ((getItemViewType(position) * 20) * density + 0.5f);
    }

    private class Holder extends RecyclerView.ViewHolder {

        private TextView mTitle, mSubtitle;
        private ImageView mExpandIcon;
        private LinearLayout mTextBox, mExpandButton;
        private CheckBox checkBoxCheckBox;
        private ImageView carImageView;
        private LinearLayout mainItemViewLinearLayout;

        Holder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mSubtitle = (TextView) itemView.findViewById(R.id.subtitle);
            mExpandIcon = (ImageView) itemView.findViewById(R.id.image_view);
            mTextBox = (LinearLayout) itemView.findViewById(R.id.text_box);
            mExpandButton = (LinearLayout) itemView.findViewById(R.id.expand_field);
            checkBoxCheckBox = (CheckBox) itemView.findViewById(R.id.checkBoxCheckBox);
            carImageView = (ImageView) itemView.findViewById(R.id.carImageView);
            mainItemViewLinearLayout = (LinearLayout) itemView.findViewById(R.id.mainItemViewLinearLayout);
            mTextBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mExpandButton.performClick();
                }
            });
            mExpandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int position = getAdapterPosition();
                        Item item = mListItems.get(position);
                        if (!item.isClicked()) {
                            item.setClicked(true);
                            actionsInterface.ItemClicked(item, position, item.isChecked(), "click");
                            mExpandIcon.animate().rotation(-180).start();
                        } else {
                            item.setClicked(false);
                            mExpandIcon.animate().rotation(0).start();
                            mMultiLevelRecyclerView.toggleItemsGroup(position);
                        }
//                        mMultiLevelRecyclerView.toggleItemsGroup(position);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            checkBoxCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int position = getAdapterPosition();
                        Item item = mListItems.get(position);
                        item.setChecked(!item.isChecked());
                        if (!item.isClicked()) {
                            item.setClicked(true);
                            actionsInterface.ItemClicked(item, position, item.isChecked(), "checked");
                            mExpandIcon.animate().rotation(-180).start();
                        } else {
                            item.setClicked(false);
                            mExpandIcon.animate().rotation(0).start();
                            mMultiLevelRecyclerView.toggleItemsGroup(position);
                        }
//                        mMultiLevelRecyclerView.toggleItemsGroup(position);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
    }
}
