package com.group9.inclass12;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class TripsListFragment extends Fragment {

    public static String TRIP_LIST_KEY = "trip_list_key";

    private OnTripsListFragmentInteractionListener mListener;

    private TripAdapter adapter;
    private RecyclerView.LayoutManager myLayoutManager;
    private MainActivity mainActivity = null;

    public TripsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trips_list, container, false);

        getActivity().setTitle("Planned Trips");

        mainActivity = (MainActivity) getActivity();

        //find the recycler view and set fixed size
        RecyclerView recyclerView = view.findViewById(R.id.trips_recyclerView);
        recyclerView.setHasFixedSize(true);

        //use a linear layout manager
        myLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(myLayoutManager);

        //set the adapter
        adapter = new TripAdapter((ArrayList<Trip>) this.getArguments().getSerializable(TRIP_LIST_KEY));
        recyclerView.setAdapter(adapter);

        //add note button
        //region
        view.findViewById(R.id.goTo_add_trip_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to add note fragment
                mListener.goToAddTripFragment();
            }
        });
        //endregion

        return view;
    }

    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTripsListFragmentInteractionListener) {
            mListener = (OnTripsListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTripsListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnTripsListFragmentInteractionListener {
        // TODO: Update argument type and name
        void goToAddTripFragment();
    }
}
