package com.comp30023.spain_itproject.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.comp30023.spain_itproject.LoginHandler;
import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.CarerUser;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.uicontroller.AccountController;

import java.io.IOException;
import java.util.ArrayList;

/**
 * The home activity for carer which displays on the carers screen after login and
 * will remain as the home screen for carers in the application.
 * Contains a list of dependents, upon clicking the dependents, allowing the carer to perform
 * various different activities in order to observe or contact the dependent
 */
public class CarerHomeActivity extends AppCompatActivity {

    // Dependents list
    private ListView dependentsList;
    private ArrayAdapter<String> arrayAdapter;

    // Settings button
    private ImageButton settingsButton;

    // Add Dependent button
    private Button addDependentsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carer_home);

        displayDependentsList();

        setupAddDependentButton();

        setupSettingsButton(this);
    }

    /**
     * Setup displaying the dependents list
     * Dynamically gets the list of dependents from the server
     */
    private void displayDependentsList() {
        // TODO Get dependents list from server

        CarerUser carerUser = (CarerUser) getIntent().getSerializableExtra(LoginHandler.PASSED_USER);

        // Get the dependents list from the server
        // Throws an IO exception if a problem occured when talking to the server, so when this
        // happens, then just retry
        try {
            ArrayList<DependentUser> dependentUsers = AccountController.getDependentsOfCarer(carerUser.getPhoneNumber());
            dependentsList = findViewById(R.id.carerHome_dependentsList);

        /*
        // For now display a fake list
        ArrayList<DependentUser> dependentUsers = new ArrayList<>();
        dependentUsers.add(new DependentUser("John","0402849382", "1234", 1));
        dependentUsers.add(new DependentUser("Asdf","0402849382", "1234", 2));

*/
            // Get the name of the user to display in the list
            ArrayList<String> dependentNames = new ArrayList<>();
            for (DependentUser dependent : dependentUsers) {
                dependentNames.add(dependent.getName());
            }

            // Make the array adapter
            arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dependentNames);

            dependentsList.setAdapter(arrayAdapter);

            // Set an on click listener
            dependentsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String[] options = {"Call", "Message"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(CarerHomeActivity.this);
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
        } catch (IOException e) {
            // When cannot get prompt whether to try again
            Toast.makeText(this, "Cannot get dependents. Please try again", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Setup the add dependents button so that it displays the next activity on click
     */
    private void setupAddDependentButton() {
        addDependentsButton = (Button) findViewById(R.id.carerHome_addDependentButton);

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
        settingsButton = (ImageButton) findViewById(R.id.carerHome_settingsButton);

        // When pressed, the user is logged out
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginHandler.logout(context);
                finish();
            }
        });
    }
}
