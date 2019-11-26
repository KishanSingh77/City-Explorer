package com.group9.inclass12;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class LocationInDisplayAdapter extends RecyclerView.Adapter<LocationInDisplayAdapter.ViewHolder> {

    ArrayList<Location> myData;

    MainActivity mainActivity;

    public LocationInDisplayAdapter(ArrayList<Location> myData) {
        this.myData = myData;
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

        final View view  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.location_in_display_item, viewGroup, false);

        //get all inner views
        TextView locationNameTextView = view.findViewById(R.id.location_name_in_display_item_list_textView);

        //get correct note data
        final Location location = myData.get(index);

        //set text view text with note data
        locationNameTextView.setText(location.name);
        locationNameTextView.setSingleLine(true);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {

        //ensure all text displayed is correct for this trip
        final Location location = myData.get(position);
        viewHolder.locationNameTextView.setText(location.name);
        viewHolder.locationNameTextView.setSingleLine(true);

    }

    @Override
    public int getItemCount() {
        return myData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView locationNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            locationNameTextView = itemView.findViewById(R.id.location_name_in_display_item_list_textView);
        }
    }
}