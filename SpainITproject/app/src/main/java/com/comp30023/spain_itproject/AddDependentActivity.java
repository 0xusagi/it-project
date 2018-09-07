package com.comp30023.spain_itproject;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.comp30023.spain_itproject.controller.AccountController;
import com.comp30023.spain_itproject.domain.Dependent;
import com.comp30023.spain_itproject.validation.PhoneNumberValidator;

public class AddDependentActivity extends AppCompatActivity {

    private EditText mobileNumberField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dependent);

        getSupportActionBar().hide();
    }

    // TODO
    public void search(View view) {
        mobileNumberField = findViewById(R.id.addDependent_addMobileNumberEditText);
        String mobileNumber = mobileNumberField.getText().toString();

        // Do some validity checking here
        // If valid pass the phone number to the account controller to get the data from the
        // server
        if (PhoneNumberValidator.isValidNumber(mobileNumber)) {
            // TODO add passing data to the server. currently no server
            AccountController accountController = AccountController.getInstance();

            // Account controller returns null if no user is found
            Dependent dependent = accountController.getDependentFromServer(mobileNumber);
            if (dependent == null) {
                // Display an error message saying dependent cannot be found
                Toast.makeText(this, "@string/invalidDependentPhoneNumberMessage", Toast.LENGTH_SHORT).show();
            }
            // TODO what to do when valid (display popup maybe to add)
            else {
                displayInfoDialog(dependent.getName());
            }
        }
    }

    private void displayInfoDialog(String dependentName) {
        AlertDialog.Builder dependentInfoDialogBuilder = new AlertDialog.Builder(this);

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
                finish();
            }
        });

        AlertDialog alertDialog = dependentInfoDialogBuilder.create();
        alertDialog.show();

        // Clear the edit text field for entering mobile number
        mobileNumberField.setText("");
     }
}
