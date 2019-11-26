package com.group9.inclass12;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

//In-Class12
//Group 9
//Rockford Stoller
//Ryan Swaim

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {

    ArrayList<Trip> myData;

    MainActivity mainActivity;

    public TripAdapter(ArrayList<Trip> myData) {
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

        final View view  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trip_item, viewGroup, false);

        //get all inner views
        TextView tripNameTextView = view.findViewById(R.id.trip_name_in_item_list_textView);
        ImageView displayTripImageViewButton = view.findViewById(R.id.map_of_trip_imageView_button);
        ImageView editTripImageViewButton = view.findViewById(R.id.edit_trip_imageView_button);
        ImageView deleteImageViewButton = view.findViewById(R.id.delete_trip_imageView_button);

        //get correct note data
        final Trip trip = myData.get(index);

        //set text view text with note data
        tripNameTextView.setText(trip.tripName);
        tripNameTextView.setSingleLine(true);

        //get MainActivity instance
        mainActivity = (MainActivity) view.getContext();

        displayTripImageViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.goToDisplayTripFragment(index);
            }
        });

        editTripImageViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.goToEditTripFragment(index);
            }
        });

        deleteImageViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.trips.remove(index);
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
        final Trip trip = myData.get(position);
        viewHolder.tripNameTextView.setText(trip.tripName);
        viewHolder.tripNameTextView.setSingleLine(true);

        viewHolder.displayTripImageViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.goToDisplayTripFragment(position);
            }
        });

        viewHolder.editTripImageViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.goToEditTripFragment(position);
            }
        });

        viewHolder.deleteImageViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.trips.remove(position);
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

        TextView tripNameTextView;
        ImageView displayTripImageViewButton;
        ImageView editTripImageViewButton;
        ImageView deleteImageViewButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tripNameTextView = itemView.findViewById(R.id.trip_name_in_item_list_textView);
            displayTripImageViewButton = itemView.findViewById(R.id.map_of_trip_imageView_button);
            editTripImageViewButton = itemView.findViewById(R.id.edit_trip_imageView_button);
            deleteImageViewButton = itemView.findViewById(R.id.delete_trip_imageView_button);
        }
    }
}