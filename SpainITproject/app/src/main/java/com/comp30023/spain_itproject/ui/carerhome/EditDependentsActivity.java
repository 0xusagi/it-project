package com.comp30023.spain_itproject.ui.carerhome;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.comp30023.spain_itproject.ui.NetworkActivity;
import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.domain.Location;

import java.util.ArrayList;
import java.util.List;

public class EditDependentsActivity extends NetworkActivity {

    public final String NO_LOCATIONS_MSG = "This dependent currently has no locations. Please add some for them";

    public static final String EXTRA_DEPENDENT = "Dependent";

    // Dependent's locations which contains all the fields
    private List<Location> locations;

    private DependentUser dependent;

    // Current dependent id obtained from previosu activity
    private String dependentID;

    // Locations list of dependent
    private ListView locationsList;
    private ArrayAdapter<String> arrayAdapter;

    private Button addLocationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dependents);

        dependent = (DependentUser) getIntent().getSerializableExtra(EXTRA_DEPENDENT);

        // Get the id of the dependent that needs to be edited which is passed through the intent
        dependentID = dependent.getId();

        // Display the locations list on the screen
        displayLocationsList();

        final Context context = this;

        addLocationButton = (Button) findViewById(R.id.editDependents_addLocationsButton);
        addLocationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CarerMapsActivity.class);

                intent.putExtra(CarerMapsActivity.EXTRA_DEPENDENT, dependent);

                startActivity(intent);
            }

        });
    }

    public void displayLocationsList() {
        locationsList = findViewById(R.id.editDependents_locationsList);

        // Start the task to setup the locations list
        new SetupLocationsListTask().execute();
    }

    private void setupList() {
        boolean isSetOnClick;
        ArrayList<String> locationNames = new ArrayList<>();

        // Get the locations

        // Handle where carer has no dependents
        if (locations.size() == 0) {
            locationNames.add(NO_LOCATIONS_MSG);
            isSetOnClick = false;
        }
        // Carer has dependents
        else {
            // Get the carer names
            for (Location location: locations) {
                locationNames.add(location.getDisplayName());
            }
            isSetOnClick = true;
        }

        // Set array adapter
        arrayAdapter = new ArrayAdapter<>(EditDependentsActivity.this, android.R.layout.simple_list_item_1, locationNames);

        locationsList.setAdapter(arrayAdapter);

        // Set on click listener only if the carer has dependents
        if (isSetOnClick) {
            locationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(final AdapterView<?> adapterView, View view, final int locationIndex, long l) {
                    // TODO change add the edit function
                    String[] options = {"Delete"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(EditDependentsActivity.this);
                    builder.setTitle("Options");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // the user clicked on options[which]

                            @SuppressLint("StaticFieldLeak")
                            NetworkTask task = new NetworkTask() {

                                @Override
                                protected Object doInBackground(Object[] objects) {
                                    super.doInBackground(objects);

                                    try {

                                        dependent.deleteLocation(locations.get(locationIndex));

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                arrayAdapter.clear();

                                                storeLocations(locations);
                                                setupList();
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Object o) {
                                    super.onPostExecute(o);
                                }
                            };
                            task.execute();
                        }
                    });
                    builder.show();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        System.out.println("On resume called");

        if (arrayAdapter != null) {
            System.out.println("Clearing array adapter");
            arrayAdapter.clear();
        }
        displayLocationsList();
    }

    /**
     * Asynchronous task to get the dependent that is selected by the carer to edit
     * Then this task sets up the list to display the locations of the dependent
     */
    private class SetupLocationsListTask extends NetworkTask<Void, Void, List<Location>> {
        Exception exception;

        @Override
        protected List<Location> doInBackground(Void... voids) {
            super.doInBackground(voids);
            try {
                return dependent.getLocations();
            } catch (Exception e) {
                exception = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Location> locations) {
            super.onPostExecute(locations);
            // Check if the dependent user has been successfully obtained
            if (locations == null) {
                Toast.makeText(EditDependentsActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            } else {

                storeLocations(locations);
                setupList();
            }
        }
    }

    /**
     * Helper function to store the list of locations since it cannot be accessed from the outer
     * class
     * @param locations
     */
    private void storeLocations(List<Location> locations) {
        this.locations = locations;
    }
}
