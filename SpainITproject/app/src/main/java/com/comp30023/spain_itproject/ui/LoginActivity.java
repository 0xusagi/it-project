package com.comp30023.spain_itproject.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.User;
import com.comp30023.spain_itproject.uicontroller.AccountController;

public class LoginActivity extends AppCompatActivity {

    public static final int PIN_LENGTH = 4;

    private TextView messageText;

    private Button loginButton;
    private Button cancelButton;

    private EditText phoneNumberText;
    private EditText pinText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        messageText = (TextView) findViewById(R.id.login_message_text);

        loginButton = (Button) findViewById(R.id.login_loginButton);
        setLoginButtonListener(this);

        cancelButton = (Button) findViewById(R.id.login_cancelButton);
        setCancelButtonListener(this);

        phoneNumberText = (EditText) findViewById(R.id.login_phoneNumberLoginField);

        setPinFields();
    }

    //Set restrictions for the pin field
    private void setPinFields() {

        pinText = (EditText) findViewById(R.id.login_pinLoginField);

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
                final String phoneNumber = phoneNumberText.getText().toString();
                final String pin = pinText.getText().toString();

                //Asynchronous task for logging in
                @SuppressLint("StaticFieldLeak")
                AsyncTask task = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        try {
                            LoginHandler.getInstance().login(context, phoneNumber, pin);

                        } catch (Exception e) {
                            displayErrorMessage(e.getMessage());
                        }
                        return null;
                    }
                };

                task.execute();

            }
        });
    }

    /**
     * Same functionality as if the cancel button
     */
    public void onBackPressed() {
        cancelButton.performClick();
    }

    /**
     * Displays a message in the text on the screen
     * @param message Message to be displayed in the text
     */
    public void displayErrorMessage(final String message) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageText.setTextColor(Color.RED);
                messageText.setText(message);
            }
        });
    }
}
