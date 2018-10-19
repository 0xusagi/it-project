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
import android.widget.ToggleButton;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.network.BadRequestException;
import com.comp30023.spain_itproject.network.NoConnectionException;
import com.comp30023.spain_itproject.network.UnverifiedAccountException;
import com.comp30023.spain_itproject.ui.calls.BaseActivity;
import com.comp30023.spain_itproject.uicontroller.AccountController;

/**
 * Activity for uses to create/register an account
 * When an account is registered, logs in the account and launches the corresponding HomeActivity (either CarerHomeActivity or DependentHomeActivity)
 */

public class AccountCreationActivity extends NetworkActivity {

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

    private boolean verified;

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

        verified = true;

        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setEnabled(false);
        setRegisterButtonListener(this);

        cancelButton = (Button) findViewById(R.id.cancelButton);
        setCancelButtonListener(this);
    }

    /**
     * Disable the register button if the sinch client is not available yet so that the user
     * is able to register to the sinch client and will not cause any complications later
     */
    @Override
    public void onServiceConnected() {
        registerButton.setEnabled(true);
    }

    /**
     * Sets references to the pin fields and sets input restrictions
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

    /**
     * Registers a new account on a separate thread and loads the next activity if successful
     * @param context Reference to the activity the LoginHandler was called from
     */
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
                final String name = nameText.getText().toString();
                final String phoneNumber = phoneNumberText.getText().toString();
                final String pin = pinText.getText().toString();
                final String confirmPin = confirmPinText.getText().toString();

                ToggleButton dependentButton = (ToggleButton) findViewById(R.id.dependentButton);
                final Boolean isDependent = dependentButton.isChecked();

                //Register the account, handle input errors

                //Logs in on new thread asynchronously
                @SuppressLint("StaticFieldLeak")
                NetworkTask task = new NetworkTask() {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        super.doInBackground(objects);
                        try {
                            LoginHandler.getInstance().register(context, name, phoneNumber, pin, confirmPin, isDependent);


                        } catch (UnverifiedAccountException e) {

                            System.out.println("Unverified received");
                            verified = false;

                        } catch (Exception e) {
                            String message = e.getMessage();
                            displayErrorMessage(message);
                            return null;
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);

                        if (!verified) {
                            displayDialog(context, "Enter the text that was send to your mobile: ", phoneNumber, pin);
                        }

                        if (LoginSharedPreference.getId(AccountCreationActivity.this) != null) {
                            if (getSinchInterface() != null && !getSinchInterface().isStarted()) {
                                getSinchInterface().startClient(LoginSharedPreference.getId(AccountCreationActivity.this));
                            }
                        }
                    }
                };

                task.execute();

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
        builder.setTitle(LoginActivity.VERIFY_ACCOUNT_TITLE);

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
