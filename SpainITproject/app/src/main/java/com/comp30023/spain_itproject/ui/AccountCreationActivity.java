package com.comp30023.spain_itproject.ui;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.User;
import com.comp30023.spain_itproject.uicontroller.AccountController;

public class AccountCreationActivity extends AppCompatActivity {

    public static final String PASSED_USER = "PASSED USER";

    public static final String invalidDetails = "Those details didn\'t work, please try again";
    public static final String incorrectPinLength = "PIN must be 4 digits, please try again";
    public static final String differentPins = "PINs don\'t match, please try again";

    private TextView messageText;

    private EditText nameText;
    private EditText phoneNumberText;
    private EditText pinText;
    private EditText confirmPinText;

    private ToggleButton dependentButton;
    private ToggleButton carerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_creation);

        messageText = (TextView) findViewById(R.id.enterDetailsText);

        dependentButton = (ToggleButton) findViewById(R.id.dependentButton);
        dependentButton.setChecked(true);

        carerButton = (ToggleButton) findViewById(R.id.carerButton);
        carerButton.setChecked(false);

        nameText = (EditText) findViewById(R.id.nameField);
        phoneNumberText = (EditText) findViewById(R.id.phoneNumberField);
        pinText = (EditText) findViewById(R.id.pinField);
        confirmPinText = (EditText) findViewById(R.id.confirmPinField);

    }

    public void registerButtonClick(View view) {

        String name = nameText.getText().toString();
        String phoneNumber = phoneNumberText.getText().toString();
        String pin = pinText.getText().toString();
        String confirmPin = confirmPinText.getText().toString();

        ToggleButton dependentButton = (ToggleButton) findViewById(R.id.dependentButton);
        Boolean isDependent = dependentButton.isChecked();

        if (pin.length() != 4) {
            resetPinFields(incorrectPinLength);
            return;
        } else if (!(confirmPin.equals(pin))) {
            resetPinFields(differentPins);
            return;
        }

        boolean success = AccountController.registerAccount(name, phoneNumber, pin, isDependent);
        if (!success) {
            resetAllFields(invalidDetails);
        } else {

            User user = AccountController.login(phoneNumber, pin, isDependent);

            Intent intent;
            if (isDependent) {
                intent = new Intent(this, DependentHomeActivity.class);
            } else {
                intent = new Intent(this, CarerHomeActivity.class);
            }

            intent.putExtra(PASSED_USER, user);
            startActivity(intent);

        }
    }

    private void resetPinFields(String message) {
        nameText.getText().clear();
        phoneNumberText.getText().clear();
        pinText.getText().clear();
        confirmPinText.getText().clear();

        messageText.setText(message);
        messageText.setTextColor(Color.RED);
    }

    private void resetAllFields(String message) {
        nameText.getText().clear();
        phoneNumberText.getText().clear();
        pinText.getText().clear();
        confirmPinText.getText().clear();

        messageText.setText(message);
        messageText.setTextColor(Color.RED);
    }

    public void dependentButtonClick(View view) {
        dependentButton.setChecked(true);
        carerButton.setChecked(false);
    }

    public void carerButtonClick(View view) {
        dependentButton.setChecked(false);
        carerButton.setChecked(true);
    }

    public void cancelButtonClick(View view) {
        finish();
    }

}
