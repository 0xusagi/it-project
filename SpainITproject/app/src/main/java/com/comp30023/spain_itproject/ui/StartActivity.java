package com.comp30023.spain_itproject.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.ui.AccountCreationActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void createAccountClick(View view) {
        Intent intent = new Intent(this, AccountCreationActivity.class);
        startActivity(intent);
    }
}
