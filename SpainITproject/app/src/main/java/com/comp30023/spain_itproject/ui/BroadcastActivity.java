package com.comp30023.spain_itproject.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.comp30023.spain_itproject.firebase.cloud_messaging.DataMessage;

/**
 * An activity that extends from this receives broadcasts regarding message handling
 */
public abstract class BroadcastActivity extends AppCompatActivity {

    public static final String HANDLE_MESSAGE = "HANDLE_MESSAGE";

    public static final String EXTRA_MESSAGE = "MESSAGE";

    BroadcastReceiver receiver;
    IntentFilter intentFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Context activityContext = this;

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //Extract the message from the intent and handle
                DataMessage message = (DataMessage) intent.getSerializableExtra(EXTRA_MESSAGE);
                message.handle(activityContext);
            }
        };

        intentFilter = new IntentFilter(HANDLE_MESSAGE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {

        if (receiver != null) {
            unregisterReceiver(receiver);
        }

        super.onPause();
    }
}
