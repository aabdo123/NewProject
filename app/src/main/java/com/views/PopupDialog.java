package com.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RotateDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.R;
import com.activities.MainActivity;
import com.fragments.GeoFenceFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by malikabuqauod on 5/18/17.
 */

public class PopupDialog {

    public static final int GRAVITY_TOP = 0;
    public static final int GRAVITY_BOTTOM = 1;
    public static final int GRAVITY_LEFT = 2;
    public static final int GRAVITY_RIGHT = 3;
    private boolean touchOutsideDismiss;
    private int backgroundColor;
    private int[] location;
    private int gravity;
    private Context context;
    private Dialog dialog;
    private View contentView;
    private ImageView ivTriangle;
    private LinearLayout llContent;
    private RelativeLayout rlOutsideBackground;

    private View attachedView = null;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private String vehicleId = "";

    public PopupDialog(Context context) {
        initDialog(context);
    }

    private void initDialog(final Context context) {
        this.context = context;
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.popup_dialog_fab_button, null);
        ViewTreeObserver viewTreeObserver = dialogView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                relocation(location);

            }
        });
        rlOutsideBackground = (RelativeLayout) dialogView.findViewById(R.id.rlOutsideBackground);
        setTouchOutsideDismiss(true);
        ivTriangle = (ImageView) dialogView.findViewById(R.id.ivTriangle);
        llContent = (LinearLayout) dialogView.findViewById(R.id.llContent);
        dialog = new Dialog(context, isFullScreen() ? android.R.style.Theme_Translucent_NoTitleBar_Fullscreen : android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(dialogView);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (onEasyDialogDismissed != null) {
                    onEasyDialogDismissed.onDismissed();
                }
            }
        });
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                if (onEasyDialogShow != null) {
                    onEasyDialogShow.onShow();
                }
            }
        });
        animatorSetForDialogShow = new AnimatorSet();
        animatorSetForDialogDismiss = new AnimatorSet();
        objectAnimatorsForDialogShow = new ArrayList<>();
        objectAnimatorsForDialogDismiss = new ArrayList<>();
        ini();
    }

    final View.OnTouchListener outsideBackgroundListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (touchOutsideDismiss && dialog != null) {
                onDialogDismiss();
            }
            return false;
        }
    };

    public Dialog getDialog() {
        return dialog;
    }

    private void ini() {
        this.setLocation(new int[]{0, 0})
                .setGravity(GRAVITY_BOTTOM)
                .setTouchOutsideDismiss(true)
                .setOutsideColor(Color.TRANSPARENT)
                .setBackgroundColor(Color.BLUE)
                .setMatchParent(true)
                .setMarginLeftAndRight(24, 24);
    }

    public PopupDialog setLayout(View layout) {
        if (layout != null) {
            this.contentView = layout;
        }
        return this;
    }

    public PopupDialog setLayoutResourceId(int layoutResourceId) {
        View view = ((Activity) context).getLayoutInflater().inflate(layoutResourceId, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        setLayout(view);
        return this;
    }

    public PopupDialog setLocation(int[] location) {
        this.location = location;
        return this;
    }

    public PopupDialog setLocationByAttachedView(View attachedView) {
        if (attachedView != null) {
            this.attachedView = attachedView;
            int[] attachedViewLocation = new int[2];
            attachedView.getLocationOnScreen(attachedViewLocation);
            switch (gravity) {
                case GRAVITY_BOTTOM:
                    attachedViewLocation[0] += attachedView.getWidth() / 2;
                    attachedViewLocation[1] += attachedView.getHeight();
                    break;
                case GRAVITY_TOP:
                    attachedViewLocation[0] += attachedView.getWidth() / 2;
                    break;
                case GRAVITY_LEFT:
                    attachedViewLocation[1] += attachedView.getHeight() / 2;
                    break;
                case GRAVITY_RIGHT:
                    attachedViewLocation[0] += attachedView.getWidth();
                    attachedViewLocation[1] += attachedView.getHeight() / 2;
            }
            setLocation(attachedViewLocation);
        }
        return this;
    }

    public View getAttachedView() {
        return this.attachedView;
    }

    public PopupDialog setGravity(int gravity) {
        if (gravity != GRAVITY_BOTTOM && gravity != GRAVITY_TOP && gravity != GRAVITY_LEFT && gravity != GRAVITY_RIGHT) {
            gravity = GRAVITY_BOTTOM;
        }
        this.gravity = gravity;
        switch (this.gravity) {
            case GRAVITY_BOTTOM:
                ivTriangle.setBackgroundResource(R.drawable.triangle_bottom);
                break;
            case GRAVITY_TOP:
                ivTriangle.setBackgroundResource(R.drawable.triangle_top);
                break;
            case GRAVITY_LEFT:
                ivTriangle.setBackgroundResource(R.drawable.triangle_left);
                break;
            case GRAVITY_RIGHT:
                ivTriangle.setBackgroundResource(R.drawable.triangle_right);
                break;
        }
        llContent.setBackgroundResource(R.drawable.round_corner_bg);
        if (attachedView != null) {
            this.setLocationByAttachedView(attachedView);
        }
        this.setBackgroundColor(backgroundColor);
        return this;
    }

    public PopupDialog setMatchParent(boolean matchParent) {
        ViewGroup.LayoutParams layoutParams = llContent.getLayoutParams();
        layoutParams.width = matchParent ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT;
        llContent.setLayoutParams(layoutParams);
        return this;
    }

    public PopupDialog setMarginLeftAndRight(int left, int right) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llContent.getLayoutParams();
        layoutParams.setMargins(left, 0, right, 0);
        llContent.setLayoutParams(layoutParams);
        return this;
    }

    public PopupDialog setMarginTopAndBottom(int top, int bottom) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llContent.getLayoutParams();
        layoutParams.setMargins(0, top, 0, bottom);
        llContent.setLayoutParams(layoutParams);
        return this;
    }

    public PopupDialog setTouchOutsideDismiss(boolean touchOutsideDismiss) {
        this.touchOutsideDismiss = touchOutsideDismiss;
        if (touchOutsideDismiss) {
            rlOutsideBackground.setOnTouchListener(outsideBackgroundListener);
        } else {
            rlOutsideBackground.setOnTouchListener(null);
        }
        return this;
    }

    public PopupDialog setOutsideColor(int color) {
        rlOutsideBackground.setBackgroundColor(color);
        return this;
    }

    public PopupDialog setBackgroundColor(int color) {
        backgroundColor = color;
        LayerDrawable drawableTriangle = (LayerDrawable) ivTriangle.getBackground();
        GradientDrawable shapeTriangle = (GradientDrawable) (((RotateDrawable) drawableTriangle.findDrawableByLayerId(R.id.shape_id)).getDrawable());
        if (shapeTriangle != null) {
            shapeTriangle.setColor(color);
        } else {
            Toast.makeText(context, "shape is null", Toast.LENGTH_SHORT).show();
        }
        GradientDrawable drawableRound = (GradientDrawable) llContent.getBackground();
        if (drawableRound != null) {
            drawableRound.setColor(color);
        }
        return this;
    }


    public PopupDialog show(String vehicleID, double latitude, double longitude) {
        if (dialog != null) {
            if (contentView == null) {
                throw new RuntimeException("setLayout(), setLayoutResourceId()？");
            }
            if (llContent.getChildCount() > 0) {
                llContent.removeAllViews();
            }
            this.vehicleId = vehicleID;
            MyAdapterMain myAdapter = new MyAdapterMain(getMenuType(), latitude, longitude);
            layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            recyclerView.setAdapter(myAdapter);
            recyclerView.setLayoutManager(layoutManager);

            llContent.addView(contentView);
            try {
                dialog.show();
                onDialogShowing();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    private class MyAdapterMain extends RecyclerView.Adapter<MyAdapterMain.ViewHolder> {

        private List<Item> itemsAdapter = new ArrayList<>();
        private double latitude;
        private double longitude;

        MyAdapterMain(List<Item> menuType, double latitude, double longitude) {
            this.itemsAdapter = menuType;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_popup_item, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final Item item = itemsAdapter.get(holder.getAdapterPosition());

            if (position == itemsAdapter.size() - 1) {
                holder.divider.setVisibility(View.INVISIBLE);
            }
            holder.labelTextView.setText(item.name);
            holder.pictureImageView.setImageDrawable(item.drawable);
            holder.itemView.setOnClickListener(v -> {
                if (item.id == R.string.geo_fence) {
                    ((MainActivity) context).call(GeoFenceFragment.newInstance(vehicleId, null, latitude, longitude), context.getString(R.string.geo_fence));
                } else {
                    ((MainActivity) context).displayView(item.id, item.name, vehicleId);
                }
                dialog.dismiss();
            });
        }

        @Override
        public int getItemCount() {
            return itemsAdapter.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout menuItemLinearLayout;
            ImageView pictureImageView;
            TextViewRegular labelTextView;
            View divider;

            private ViewHolder(View v) {
                super(v);
                menuItemLinearLayout = (LinearLayout) v.findViewById(R.id.menuItemLinearLayout);
                pictureImageView = (ImageView) v.findViewById(R.id.pictureImageView);
                labelTextView = (TextViewRegular) v.findViewById(R.id.labelTextView);
                divider = v.findViewById(R.id.divider);
            }
        }
    }

    public View getTipViewInstance() {
        return rlOutsideBackground.findViewById(R.id.rlParentForAnimate);
    }

    public static final int DIRECTION_X = 0;

    public static final int DIRECTION_Y = 1;

    public PopupDialog setAnimationTranslationShow(int direction, int duration, float... values) {
        return setAnimationTranslation(true, direction, duration, values);
    }

    public PopupDialog setAnimationTranslationDismiss(int direction, int duration, float... values) {
        return setAnimationTranslation(false, direction, duration, values);
    }

    private PopupDialog setAnimationTranslation(boolean isShow, int direction, int duration, float... values) {
        if (direction != DIRECTION_X && direction != DIRECTION_Y) {
            direction = DIRECTION_X;
        }
        String propertyName = "";
        switch (direction) {
            case DIRECTION_X:
                propertyName = "translationX";
                break;
            case DIRECTION_Y:
                propertyName = "translationY";
                break;
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(rlOutsideBackground.findViewById(R.id.rlParentForAnimate), propertyName, values)
                .setDuration(duration);
        if (isShow) {
            objectAnimatorsForDialogShow.add(animator);
        } else {
            objectAnimatorsForDialogDismiss.add(animator);
        }
        return this;
    }

    public PopupDialog setAnimationAlphaShow(int duration, float... values) {
        return setAnimationAlpha(true, duration, values);
    }

    public PopupDialog setAnimationAlphaDismiss(int duration, float... values) {
        return setAnimationAlpha(false, duration, values);
    }

    private PopupDialog setAnimationAlpha(boolean isShow, int duration, float... values) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(rlOutsideBackground.findViewById(R.id.rlParentForAnimate), "alpha", values).setDuration(duration);
        if (isShow) {
            objectAnimatorsForDialogShow.add(animator);
        } else {
            objectAnimatorsForDialogDismiss.add(animator);
        }
        return this;
    }

    private AnimatorSet animatorSetForDialogShow;
    private AnimatorSet animatorSetForDialogDismiss;
    private List<Animator> objectAnimatorsForDialogShow;
    private List<Animator> objectAnimatorsForDialogDismiss;

    private void onDialogShowing() {
        if (animatorSetForDialogShow != null && objectAnimatorsForDialogShow != null && objectAnimatorsForDialogShow.size() > 0) {
            animatorSetForDialogShow.playTogether(objectAnimatorsForDialogShow);
            animatorSetForDialogShow.start();
        }
    }

    @SuppressLint("NewApi")
    private void onDialogDismiss() {
        if (animatorSetForDialogDismiss.isRunning()) {
            return;
        }
        if (animatorSetForDialogDismiss != null && objectAnimatorsForDialogDismiss != null && objectAnimatorsForDialogDismiss.size() > 0) {
            animatorSetForDialogDismiss.playTogether(objectAnimatorsForDialogDismiss);
            animatorSetForDialogDismiss.start();
            animatorSetForDialogDismiss.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (context != null && context instanceof Activity) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            if (!((Activity) context).isDestroyed()) {
                                dialog.dismiss();
                            }
                        } else {
                            try {
                                dialog.dismiss();
                            } catch (final IllegalArgumentException e) {

                            } catch (final Exception e) {
                                e.printStackTrace();
                            } finally {
                                dialog = null;
                            }
                        }
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        } else {
            dialog.dismiss();
        }
    }


    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            onDialogDismiss();
        }
    }


    private void relocation(int[] location) {
        float statusBarHeight = isFullScreen() ? 0.0f : getStatusBarHeight();

        ivTriangle.setX(location[0] - ivTriangle.getWidth() / 2);
        ivTriangle.setY(location[1] - ivTriangle.getHeight() / 2 - statusBarHeight);
        switch (gravity) {
            case GRAVITY_BOTTOM:
                llContent.setY(location[1] - ivTriangle.getHeight() / 2 - statusBarHeight + ivTriangle.getHeight());
                break;
            case GRAVITY_TOP:
                llContent.setY(location[1] - llContent.getHeight() - statusBarHeight - ivTriangle.getHeight() / 2);
                break;
            case GRAVITY_LEFT:
                llContent.setX(location[0] - llContent.getWidth() - ivTriangle.getWidth() / 2);
                break;
            case GRAVITY_RIGHT:
                llContent.setX(location[0] + ivTriangle.getWidth() / 2);
                break;
        }

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llContent.getLayoutParams();
        switch (gravity) {
            case GRAVITY_BOTTOM:
            case GRAVITY_TOP:
                int triangleCenterX = (int) (ivTriangle.getX() + ivTriangle.getWidth() / 2);
                int contentWidth = llContent.getWidth();
                int rightMargin = getScreenWidth() - triangleCenterX;
                int leftMargin = getScreenWidth() - rightMargin;
                int availableLeftMargin = leftMargin - layoutParams.leftMargin;
                int availableRightMargin = rightMargin - layoutParams.rightMargin;
                int x = 0;
                if (contentWidth / 2 <= availableLeftMargin && contentWidth / 2 <= availableRightMargin) {
                    x = triangleCenterX - contentWidth / 2;
                } else {
                    if (availableLeftMargin <= availableRightMargin) {
                        x = layoutParams.leftMargin;
                    } else {
                        x = getScreenWidth() - (contentWidth + layoutParams.rightMargin);
                    }
                }
                llContent.setX(x);
                break;
            case GRAVITY_LEFT:
            case GRAVITY_RIGHT:
                int triangleCenterY = (int) (ivTriangle.getY() + ivTriangle.getHeight() / 2);
                int contentHeight = llContent.getHeight();
                int topMargin = triangleCenterY;
                int bottomMargin = getScreenHeight() - topMargin;
                int availableTopMargin = topMargin - layoutParams.topMargin;
                int availableBottomMargin = bottomMargin - layoutParams.bottomMargin;
                int y = 0;
                if (contentHeight / 2 <= availableTopMargin && contentHeight / 2 <= availableBottomMargin) {
                    y = triangleCenterY - contentHeight / 2;
                } else {
                    if (availableTopMargin <= availableBottomMargin) {
                        y = layoutParams.topMargin;
                    } else {
                        y = getScreenHeight() - (contentHeight + layoutParams.topMargin);
                    }
                }
                llContent.setY(y);
                break;
        }
    }


    private int getScreenWidth() {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    private int getScreenHeight() {
        int statusBarHeight = isFullScreen() ? 0 : getStatusBarHeight();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.heightPixels - statusBarHeight;
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public boolean isFullScreen() {
        int flg = ((Activity) context).getWindow().getAttributes().flags;
        boolean flag = false;
        if ((flg & 1024) == 1024) {
            flag = true;
        }
        return flag;
    }

    public PopupDialog setCancelable(boolean cancelable) {
        dialog.setCancelable(cancelable);
        return this;
    }

    private PopupDialog.OnEasyDialogDismissed onEasyDialogDismissed;

    public PopupDialog setOnEasyDialogDismissed(PopupDialog.OnEasyDialogDismissed onEasyDialogDismissed) {
        this.onEasyDialogDismissed = onEasyDialogDismissed;
        return this;
    }

    /**
     * Dialog is dismissed
     */
    public interface OnEasyDialogDismissed {
        public void onDismissed();
    }

    private PopupDialog.OnEasyDialogShow onEasyDialogShow;

    public PopupDialog setOnEasyDialogShow(PopupDialog.OnEasyDialogShow onEasyDialogShow) {
        this.onEasyDialogShow = onEasyDialogShow;
        return this;
    }

    /**
     * Dialog is showing
     */
    public interface OnEasyDialogShow {
        public void onShow();
    }

    private class Item {
        final int id;
        final String name;
        Drawable drawable;

        Item(int id, String name, Drawable drawableId) {
            this.id = id;
            this.name = name;
            if (drawable != null) {
                drawable = null;
            }
            this.drawable = drawableId;
        }
    }

    private List<Item> getMenuType() {
        List<Item> items = new ArrayList<>();
        items.add(new Item(R.string.geo_fence, context.getString(R.string.geo_fence), ContextCompat.getDrawable(context, R.drawable.geo_fence)));
        items.add(new Item(R.string.historical_route_playback, context.getString(R.string.historical_route_playback), ContextCompat.getDrawable(context, R.drawable.historical)));
        items.add(new Item(R.string.alarm_notification, context.getString(R.string.alarm_notification), ContextCompat.getDrawable(context, R.drawable.ic_alarm_primary)));
        items.add(new Item(R.string.data_analysis, context.getString(R.string.data_analysis), ContextCompat.getDrawable(context, R.drawable.ic_placeholder_data_analysis)));
        return items;
    }
}