package com.group9.inclass12;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class DisplayTripFragment extends Fragment implements OnMapReadyCallback {

    public static String LOCATION_IN_DISPLAY_LIST_KEY = "location_in_display_list_key";
    public static String INDEX_IN_DISPLAY_KEY = "index_in_display_key";

    private LocationInDisplayAdapter adapter;
    private RecyclerView.LayoutManager myLayoutManager;
    private MainActivity mainActivity = null;

    private int tripIndex;

    private GoogleMap mMap;

    public DisplayTripFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_trip, container, false);

        getActivity().setTitle("Trip Display");

        mainActivity = (MainActivity) getActivity();

        TextView tripNameTextView = view.findViewById(R.id.trip_name_in_display_textView);
        TextView destinationCityTextView = view.findViewById(R.id.destination_city_in_display_textView);
        TextView plannedDateTextView = view.findViewById(R.id.planned_date_in_display_textView);

        //find the recycler view and set fixed size
        RecyclerView recyclerView = view.findViewById(R.id.locations_in_display_recyclerView);
        recyclerView.setHasFixedSize(true);

        //use a linear layout manager
        myLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(myLayoutManager);

        //set the adapter
        adapter = new LocationInDisplayAdapter((ArrayList<Location>) this.getArguments().getSerializable(LOCATION_IN_DISPLAY_LIST_KEY));
        recyclerView.setAdapter(adapter);

        tripIndex = this.getArguments().getInt(INDEX_IN_DISPLAY_KEY);

        tripNameTextView.setText(mainActivity.trips.get(tripIndex).tripName);
        destinationCityTextView.setText(mainActivity.trips.get(tripIndex).destinationCity.name);
        plannedDateTextView.setText(mainActivity.trips.get(tripIndex).tripDate);

        //Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map_in_display);
        mapFragment.getMapAsync(this);

        view.findViewById(R.id.finish_in_display_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        final LatLngBounds.Builder builder = new LatLngBounds.Builder();

        //add initial point the destination city
        LatLng destCity = new LatLng(Double.parseDouble(mainActivity.trips.get(tripIndex).destinationCity.lat), Double.parseDouble(mainActivity.trips.get(tripIndex).destinationCity.lng));
        mMap.addMarker(new MarkerOptions().position(destCity).title(mainActivity.trips.get(tripIndex).tripName));
        builder.include(destCity);

        //set all points in the bounds for the camera
        for(Location location : adapter.myData) {
            LatLng p = new LatLng(Double.parseDouble(location.lat), Double.parseDouble(location.lng));
            mMap.addMarker(new MarkerOptions().position(p).title(location.name));
            builder.include(p);
        }

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                //build the bounds for the camera
                LatLngBounds bounds = builder.build();

                //position the map's camera
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            }
        });
    }
}
