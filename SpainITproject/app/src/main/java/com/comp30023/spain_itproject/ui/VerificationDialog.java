package com.comp30023.spain_itproject.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comp30023.spain_itproject.network.BadRequestException;
import com.comp30023.spain_itproject.network.NoConnectionException;
import com.comp30023.spain_itproject.network.UnverifiedAccountException;
import com.comp30023.spain_itproject.uicontroller.AccountController;

/**
 * Dialog for entering the verification code
 */
public class VerificationDialog extends AlertDialog.Builder {

    public static final String VERIFICATION_DEFAULT_MESSAGE = "Enter the text that was send to your mobile: ";
    public static final String VERIFICATION_RETRY_MESSAGE = "That code was not correct, please try again";

    public static final String VERIFICATION_POSITIVE = "OK";
    public static final String VERIFICATION_NEGATIVE = "Cancel";

    public VerificationDialog(final @NonNull Context context, final String phoneNumber, final String pin, String message) {
        super(context);

        setTitle(LoginActivity.VERIFY_ACCOUNT_TITLE);

        TextView textPrompt = new TextView(context);

        if (message != null) {
            textPrompt.setText(message);
        } else {
            textPrompt.setText(VERIFICATION_DEFAULT_MESSAGE);
        }

        // Set up the input
        final EditText input = new EditText(context);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        layout.addView(textPrompt);
        layout.addView(input);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        setView(layout);

        // Set up the buttons
        setPositiveButton(VERIFICATION_POSITIVE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final String code = input.getText().toString();

                @SuppressLint("StaticFieldLeak")
                AsyncTask task = new AsyncTask() {

                    boolean verified = true;
                    boolean loginFailed = false;

                    @Override
                    protected Object doInBackground(Object[] objects) {

                        try {
                            //Verify the account and then login
                            AccountController.getInstance().verifyAccount(phoneNumber, code);

                        } catch (Exception e) {
                            verified = false;
                        }

                        try {
                            LoginHandler.getInstance().login(context, phoneNumber, pin);

                        } catch (Exception e) {
                            loginFailed = true;
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);

                        if (!verified) {
                            new VerificationDialog(context,phoneNumber, pin, VERIFICATION_RETRY_MESSAGE).show();

                        } else if (loginFailed) {
                            new RetryLoginDialog(context, phoneNumber, pin).show();
                        }
                    }
                };
                task.execute();
            }
        });

        setNegativeButton(VERIFICATION_NEGATIVE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

    }

    private class RetryLoginDialog extends AlertDialog.Builder {

        public static final String RETRY_LOGIN_TITLE = "Retry";
        public static final String RETRY_LOGIN_MESSAGE = "You're account is now verified, but a problem occurred while logging in";

        public static final String RETRY_LOGIN_POSITIVE = "Retry";
        public static final String RETRY_LOGIN_NEGATIVE = "Cancel";

        public RetryLoginDialog(final @NonNull Context context, final String phoneNumber, final String pin) {
            super(context);

            setTitle(RETRY_LOGIN_TITLE);
            setMessage(RETRY_LOGIN_MESSAGE);

            setPositiveButton(RETRY_LOGIN_POSITIVE, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    @SuppressLint("StaticFieldLeak")
                    AsyncTask task = new AsyncTask() {

                        private boolean success = true;

                        @Override
                        protected Object doInBackground(Object[] objects) {

                            try {
                                LoginHandler.getInstance().login(context, phoneNumber, pin);

                            } catch (Exception e) {
                                success = false;
                            }

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Object o) {
                            super.onPostExecute(o);

                            if (!success) {
                                new RetryLoginDialog(context, phoneNumber, pin).show();
                            }
                        }
                    };
                    task.execute();

                }
            });

            setNegativeButton(RETRY_LOGIN_NEGATIVE, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
    }
}
