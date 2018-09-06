package com.comp30023.spain_itproject.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.comp30023.spain_itproject.LoginHandler;
import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.InvalidDetailsException;
import com.comp30023.spain_itproject.domain.User;
import com.comp30023.spain_itproject.uicontroller.AccountController;

/**
 * Activity for uses to create/register an account
 * When an account is registered, logs in the account and launches the corresponding HomeActivity (either CarerHomeActivity or DependentHomeActivity)
 */
public class AccountCreationActivity extends AppCompatActivity {

    public static final int PIN_LENGTH = 4;

    private TextView messageText;

    private EditText nameText;
    private EditText phoneNumberText;
    private EditText pinText;
    private EditText confirmPinText;

    private ToggleButton dependentButton;
    private ToggleButton carerButton;

    private Button registerButton;
    private Button cancelButton;

    /**
     * Initialises activity, sets display to corresponding layout and sets references to Views in layout
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_creation);

        messageText = (TextView) findViewById(R.id.enterDetailsText);

        dependentButton = (ToggleButton) findViewById(R.id.dependentButton);
        dependentButton.setChecked(true);
        dependentButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Toggles dependentButton to checked, carerButton to unchecked
             * @param v
             */
            @Override
            public void onClick(View v) {
                dependentButton.setChecked(true);
                carerButton.setChecked(false);
            }
        });

        carerButton = (ToggleButton) findViewById(R.id.carerButton);
        carerButton.setChecked(false);
        carerButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Toggles carerButton to checked, dependentButton to unchecked
             * @param v
             */
            @Override
            public void onClick(View v) {
                dependentButton.setChecked(false);
                carerButton.setChecked(true);
            }
        });

        nameText = (EditText) findViewById(R.id.nameField);
        phoneNumberText = (EditText) findViewById(R.id.phoneNumberField);

        setPinFields();

        registerButton = (Button) findViewById(R.id.registerButton);
        setRegisterButtonListener(this);

        cancelButton = (Button) findViewById(R.id.cancelButton);
        setCancelButtonListener(this);

    }

    /**
     * References in the pin fields and sets input restrictions
     */
    private void setPinFields() {

        pinText = (EditText) findViewById(R.id.pinField);
        confirmPinText = (EditText) findViewById(R.id.confirmPinField);

        InputFilter.LengthFilter maxPinLength = new InputFilter.LengthFilter(PIN_LENGTH);
        InputFilter[] pinFilters = new InputFilter[1];
        pinFilters[0] = maxPinLength;
        pinText.setFilters(pinFilters);
        confirmPinText.setFilters(pinFilters);
    }

    private void setRegisterButtonListener(final Context context) {
        registerButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Collects information from other fields and creates an account to log in to
             * When enterred information is invalid, displays message
             * When enterred information is valid, creates an account, passes to LoginHandler and closes this activity
             * @param v
             */
            @Override
            public void onClick(View v) {

                //Retrieve inputs from fields
                String name = nameText.getText().toString();
                String phoneNumber = phoneNumberText.getText().toString();
                String pin = pinText.getText().toString();
                String confirmPin = confirmPinText.getText().toString();

                ToggleButton dependentButton = (ToggleButton) findViewById(R.id.dependentButton);
                Boolean isDependent = dependentButton.isChecked();

                //Register the account, handle input errors
                try {
                    AccountController.registerAccount(name, phoneNumber, pin, confirmPin, isDependent);
                } catch (InvalidDetailsException e) {
                    messageText.setText(e.getMessage());
                    messageText.setTextColor(Color.RED);
                    return;
                }

                //Login the user
                LoginHandler.newLogin(context, phoneNumber, pin, isDependent);
                finish();
            }
        });
    }

    private void setCancelButtonListener(final Context context) {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            /**
             * When button clicked, closes this Activity and starts the StartActivity
             * @param v
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StartActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Same functionality as if the cancel button
     */
    public void onBackPressed() {
        cancelButton.performClick();
    }

}
