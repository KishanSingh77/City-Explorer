package com.group9.inclass12;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

//In-Class12
//Group 9
//Rockford Stoller
//Ryan Swaim

public class MainActivity extends AppCompatActivity implements TripsListFragment.OnTripsListFragmentInteractionListener, AddTripFragment.OnAddTripFragmentInteractionListener {

    ArrayList<Trip> trips = new ArrayList<>();

    TripsListFragment tripsListFragment;

    //create database variables
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("trips");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tripsListFragment = new TripsListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TripsListFragment.TRIP_LIST_KEY, trips);
        tripsListFragment.setArguments(bundle);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(trips.size() == 0) {
                    for (DataSnapshot tripSnap : dataSnapshot.getChildren()) {
                        Trip trip = tripSnap.getValue(Trip.class);

                        trips.add(trip);

                        Log.d("demo", trip.toString());
                    }
                }

                tripsListFragment.notifyAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, tripsListFragment, "trips_list_fragment")
                .commit();
    }

    public void goToEditTripFragment(int index) {

        //goTo EditTripFragment
        //region
        EditTripFragment editTripFragment = new EditTripFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EditTripFragment.LOCATION_LIST_KEY, trips.get(index).locations);
        bundle.putInt(EditTripFragment.INDEX_KEY, index);
        editTripFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, editTripFragment, "edit_trip_fragment")
                .addToBackStack(null)
                .commit();
        //endregion
    }

    public void goToDisplayTripFragment(int index) {

        //goTo DisplayTripFragment
        //region
        DisplayTripFragment displayTripFragment = new DisplayTripFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DisplayTripFragment.LOCATION_IN_DISPLAY_LIST_KEY, trips.get(index).locations);
        bundle.putInt(DisplayTripFragment.INDEX_IN_DISPLAY_KEY, index);
        displayTripFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, displayTripFragment, "display_trip_fragment")
                .addToBackStack(null)
                .commit();
        //endregion
    }

    public void updateDatabase() {
        //restore the updated list in the database
        myRef.setValue(trips);
    }

    @Override
    public void goToAddTripFragment() {

        //goTo AddTripFragment
        //region
        AddTripFragment addTripFragment = new AddTripFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, addTripFragment, "add_trip_fragment")
                .addToBackStack(null)
                .commit();
        //endregion
    }

    @Override
    public void cityFound(Location location, String tripName, String tripDate) {
        //create Trip and add it to the trips list
        //region
        Trip trip = new Trip();
        trip.tripName = tripName;
        trip.tripDate = tripDate;//new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        trip.destinationCity = location;

        trips.add(trip);
        tripsListFragment.notifyAdapter();
        updateDatabase();
        getSupportFragmentManager().popBackStack();
        //endregion
    }
}
