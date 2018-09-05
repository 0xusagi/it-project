package com.comp30023.spain_itproject.ui;

import android.accounts.Account;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.User;
import com.comp30023.spain_itproject.ui.AccountCreationActivity;
import com.comp30023.spain_itproject.uicontroller.AccountController;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);

        if (LoginSharedPreference.checkLogIn(this)) {

            Intent intent;

            String phoneNumber = LoginSharedPreference.getPhoneNumber(this);
            String pin = LoginSharedPreference.getPin(this);
            boolean isDependent = LoginSharedPreference.getIsDependent(this);

            User user;
            user = AccountController.login(phoneNumber, pin, isDependent);

            if (isDependent) {
                intent = new Intent(this, DependentHomeActivity.class);
            } else {
                intent = new Intent(this, CarerHomeActivity.class);
            }

            intent.putExtra("PASSED_USER", user);
            startActivity(intent);
        }
    }

    public void createAccountClick(View view) {
        Intent intent = new Intent(this, AccountCreationActivity.class);
        startActivity(intent);
    }
}
