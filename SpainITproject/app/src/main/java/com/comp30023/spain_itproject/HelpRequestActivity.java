package com.comp30023.spain_itproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HelpRequestActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_request);
    }
}
