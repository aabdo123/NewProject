package com.adapters;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.R;
import com.google.android.gms.maps.model.LatLng;
import com.models.LatLogModel;
import com.utilities.map.MapUtils;
import com.views.TextViewLight;
import com.views.TextViewRegular;

import java.util.ArrayList;


public class SelectedLocationsAdapter extends RecyclerView.Adapter<SelectedLocationsAdapter.ViewHolder> {
    public static final int START_HIDDEN = 0;
    public static final int START_OPENED = 1;
    public static final int END_HIDDEN = 2;
    public static final int END_OPENED = 4;
    private ArrayList<LatLogModel> mainItemDataObject;
    private Context context;

    public SelectedLocationsAdapter(Context context, ArrayList<LatLogModel> mainItemDataObject) {
        this.context = context;
        this.mainItemDataObject = mainItemDataObject;
    }


    @NonNull
    @Override
    public SelectedLocationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == START_HIDDEN) {
            final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_start_hidden, parent, false);
            return new SlideViewHolder(v);
        } else if (viewType == START_OPENED) {
            final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_start_opened, parent, false);
            return new SmartMatjarHolder(v);
        } else if (viewType == END_HIDDEN) {
            final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_end_hidden, parent, false);
            return new PrimeViewHolder(v);
        } else {
            final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_end_opened, parent, false);
            return new OpenEndViewHolder(v);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull final SelectedLocationsAdapter.ViewHolder holder, int position) {
        try {
            if (holder.getItemViewType() == START_HIDDEN) {
                final SlideViewHolder slide = (SlideViewHolder) holder;
                try {
                    if (mainItemDataObject.get(slide.getAdapterPosition()) != null) {
                        LatLng latLng = mainItemDataObject.get(slide.getAdapterPosition()).getLatLng();
                        if (latLng != null) {
                            String[] arr = MapUtils.getAddressByLatLng(context, latLng);
                            if (arr != null && arr.length > 0 && arr[0] != null && arr[3] != null) {
                                slide.goTextView.setText(arr[0]);
                                slide.streetTextView.setText(arr[3]);
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (holder.getItemViewType() == START_OPENED) {
                final SmartMatjarHolder slide = (SmartMatjarHolder) holder;
                try {
                    if (mainItemDataObject.get(slide.getAdapterPosition()) != null) {
                        LatLng latLng = mainItemDataObject.get(slide.getAdapterPosition()).getLatLng();
                        if (latLng != null) {
                            String[] arr = MapUtils.getAddressByLatLng(context, latLng);
                            if (arr != null && arr.length > 0 && arr[0] != null && arr[3] != null) {
                                slide.goTextView.setText(arr[0]);
                                slide.streetTextView.setText(arr[3]);
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (holder.getItemViewType() == END_HIDDEN) {
                final PrimeViewHolder slide = (PrimeViewHolder) holder;
                try {
                    if (mainItemDataObject.get(slide.getAdapterPosition()) != null) {
                        LatLng latLng = mainItemDataObject.get(slide.getAdapterPosition()).getLatLng();
                        if (latLng != null) {
                            String[] arr = MapUtils.getAddressByLatLng(context, latLng);
                            if (arr != null && arr.length > 0 && arr[0] != null && arr[3] != null) {
                                slide.goTextView.setText(arr[0]);
                                slide.streetTextView.setText(arr[3]);
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (holder.getItemViewType() == END_OPENED) {
                final OpenEndViewHolder slide = (OpenEndViewHolder) holder;
                try {
                    if (mainItemDataObject.get(slide.getAdapterPosition()) != null) {
                        LatLng latLng = mainItemDataObject.get(slide.getAdapterPosition()).getLatLng();
                        if (latLng != null) {
                            String[] arr = MapUtils.getAddressByLatLng(context, latLng);
                            if (arr != null && arr.length > 0 && arr[0] != null && arr[3] != null) {
                                slide.goTextView.setText(arr[0]);
                                slide.streetTextView.setText(arr[3]);
                            }
                        }
                        if (mainItemDataObject != null && mainItemDataObject.get(slide.getAdapterPosition()).getLocationLocateModel() != null) {
                            slide.distanceTextView.setText((mainItemDataObject.get(slide.getAdapterPosition()).getLocationLocateModel().getDistance()));
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
    }


    @Override
    public int getItemViewType(int position) {
        return mainItemDataObject.get(position).getItemViewType();
    }


    @Override
    public int getItemCount() {
        return mainItemDataObject.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class SlideViewHolder extends ViewHolder {
        TextViewRegular goTextView;
        TextViewLight streetTextView;
        TextViewLight distanceTextView;

        private SlideViewHolder(View view) {
            super(view);
            goTextView = (TextViewRegular) view.findViewById(R.id.goTextView);
            streetTextView = (TextViewLight) view.findViewById(R.id.streetTextView);
            distanceTextView = (TextViewLight) view.findViewById(R.id.distanceTextView);
        }
    }

    public static class SmartMatjarHolder extends ViewHolder {
        TextViewRegular goTextView;
        TextViewLight streetTextView;
        TextViewLight distanceTextView;

        private SmartMatjarHolder(View view) {
            super(view);
            goTextView = (TextViewRegular) view.findViewById(R.id.goTextView);
            streetTextView = (TextViewLight) view.findViewById(R.id.streetTextView);
            distanceTextView = (TextViewLight) view.findViewById(R.id.distanceTextView);
        }
    }

    public static class PrimeViewHolder extends ViewHolder {
        TextViewRegular goTextView;
        TextViewLight streetTextView;
        TextViewLight distanceTextView;

        private PrimeViewHolder(View view) {
            super(view);
            goTextView = (TextViewRegular) view.findViewById(R.id.goTextView);
            streetTextView = (TextViewLight) view.findViewById(R.id.streetTextView);
            distanceTextView = (TextViewLight) view.findViewById(R.id.distanceTextView);
        }
    }


    public static class OpenEndViewHolder extends ViewHolder {
        TextViewRegular goTextView;
        TextViewLight streetTextView;
        TextViewLight distanceTextView;

        private OpenEndViewHolder(View view) {
            super(view);
            goTextView = (TextViewRegular) view.findViewById(R.id.goTextView);
            streetTextView = (TextViewLight) view.findViewById(R.id.streetTextView);
            distanceTextView = (TextViewLight) view.findViewById(R.id.distanceTextView);
        }
    }


}

