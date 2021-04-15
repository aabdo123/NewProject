package com.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.models.NavDrawerModel;
import com.views.TextViewRegular;

import java.util.Collections;
import java.util.List;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {

    List<NavDrawerModel> data = Collections.emptyList();
    private LayoutInflater inflater;

    public NavigationDrawerAdapter(Context context, List<NavDrawerModel> data) {
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavDrawerModel current = data.get(position);
        holder.title.setText(current.getTitle());
        holder.image.setImageResource(current.getImage());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextViewRegular title;
        private ImageView image;
//        private RelativeLayout rowRelativeLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextViewRegular) itemView.findViewById(R.id.rowTitle);
            image = (ImageView) itemView.findViewById(R.id.rowImage);
//            rowRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.rowRelativeLayout);
        }
    }
}
