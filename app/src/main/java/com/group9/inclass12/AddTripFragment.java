package com.group9.inclass12;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

//In-Class12
//Group 9
//Rockford Stoller
//Ryan Swaim

public class AddTripFragment extends Fragment {

    OnAddTripFragmentInteractionListener mListener;

    public String tripName = null;

    public TextView dateDisplayTextView;
    public String tripDate = null;

    private MainActivity mainActivity = null;

    public AddTripFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_trip, container, false);

        getActivity().setTitle("Add Trip");

        mainActivity = (MainActivity) getActivity();

        final EditText tripNameEditText = view.findViewById(R.id.trip_name_editText);
        final EditText citySearchEditText = view.findViewById(R.id.city_search_editText);
        dateDisplayTextView = view.findViewById(R.id.current_picked_date_textView);
        Button pickDateButton = view.findViewById(R.id.pick_date_button);
        Button searchButton = view.findViewById(R.id.search_and_add_trip_button);
        Button cancelButton = view.findViewById(R.id.cancel_add_trip_button);

        //pick date button
        //region
        pickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });
        //endregion

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tripNameEditText.length() > 0 && citySearchEditText.length() > 0 && tripDate != null) {

                    tripName = tripNameEditText.getText().toString();

                    //https://maps.googleapis.com/maps/api/place/findplacefromtext/json?
                    RequestParams urlString = new RequestParams();
                    urlString.addParameter("input", citySearchEditText.getText().toString());
                    urlString.addParameter("inputtype", "textquery");
                    urlString.addParameter("fields", "name,geometry");
                    urlString.addParameter("key", getResources().getString(R.string.api_key));
                    urlString.addParameter("type", "city_hall");

                    new GetDestinationCity().execute(urlString.getEncodedUrl("https://maps.googleapis.com/maps/api/place/findplacefromtext/json"));
                } else {
                    if(tripNameEditText.length() == 0) {
                        Toast.makeText(mainActivity, "Enter a trip name", Toast.LENGTH_LONG).show();
                    } else if(citySearchEditText.length() == 0) {
                        Toast.makeText(mainActivity, "Enter a city to plan a trip to", Toast.LENGTH_LONG).show();
                    } else if(tripDate == null) {
                        Toast.makeText(mainActivity, "Pick a date for the trip", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    class GetDestinationCity extends AsyncTask<String, Integer, Location> {

        @Override
        protected void onPostExecute(Location location) {

            //send city to MainActivity to add the the trip list
            //region
            super.onPostExecute(location);
            if(location != null) {
                mListener.cityFound(location, tripName, tripDate);
            } else {
                Toast.makeText(mainActivity, "Unable to find such city", Toast.LENGTH_LONG).show();
            }
            //endregion
        }

        @Override
        protected Location doInBackground(String... strings) {

            //get candidate cities
            //region
            HttpURLConnection connection = null;
            Location result = null;
            try {
                Log.d("demo", "doInBackground: " + strings[0]);
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    result = null;
                    String json = IOUtils.toString(connection.getInputStream(), "UTF-8");

                    JSONObject root = new JSONObject(json);
                    JSONArray candidatesArray = root.getJSONArray("candidates");

                    Log.d("demo", candidatesArray.length() + "");

                    //if no city is found force a return
                    if(candidatesArray.length() == 0) {
                        return null;
                    }

                    for(int i = 0; i < candidatesArray.length(); i++) {
                        Location location = new Location();

                        JSONObject locationJson = candidatesArray.getJSONObject(i);

                        location.name = locationJson.getString("name");
                        Log.d("demo", "name: " + location.name);

                        locationJson = candidatesArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location");

                        location.lat = locationJson.getString("lat");
                        location.lng = locationJson.getString("lng");
                        Log.d("demo", "lat: " + location.lat + " lng: " + location.lng);

                        result = location;
                    }
                }
            } catch (SocketTimeoutException e) {
                publishProgress(-1);
                e.printStackTrace();
            } catch (MalformedURLException e) {
                Log.d("demo", "Error 3");
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("demo", "Error 4");
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                //Close open connections and reader
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
            //endregion
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddTripFragmentInteractionListener) {
            mListener = (OnAddTripFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAddTripFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnAddTripFragmentInteractionListener {
        // TODO: Update argument type and name
        void cityFound(Location location, String tripName, String tripDate);
    }
}
