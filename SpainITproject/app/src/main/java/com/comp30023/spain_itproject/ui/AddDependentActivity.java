package com.comp30023.spain_itproject.ui;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.uicontroller.AccountController;

public class AddDependentActivity extends AppCompatActivity {

    private EditText mobileNumberField;
    private Button searchButton;

    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dependent);

        // Setup the mobile number field
        mobileNumberField = findViewById(R.id.addDependent_addMobileNumberEditText);

        setupSearchButton();
    }

    /**
     * Setup the search button
     */
    private void setupSearchButton() {
        searchButton = findViewById(R.id.addDependent_searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO send the query to the server
                // Get the mobile number to query
                phoneNumber = mobileNumberField.getText().toString();

                // Send to server
                try {
                    String dependentName = new GetDependentNameFromNumberTask().execute(phoneNumber, AddDependentActivity.this).get();
                    displayInfoDialog(dependentName);
                } catch (Exception e) {
                    // Display error message as a toast
                    Toast errorMsg = Toast.makeText(getApplicationContext(), "Dependent does not exist", Toast.LENGTH_SHORT);
                    errorMsg.setGravity(Gravity.CENTER, 0, 0);
                    errorMsg.show();
                }
            }
        });
    }

    /**
     * Display the dialogue whether to add the dependent or not
     * @param dependentName
     */
    private void displayInfoDialog(String dependentName) {
        final AlertDialog.Builder dependentInfoDialogBuilder = new AlertDialog.Builder(this);

        // Set the title of the alert dialog
        dependentInfoDialogBuilder.setTitle("Add Dependent");

        // Set the message to be displayed
        String message = "Is " + dependentName + " waiting for you to be their carer?";
        dependentInfoDialogBuilder.setMessage(message);

        // Set the clickable button "Yes"
        dependentInfoDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            // Send the request to the dependent
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // TODO send a request to the server
                new AddDependentTask().execute(phoneNumber);
            }
        });

        // Set the clickable button "No"
        dependentInfoDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            // Close the alert dialog
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dependentInfoDialogBuilder.show();

        // Clear the edit text field for entering mobile number
        mobileNumberField.setText("");
     }

    /**
     * Async task to add a dependent on the background thread which interacts with the server
     * Calling execute(phoneNumber) always returns null
     */
    private class AddDependentTask extends AsyncTask<String, Void, Boolean> {

         @Override
         protected Boolean doInBackground(String... strings) {
             // Dependent phone number is the first argument
             String dependentPhoneNumber = strings[0];

             try {
                 AccountController.getInstance().requestDependent(LoginSharedPreference.getId(AddDependentActivity.this),
                         dependentPhoneNumber);
                 return true;

             } catch (Exception e) {
                 return false;
             }
         }

         @Override
         protected void onPostExecute(Boolean success) {
             if (success) {
                 Toast.makeText(AddDependentActivity.this, "Dependent added", Toast.LENGTH_SHORT).show();
             }
             else {
                 Toast.makeText(AddDependentActivity.this, "Failed to add dependent", Toast.LENGTH_LONG).show();
             }
         }
    }
}
