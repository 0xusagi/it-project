package com.comp30023.spain_itproject.ui;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.comp30023.spain_itproject.LoginHandler;
import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.User;
import com.comp30023.spain_itproject.ui.AccountCreationActivity;
import com.comp30023.spain_itproject.uicontroller.AccountController;

import java.io.IOException;

/**
 * Launching activity
 * Initialises the LoginSharedPreferences
 * Provides users option to sign in or create an account
 * If previously signed in, directs to appropriate HomeActivity (depending on whether signed in as a Dependent or Carer)
 */
public class StartActivity extends AppCompatActivity {

    private Button createAccountButton;
    private Button loginButton;

    /**
     * Initialises the activity and displays the layout
     * Initialises the LoginSharedPreference
     * Sets references and listeners to the buttons
     *
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if (LoginHandler.isLoggedIn(this)) {
            try {
                LoginHandler.login(this);
            } catch (IOException e) {
                return;
            }
            finish();
        }

        createAccountButton = (Button) findViewById(R.id.createAccountButton);
        setCreateAccountButtonListener(this);

        loginButton = (Button) findViewById(R.id.loginButton);
        setLoginButtonListener(this);

    }

    private void setCreateAccountButtonListener(final Context context) {
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AccountCreationActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setLoginButtonListener(final Context context) {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }
}
