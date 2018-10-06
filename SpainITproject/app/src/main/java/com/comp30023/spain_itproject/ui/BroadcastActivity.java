package com.comp30023.spain_itproject.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import com.comp30023.spain_itproject.ui.MessageReceivedActivity;

public class BroadcastActivity extends AppCompatActivity {

    public static final String OPEN_NEW_ACTIVITY = "OPEN_NEW_ACTIVITY";

    BroadcastReceiver receiver;
    IntentFilter intentFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Context activityContext = this;

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                System.out.println("Broadcast received");
                Intent newIntent = new Intent(activityContext, MessageReceivedActivity.class);
                startActivity(newIntent);
            }
        };

        intentFilter = new IntentFilter(OPEN_NEW_ACTIVITY);

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
