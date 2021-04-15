package com.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.activities.MainActivity;
import com.adapters.NavigationDrawerAdapter;
import com.managers.PreferencesManager;
import com.models.NavDrawerModel;
import com.utilities.Utils;
import com.utilities.constants.SharesPrefConstants;
import com.views.ButtonBold;

import java.util.ArrayList;
import java.util.List;

public class FragmentDrawer extends Fragment {

    private Activity activity;
    private static String[] titles = null;
    private static int[] images = {
            R.drawable.sidebar_home,
//            R.drawable.sidebar_user,
            R.drawable.sidebar_map_of_vehicles,
            R.drawable.sidebar_car_list,
            R.drawable.ic_placeholder,
            R.drawable.sidebar_landmark,
            R.drawable.sidebar_alarm,
            R.drawable.sidebar_help,
            R.drawable.sidebar_about,
            R.drawable.sidebar_exit
    };
    private static int[] titleIds = {
            R.string.nav_home,
//            R.string.nav_user_management,
            R.string.nav_map,
            R.string.nav_list_of_vehicles,
            R.string.nav_geo_fence_list,
            R.string.nav_landmark_list,
            R.string.nav_alarm_notification,
            R.string.nav_help,
            R.string.nav_about,
            R.string.nav_logout
    };

    public ActionBarDrawerToggle mDrawerToggle;
    private RecyclerView recyclerView;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter adapter;
    private View containerView;
    private FragmentDrawerListener drawerListener;
    private TextView userNameTextView;
    private ButtonBold changeLanguageButton;

    public FragmentDrawer() {
    }

    public static List<NavDrawerModel> getData() {
        List<NavDrawerModel> data = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            NavDrawerModel navItem = new NavDrawerModel();
            navItem.setTitle(titles[i]);
            navItem.setImage(images[i]);
            navItem.setTitleId(titleIds[i]);
            data.add(navItem);
        }
        return data;
    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        titles = activity.getResources().getStringArray(R.array.nav_drawer_labels);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        setUpViews(view);
        return view;
    }


    protected void setUpViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.drawerList);
        userNameTextView = (TextView) view.findViewById(R.id.userNameTextView);
        changeLanguageButton = (ButtonBold) view.findViewById(R.id.changeLanguageButton);
        userNameTextView.setText("" + PreferencesManager.getInstance().getStringValue(SharesPrefConstants.USERNAME));
        adapter = new NavigationDrawerAdapter(activity, getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(activity, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                drawerListener.onDrawerItemSelected(view, titleIds[position]);
                mDrawerLayout.closeDrawer(containerView);
            }


            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        changeLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) activity).showChangeLanguageDialog();
            }
        });
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = activity.findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                activity.invalidateOptionsMenu();
                Utils.hidKeyBoard(activity);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                activity.invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public interface FragmentDrawerListener {
        void onDrawerItemSelected(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }
}
