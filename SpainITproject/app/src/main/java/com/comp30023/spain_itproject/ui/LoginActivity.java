package com.comp30023.spain_itproject.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.comp30023.spain_itproject.LoginHandler;
import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.domain.Location;

public class LoginActivity extends AppCompatActivity {

    public static final int PIN_LENGTH = 4;

    private Button loginButton;
    private Button cancelButton;

    private ToggleButton dependentButton;
    private ToggleButton carerButton;

    private EditText phoneNumberText;
    private EditText pinText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.loginButton);
        setLoginButtonListener(this);

        cancelButton = (Button) findViewById(R.id.cancelButton);
        setCancelButtonListener(this);

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

        phoneNumberText = (EditText) findViewById(R.id.phoneNumberLoginField);

        setPinFields();
    }

    private void setPinFields() {

        pinText = (EditText) findViewById(R.id.pinLoginField);

        InputFilter.LengthFilter maxPinLength = new InputFilter.LengthFilter(PIN_LENGTH);
        InputFilter[] pinFilters = new InputFilter[1];
        pinFilters[0] = maxPinLength;
        pinText.setFilters(pinFilters);
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

    /**STILL REQUIRES CHECKING FOR INVALID DETAILS*/
    private void setLoginButtonListener(final Context context) {
        loginButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Collects information from other fields and logs in
             * @param v
             */
            @Override
            public void onClick(View v) {

                //Retrieve inputs from fields
                String phoneNumber = phoneNumberText.getText().toString();
                String pin = pinText.getText().toString();

                ToggleButton dependentButton = (ToggleButton) findViewById(R.id.dependentButton);
                Boolean isDependent = dependentButton.isChecked();

                //Login the user
                LoginHandler.newLogin(context, phoneNumber, pin, isDependent);
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
