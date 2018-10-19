package com.comp30023.spain_itproject.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.network.BadRequestException;
import com.comp30023.spain_itproject.network.NoConnectionException;
import com.comp30023.spain_itproject.network.UnverifiedAccountException;
import com.comp30023.spain_itproject.uicontroller.AccountController;

public class LoginActivity extends NetworkActivity {

    public static final String VERIFY_ACCOUNT_TITLE = "Enter verification code";

    public static final int PIN_LENGTH = 4;

    private TextView messageText;

    private Button loginButton;
    private Button cancelButton;

    private EditText phoneNumberText;
    private EditText pinText;

    private boolean verified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        messageText = (TextView) findViewById(R.id.login_message_text);

        loginButton = (Button) findViewById(R.id.login_loginButton);
        loginButton.setEnabled(false);
        setLoginButtonListener(this);

        cancelButton = (Button) findViewById(R.id.login_cancelButton);
        setCancelButtonListener(this);

        phoneNumberText = (EditText) findViewById(R.id.login_phoneNumberLoginField);

        verified = true;

        setPinFields();
    }

    @Override
    public void onServiceConnected() {
        loginButton.setEnabled(true);
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
                NetworkTask task = new NetworkTask() {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        super.doInBackground(objects);

                        try {
                            LoginHandler.getInstance().login(context, phoneNumber, pin);

                        } catch (UnverifiedAccountException e) {
                            verified = false;

                        } catch (Exception e) {
                            displayErrorMessage(e.getMessage());
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);

                        if (!verified) {
                            displayDialog(context, "Enter the text that was send to your mobile: ", phoneNumber, pin);
                        }


                        if (LoginSharedPreference.getId(LoginActivity.this) != null && getSinchInterface() != null && !getSinchInterface().isStarted()) {
                            getSinchInterface().startClient(LoginSharedPreference.getId(LoginActivity.this));
                        }
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

    private void displayDialog(final Context context, String message, final String phoneNumber, final String pin) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(VERIFY_ACCOUNT_TITLE);

        TextView textPrompt = new TextView(context);
        textPrompt.setText(message);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Set up the input
        final EditText input = new EditText(context);

        layout.addView(textPrompt);
        layout.addView(input);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final String code = input.getText().toString();
                verified = true;

                @SuppressLint("StaticFieldLeak")
                NetworkTask task = new NetworkTask() {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        super.doInBackground(objects);

                        try {
                            AccountController.getInstance().verifyAccount(phoneNumber, code);
                            LoginHandler.getInstance().login(context, phoneNumber, pin);

                        } catch (UnverifiedAccountException e1) {
                            verified = false;

                        } catch (BadRequestException e1) {
                            e1.printStackTrace();
                            verified = false;

                        } catch (NoConnectionException e1) {
                            e1.printStackTrace();

                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);

                        if (!verified) {
                            displayDialog(context, "That code was not correct, please try again", phoneNumber, pin);
                        }
                    }
                };
                task.execute();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
