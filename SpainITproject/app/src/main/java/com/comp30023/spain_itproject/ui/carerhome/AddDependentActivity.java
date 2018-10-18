package com.comp30023.spain_itproject.ui.carerhome;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.ui.LoginSharedPreference;
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

                // Send to server and display the prompt to add
                new GetDependentNameFromNumberTask().execute(phoneNumber);
            }
        });
    }

    /**
     * Task to get the dependent's name from the server based on a phone number
     */
    private class GetDependentNameFromNumberTask extends AsyncTask<String, Void, String> {
        private Exception exception;

        @Override
        protected String doInBackground(String... params) {
            // First argument is the phone number of the dependent to search for
            String phoneNumber = params[0];

            try {
                String name = AccountController.getInstance().getDependentNameByPhoneNumber(phoneNumber);

                return name;
            }
            catch (Exception e) {
                exception = e;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String name) {
            if (name == null) {
                Toast.makeText(AddDependentActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                displayInfoDialog(name);
            }
        }
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
        String message = "Do you want to add " + dependentName + " as a friend?";
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
         private Exception exception;

         @Override
         protected Boolean doInBackground(String... strings) {
             // Dependent phone number is the first argument
             String dependentPhoneNumber = strings[0];

             try {
                 System.out.println(LoginSharedPreference.getId(AddDependentActivity.this));
                 AccountController.getInstance().requestDependent(LoginSharedPreference.getId(AddDependentActivity.this),
                         dependentPhoneNumber);
                 return true;

             } catch (Exception e) {
                 e.printStackTrace();
                 exception = e;
                 return false;
             }
         }

         @Override
         protected void onPostExecute(Boolean success) {
             if (success) {
                 Toast.makeText(AddDependentActivity.this, "Dependent added", Toast.LENGTH_SHORT).show();
             }
             else {
                 Toast.makeText(AddDependentActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
             }
         }
    }
}
