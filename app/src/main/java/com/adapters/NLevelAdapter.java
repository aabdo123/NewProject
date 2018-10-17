package com.adapters;

/**
 * Created by sadra on 7/29/17.
 */


import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.models.NLevelItemModel;
import com.views.NLevelListItem;

import java.util.ArrayList;
import java.util.List;

public class NLevelAdapter extends BaseAdapter {

    private List<NLevelItemModel> list;
    private List<NLevelListItem> filtered;

    private void setFiltered(ArrayList<NLevelListItem> filtered) {
        this.filtered = filtered;

    }

    public NLevelAdapter(List<NLevelItemModel> list) {
        this.list = list;
        this.filtered = filterItems();
    }

    @Override
    public int getCount() {
        return filtered.size();
    }

    @Override
    public NLevelListItem getItem(int arg0) {
        return filtered.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {

        return getItem(arg0).getView();
    }

    public NLevelFilter getFilter() {
        return new NLevelFilter();
    }

    public class NLevelFilter {

        public void filter() {
            new AsyncFilter().execute();
        }


        class AsyncFilter extends AsyncTask<Void, Void, ArrayList<NLevelListItem>> {

            @Override
            protected ArrayList<NLevelListItem> doInBackground(Void... arg0) {

                return (ArrayList<NLevelListItem>) filterItems();
            }

            @Override
            protected void onPostExecute(ArrayList<NLevelListItem> result) {
                setFiltered(result);
                NLevelAdapter.this.notifyDataSetChanged();
            }
        }


    }


    //    private List<NLevelListItem> filterItems() {
//        List<NLevelListItem> tempfiltered = new ArrayList<NLevelListItem>();
//        OUTER:for (int x = 0; x < list.size(); x++) {
//            //add expanded items and top level items
//            //if parent is null then its a top level item
//            if (list.get(x).getParent() == null) {
//                tempfiltered.add(list.get(x));
//            } else {
//                //go through each ancestor to make sure they are all expanded
//                NLevelListItem parent = list.get(x);
//                while ((parent = parent.getParent()) != null) {
//                    if (!parent.isExpanded()) {
//                        //one parent was not expanded
//                        //skip the rest and continue the OUTER for loop
//                        continue OUTER;
//                    }
//                }
//                tempfiltered.add(list.get(x));
//            }
//        }
//
//        return tempfiltered;
//    }
    private List<NLevelListItem> filterItems() {
        List<NLevelListItem> tempfiltered = new ArrayList<NLevelListItem>();
        OUTER:
        for (int x = 0; x < list.size(); x++) {
            //add expanded items and top level items
            //if parent is null then its a top level item
            if (list.get(x).getParent() == null) {
                tempfiltered.add(list.get(x));
                boolean state = list.get(x).isExpanded();
                Log.e("TAG", "TAGS");
            } else {
                //go through each ancestor to make sure they are all expanded
                NLevelListItem parent = list.get(x);
                while ((parent = parent.getParent()) != null) {
                    if (!parent.isExpanded()) {
                        //one parent was not expanded
                        //skip the rest and continue the OUTER for loop
                        list.get(x).makeItState(parent.isExpanded(), parent.isExpanded());
                        continue OUTER;
                    }else {
                        list.get(x).makeItState(true, true);
                    }
                }
                tempfiltered.add(list.get(x));
            }
        }

        return tempfiltered;
    }

    public void toggle(int arg2) {
        filtered.get(arg2).toggle();
    }
}
