package com.comp30023.spain_itproject.ui.carerhome;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.CarerUser;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.ui.LoginHandler;
import com.comp30023.spain_itproject.ui.LoginSharedPreference;
import com.comp30023.spain_itproject.uicontroller.AccountController;

import java.util.ArrayList;
import java.util.List;

/**
 * The home activity for carer which displays on the carers screen after login and
 * will remain as the home screen for carers in the application.
 * Contains a list of dependents, upon clicking the dependents, allowing the carer to perform
 * various different activities in order to observe or contact the dependent
 */
public class CarerHomeActivity extends AppCompatActivity {

    // The list of dependents of the carer which contains all information about the dependents
    // to be used when the carer wants to edit the dependent
    private List<DependentUser> dependents;

    // Dependents list
    private ListView dependentsList;
    private ArrayAdapter<String> arrayAdapter;

    // Settings button
    private ImageButton settingsButton;

    // Add Dependent button
    private Button addDependentsButton;

    // Refresh button
    private ImageButton refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carer_home);

        dependentsList = findViewById(R.id.carerHome_dependentsList);
        displayDependentsList();

        setupAddDependentButton();

        setupSettingsButton(this);
    }

    /**
     * Setup displaying the dependents list
     * Dynamically gets the list of dependents from the server
     */
    private void displayDependentsList() {
        // Get the dependents list from the server
        new DisplayDependentsListTask().execute(LoginSharedPreference.getId(this));
    }

    private void setArrayAdapter(ArrayList<String> dependentNames) {
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dependentNames);
    }

    /**
     * Setup the add dependents button so that it displays the next activity on click
     */
    private void setupAddDependentButton() {
        addDependentsButton = findViewById(R.id.carerHome_addDependentButton);

        // When pressed the add dependents button the next activity to be shown is the
        // add dependent activity
        addDependentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display the next activity
                Intent intent = new Intent(getApplicationContext(), AddDependentActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Setup the settings button
     * @param context
     */
    private void setupSettingsButton(final Context context) {
        settingsButton = findViewById(R.id.carerHome_settingsButton);

        // When pressed, the user is logged out
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginHandler.getInstance().logout(context);
                finish();
            }
        });
    }

    /**
     * Async task to run on background thread to get the carer user from the server
     */
    private class DisplayDependentsListTask extends AsyncTask<String, Void, List<DependentUser>> {
        private Exception exception;

        @Override
        protected List<DependentUser> doInBackground(String... strings) {
            // Get the carer User from the server
            try {
                CarerUser carer = AccountController.getInstance().getCarer(strings[0]);
                return carer.getDependents();
            } catch (Exception e) {
                exception = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<DependentUser> dependents) {
            // If the carer User is null, then there is an error
            if (dependents == null) {
                // Make a toast for the error
                Toast.makeText(CarerHomeActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                storeDependents(dependents);
                setupList();
            }
        }
    }

    private void setupRefreshButton() {
        refreshButton = findViewById(R.id.carerHome_refreshButton);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupList();
            }
        });
    }
  
    /**
     * Helper method to store the dependents list into the class since cannot be accessed in the
     * inner field
     * @param dependents
     */
    private void storeDependents(List<DependentUser> dependents) {
        this.dependents = dependents;
    }

    /**
     * Makes a list of the dependent names that a carer has,
     * If carer has none, then display a message
     * @param carerUser
     * @return
     */
    private void setupList() {
        boolean isSetOnClick;
        ArrayList<String> dependentNames = new ArrayList<>();

        // Get the dependents
        if (dependents == null) {
            System.out.println("NULL");
            finish();
        }

        // Handle where carer has no dependents
        if (dependents.size() == 0) {
            dependentNames.add("You currently have no dependents :(");
            isSetOnClick = false;
        }
        // Carer has dependents
        else {
            // Get the carer names
            for (DependentUser dependent: dependents) {
                dependentNames.add(dependent.getName());
            }
            isSetOnClick = true;
        }

        // Set array adapter
        arrayAdapter = new ArrayAdapter<>(CarerHomeActivity.this, android.R.layout.simple_list_item_1, dependentNames);

        dependentsList.setAdapter(arrayAdapter);

        // Set on click listener only if the carer has dependents
        if (isSetOnClick) {
            dependentsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    setupAlertDialog(i);
                }
            });
        }
    }

    /**
     * Setup the alert dialog which displays the options on what to do when a dependent is clicked
     * from the list view
     * @param i
     */
    private void setupAlertDialog(final int i) {
        String[] options = {"Call", "Message", "Edit"};

        AlertDialog.Builder builder = new AlertDialog.Builder(CarerHomeActivity.this);
        builder.setTitle("Choose");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on options[which]
                Intent intent;
                switch (which) {
                    case 0:
                        // Get the phone number of the dependent
                        String phoneNumber = getDependentAt(i).getPhoneNumber();

                        // Make a new calling intent
                        intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:"+phoneNumber));

                        // Check for permission
                        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CarerHomeActivity.this,
                                    new String[]{Manifest.permission.CALL_PHONE}, 1);
                        } else {
                            startActivity(intent);
                        }
                        break;
                    // Edit button
                    case 2:
                        // Make a new intent to display the edit dependents activity
                        intent = new Intent(getApplicationContext(), EditDependentsActivity.class);
                        // Pass through the dependent id so that can edit easily
                        intent.putExtra("DependentID", getDependentAt(i).getId());
                        startActivity(intent);
                        break;

                    // Default case
                    default:
                        break;
                }
            }
        });
        builder.show();
    }

    /**
     * Helper method to get the DependentUser at the index i from the outer class from inner class
     * of the onClick method
     * @param i
     * @return
     */
    private DependentUser getDependentAt(int i) {
        return dependents.get(i);
    }
}