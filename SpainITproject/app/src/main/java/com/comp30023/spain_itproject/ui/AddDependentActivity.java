package com.comp30023.spain_itproject.ui;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.uicontroller.AccountController;
import com.comp30023.spain_itproject.validation.PhoneNumberLengthValidator;

import java.io.IOException;

public class AddDependentActivity extends AppCompatActivity {

    private EditText mobileNumberField;
    private Button searchButton;

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
                String phoneNumber = mobileNumberField.getText().toString();

                // Send to server
                try {
                    DependentUser dependentUser = AccountController.getDependent(phoneNumber);
                    displayInfoDialog(dependentUser.getName());
                } catch (IOException e) {
                    // Display error message as a toast
                    Toast errorMsg = Toast.makeText(getApplicationContext(), "Dependent does not exist", Toast.LENGTH_SHORT);
                }
            }
        });
    }

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

            }
        });

        // Set the clickable button "No"
        dependentInfoDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            // Close the alert dialog
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = dependentInfoDialogBuilder.create();
        alertDialog.show();

        // Clear the edit text field for entering mobile number
        mobileNumberField.setText("");
     }
}
