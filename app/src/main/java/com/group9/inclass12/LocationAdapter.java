package com.group9.inclass12;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    ArrayList<Location> myData;

    private int tripIndex;

    MainActivity mainActivity;

    public LocationAdapter(ArrayList<Location> myData, int tripIndex) {
        this.myData = myData;
        this.tripIndex = tripIndex;
    }

    @Override
    public int getItemViewType(int position) {
        //altered the getItemViewType to return the position of the overall view
        //to have it as an index in the onCreateViewHolder
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final int index = i;

        final View view  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.location_item, viewGroup, false);

        mainActivity = (MainActivity) view.getContext();

        //get all inner views
        TextView locationNameTextView = view.findViewById(R.id.location_name_in_item_list_textView);
        ImageView deleteImageViewButton = view.findViewById(R.id.delete_location_imageView_button);

        //get correct note data
        final Location location = myData.get(index);

        //set text view text with note data
        locationNameTextView.setText(location.name);
        locationNameTextView.setSingleLine(true);

        deleteImageViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.trips.get(tripIndex).locations.remove(index);
                notifyDataSetChanged();
                mainActivity.updateDatabase();
            }
        });

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {

        //ensure all text displayed is correct for this trip
        final Location location = myData.get(position);
        viewHolder.locationNameTextView.setText(location.name);
        viewHolder.locationNameTextView.setSingleLine(true);

        viewHolder.deleteImageViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.trips.get(tripIndex).locations.remove(position);
                notifyDataSetChanged();
                mainActivity.updateDatabase();
            }
        });
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView locationNameTextView;
        ImageView deleteImageViewButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            locationNameTextView = itemView.findViewById(R.id.location_name_in_item_list_textView);
            deleteImageViewButton = itemView.findViewById(R.id.delete_location_imageView_button);
        }
    }
}