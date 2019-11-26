package com.group9.inclass12;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.ArrayList;

//In-Class12
//Group 9
//Rockford Stoller
//Ryan Swaim

public class EditTripFragment extends Fragment {

    public static String LOCATION_LIST_KEY = "location_list_key";
    public static String INDEX_KEY = "index_key";

    private LocationAdapter adapter;
    private RecyclerView.LayoutManager myLayoutManager;
    private MainActivity mainActivity = null;

    private ArrayList<Location> locationsArray = new ArrayList<>();

    public int tripIndex;
    public TextView dateDisplayTextView;

    public EditTripFragment() {
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
        View view = inflater.inflate(R.layout.fragment_edit_trip, container, false);

        getActivity().setTitle("Edit Trip and Locations");

        mainActivity = (MainActivity) getActivity();

        //find the recycler view and set fixed size
        RecyclerView recyclerView = view.findViewById(R.id.locations_recyclerView);
        recyclerView.setHasFixedSize(true);

        //use a linear layout manager
        myLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(myLayoutManager);

        tripIndex = this.getArguments().getInt(INDEX_KEY);
        dateDisplayTextView = view.findViewById(R.id.current_date_in_edit_trip_textView);
        dateDisplayTextView.setText("Planned Date: " + mainActivity.trips.get(tripIndex).tripDate);

        //set the adapter
        adapter = new LocationAdapter((ArrayList<Location>) this.getArguments().getSerializable(LOCATION_LIST_KEY), tripIndex);
        recyclerView.setAdapter(adapter);

        //change date button
        //region
        view.findViewById(R.id.change_date_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });
        //endregion

        //add location button
        //region
        view.findViewById(R.id.goTo_add_location_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(adapter.myData.size() < 15) {
                    //setup the alert builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                    builder.setTitle("Choose a location category");

                    final String[] location_categories = getResources().getStringArray(R.array.location_categories);

                    builder.setItems(location_categories, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //https://maps.googleapis.com/maps/api/place/nearbysearch/json
                            RequestParams urlString = new RequestParams();
                            urlString.addParameter("location", mainActivity.trips.get(tripIndex).destinationCity.lat + "," + mainActivity.trips.get(tripIndex).destinationCity.lng)
                                    .addParameter("radius", "24140.2")
                                    .addParameter("type", location_categories[which])
                                    .addParameter("key", getResources().getString(R.string.api_key))
                                    .addParameter("fields", "name,geometry");
                            //?location=-33.8670522,151.1957362&radius=1500&type=parking&key=AIzaSyCrQ9wifukgByzMZDYgA4y27DvFMDaRzdE&fields=name,geometry

                            new GetTypeOfLocations().execute(urlString.getEncodedUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/json"));
                        }
                    });
                    //create and show the alert dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    Toast.makeText(mainActivity, "You are at max locations (15/15)", Toast.LENGTH_LONG).show();
                }
            }
        });
        //endregion

        view.findViewById(R.id.finish_add_location_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    class GetTypeOfLocations extends AsyncTask<String, Integer, ArrayList<Location>> {

        @Override
        protected void onPostExecute(final ArrayList<Location> locations) {
            super.onPostExecute(locations);

            // setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
            builder.setTitle("Choose a location");

            final String[] location_options = new String[locations.size()];

            for(int i = 0; i < locations.size(); i++) {
                location_options[i] = locations.get(i).name;
            }

            builder.setItems(location_options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    mainActivity.trips.get(tripIndex).locations.add(locations.get(which));
                    adapter.notifyDataSetChanged();
                    //1111111111111111111111111111111111111111111111111111111111111 Not working!!!!!
                    mainActivity.updateDatabase();
                }
            });
            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        @Override
        protected ArrayList<Location> doInBackground(String... strings) {

            HttpURLConnection connection = null;
            ArrayList<Location> result = null;
            try {
                Log.d("demo", "doInBackground: " + strings[0]);
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    result = new ArrayList<>();
                    String json = IOUtils.toString(connection.getInputStream(), "UTF-8");

                    JSONObject root = new JSONObject(json);
                    JSONArray resultsArray = root.getJSONArray("results");

                    Log.d("demo", resultsArray.length() + "");

                    for(int i = 0; i < 15; i++) {
                        Location location = new Location();

                        JSONObject locationJson = resultsArray.getJSONObject(i);

                        location.name = locationJson.getString("name");
                        //Log.d("demo", "name: " + location.name);

                        locationJson = resultsArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location");

                        location.lat = locationJson.getString("lat");
                        location.lng = locationJson.getString("lng");
                        //Log.d("demo", "lat: " + location.lat + " lng: " + location.lng);

                        result.add(location);
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
        }
    }
}
