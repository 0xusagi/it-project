package com.comp30023.spain_itproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ToggleButton;

public class AccountCreationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_creation);

        ToggleButton dependentButton = (ToggleButton) findViewById(R.id.dependentButton);
        dependentButton.setChecked(true);

        ToggleButton carerButton = (ToggleButton) findViewById(R.id.carerButton);
        carerButton.setChecked(false);

    }

    /*public void registerButtonClick(View view) {

        EditText nameText = (EditText) findViewById(R.id.nameField);
        String name = nameText.getText().toString();

        EditText phoneNumberText = (EditText) findViewById(R.id.phoneNumberField);
        String phoneNumber = phoneNumberText.getText().toString();

        EditText pinText = (EditText) findViewById(R.id.pinField);
        String pin = pinText.getText().toString();

        createAccount(name, phoneNumber, pin);



    }

    public void createAccount(String name, String phoneNumber, String pin) {

    }*/

    public void dependentButtonClick(View view) {
        ToggleButton dependentButton = (ToggleButton) findViewById(R.id.dependentButton);
        dependentButton.setChecked(true);

        ToggleButton carerButton = (ToggleButton) findViewById(R.id.carerButton);
        carerButton.setChecked(false);
    }

    public void carerButtonClick(View view) {
        ToggleButton dependentButton = (ToggleButton) findViewById(R.id.dependentButton);
        dependentButton.setChecked(false);

        ToggleButton carerButton = (ToggleButton) findViewById(R.id.carerButton);
        carerButton.setChecked(true);
    }

    public void cancelButtonClick(View view) {
        finish();
    }

}
