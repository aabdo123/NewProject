package com.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.R;
import com.models.Item;
import com.multilevelview.MultiLevelAdapter;
import com.multilevelview.MultiLevelRecyclerView;
import com.utilities.AppUtils;
import com.utilities.Utils;

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
                String firstOne = mItem.getID().substring(0, 1);
                if (firstOne.equalsIgnoreCase("G")) {
                    mViewHolder.carImageView.setVisibility(View.GONE);
                    mViewHolder.mainWidth.setVisibility(View.GONE);
                } else {
                    mViewHolder.carImageView.setVisibility(View.VISIBLE);
                    mViewHolder.mainWidth.setVisibility(View.VISIBLE);
                    if (mItem.getVehicleStatus() != null) {
                        mViewHolder.carImageView.setBackground(AppUtils.getCarIconDrawable(mContext, mItem.getVehicleStatus()));
                    }
                }
            }

//            else {
//                mViewHolder.carImageView.setVisibility(View.VISIBLE);
//                if (mItem.getVehicleStatus() != null) {
//                    mViewHolder.carImageView.setBackground(AppUtils.getCarIconDrawable(mContext, mItem.getVehicleStatus()));
//                }
//            }
            if (mItem.hasChildren() && mItem.getChildren().size() > 0) {
//                setExpandButton(mViewHolder.mExpandIcon, mItem.isExpanded());
                mViewHolder.mExpandButton.setVisibility(View.VISIBLE);
                mViewHolder.mSubtitle.setVisibility(View.VISIBLE);
                mViewHolder.mSubtitle.setText(String.format(Locale.getDefault(), "%s %s", mItem.getChildren().size(), mContext.getResources().getString(R.string.vehicles)));
            } else {
                mViewHolder.mExpandButton.setVisibility(View.GONE);
                mViewHolder.mSubtitle.setVisibility(View.GONE);
            }
            mViewHolder.mTitle.setText(String.format(Locale.getDefault(), "%s", mItem.getName() != null ? mItem.getName() : mItem.getVehicleDisplayName() != null ? mItem.getVehicleDisplayName() : ""));


//
//            if (mItem.isClicked()) {
//                mViewHolder.mExpandIcon.setRotation(-180);
//            } else {
//                mViewHolder.mExpandIcon.setRotation(0);
//            }


            if (mItem.isClicked()) {
                mViewHolder.mExpandIcon.setBackground(mContext.getResources().getDrawable(R.drawable.group_collapse));
            } else {
                mViewHolder.mExpandIcon.setBackground(mContext.getResources().getDrawable(R.drawable.group_expand));
            }
//            if (mItem.isGroupChecked()) {
//                mViewHolder.checkBoxCheckBox.setButtonDrawable(mContext.getResources().getDrawable(R.drawable.ic_semi_hecked));
//            } else {
//                mViewHolder.checkBoxCheckBox.setButtonDrawable(mContext.getResources().getDrawable(R.drawable.check_box_state));
//            }

            if (mItem.isChecked()) {
                mViewHolder.checkBoxCheckBox.setChecked(true);
            } else {
                mViewHolder.checkBoxCheckBox.setChecked(false);
            }

            if (position == 0) {
                ((ViewGroup.MarginLayoutParams) mViewHolder.mExpandButton.getLayoutParams()).leftMargin = Utils.pxToDp(5);
            } else {
                ((ViewGroup.MarginLayoutParams) mViewHolder.mExpandButton.getLayoutParams()).leftMargin = Utils.pxToDp(20);
            }
//            Log.e("s","s");

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

    }

    private class Holder extends RecyclerView.ViewHolder {

        private TextView mTitle, mSubtitle;
        private ImageView mExpandIcon;
        private LinearLayout mTextBox, mExpandButton;
        private CheckBox checkBoxCheckBox;
        private ImageView carImageView;
        private LinearLayout mainItemViewLinearLayout;
        private View mainWidth;

        Holder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mainWidth = (View) itemView.findViewById(R.id.mainWidth);
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
                    checkBoxCheckBox.performClick();
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
//                            actionsInterface.ItemClicked(item, position, item.isChecked(), "click");
                            mMultiLevelRecyclerView.toggleItemsGroup(position);
                            mExpandIcon.setBackground(mContext.getResources().getDrawable(R.drawable.group_collapse));
                        } else {
                            item.setClicked(false);
                            mExpandIcon.setBackground(mContext.getResources().getDrawable(R.drawable.group_expand));
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
                        String state = "vehicle";
                        int position = getAdapterPosition();
                        Item item = mListItems.get(position);
                        item.setChecked(!item.isChecked());
                        if (item.getID() != null) {
                            String firstOne = item.getID().substring(0, 1);
                            if (firstOne.equalsIgnoreCase("G")) {
                                state = "grope";
                            } else {
                                state = "vehicle";
                            }
                        }
                        actionsInterface.ItemClicked(item, position, item.isChecked(), state);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
    }
}
