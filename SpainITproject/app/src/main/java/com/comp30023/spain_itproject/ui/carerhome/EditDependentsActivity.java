package com.comp30023.spain_itproject.ui.carerhome;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.CarerUser;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.domain.Location;
import com.comp30023.spain_itproject.ui.LoginSharedPreference;
import com.comp30023.spain_itproject.uicontroller.AccountController;

import java.util.ArrayList;
import java.util.List;

public class EditDependentsActivity extends AppCompatActivity {
    public final String NO_LOCATIONS_MSG = "This dependent currently has no locations. Please add some for them";

    private String dependentID;
    private ListView locationsList;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dependents);

        // Get the id of the dependent that needs to be edited which is passed through the intent
        dependentID = getIntent().getStringExtra("DependentID");

        // Display the locations list on the screen
        displayLocationsList();
    }

    public void displayLocationsList() {
        locationsList = findViewById(R.id.editDependents_locationsList);

        // Start the task to setup the locations list
        new SetupLocationsListTask().execute();
    }

    private void setupList(DependentUser dependentUser) {
        boolean isSetOnClick;
        ArrayList<String> locationNames = new ArrayList<>();

        // Get the dependents
        try {
            List<Location> locations = dependentUser.getLocations();

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
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        // TODO change the options
                        String[] options = {"Call", "Message", "Edit"};

                        AlertDialog.Builder builder = new AlertDialog.Builder(EditDependentsActivity.this);
                        builder.setTitle("Choose");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // the user clicked on options[which]
                            }
                        });
                        builder.show();
                    }
                });
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Asynchronous task to get the dependent that is selected by the carer to edit
     * Then this task sets up the list to display the locations of the dependent
     */
    private class SetupLocationsListTask extends AsyncTask<Void, Void, DependentUser> {
        Exception exception;

        @Override
        protected DependentUser doInBackground(Void... voids) {
            try {
                return AccountController.getInstance().getDependent(dependentID);
            } catch (Exception e) {
                exception = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(DependentUser dependentUser) {
            // Check if the dependent user has been successfully obtained
            if (dependentUser == null) {
                Toast.makeText(EditDependentsActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                setupList(dependentUser);
            }
        }
    }
}
