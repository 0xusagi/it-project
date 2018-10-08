package com.comp30023.spain_itproject.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.firebase.DataMessage;
import com.comp30023.spain_itproject.firebase.InvalidMessageException;
import com.comp30023.spain_itproject.firebase.MyFirebaseMessagingService;

import java.nio.file.Path;

/**
 * Launching activity
 * Initialises the LoginSharedPreferences
 * Provides users option to sign in or create an account
 * If previously signed in, directs to appropriate HomeActivity (depending on whether signed in as a Dependent or Carer)
 */
public class StartActivity extends BroadcastActivity {

    private Button createAccountButton;
    private Button loginButton;

    /**
     * Initialises the activity and displays the layout
     * Initialises the LoginSharedPreference
     * Sets references and listeners to the buttons
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        createAccountButton = findViewById(R.id.createAccountButton);
        setCreateAccountButtonListener(this);

        loginButton = findViewById(R.id.start_loginButton);
        setLoginButtonListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();

        // Login the user if the user has already logged in without logging out
        if (LoginHandler.getInstance().isLoggedIn(this)) {

            //FCM messages when in background pass data component to launch activity
            //Check if data received when in background
            try {
                DataMessage message = DataMessage.toDataMessage(getIntent());
                message.handle(this);

                //When extras does not contain a valid message, just continue with login
            } catch (InvalidMessageException e) {
                LoginHandler.getInstance().continueLogin(this);
            }
        }

    }

    private void setCreateAccountButtonListener(final Context context) {
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AccountCreationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setLoginButtonListener(final Context context) {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
