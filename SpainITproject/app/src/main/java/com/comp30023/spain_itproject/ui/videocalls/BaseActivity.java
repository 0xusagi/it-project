package com.comp30023.spain_itproject.ui.videocalls;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.calls.videoCalls.sinch.SinchClientService;

public abstract class BaseActivity extends AppCompatActivity implements ServiceConnection {

    private SinchClientService.SinchServiceInterface sinchInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationContext().bindService(new Intent(this, SinchClientService.class), this,
            BIND_AUTO_CREATE);
    }


    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (SinchClientService.class.getName().equals(componentName.getClassName())) {
            sinchInterface = (SinchClientService.SinchServiceInterface) iBinder;
            onServiceConnected();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        if (SinchClientService.class.getName().equals(componentName.getClassName())) {
            sinchInterface = null;
            onServiceDisconnected();
        }
    }

    // To be overriden by subclasses
    protected void onServiceConnected() {

    }

    protected void onServiceDisconnected() {

    }

    protected SinchClientService.SinchServiceInterface getSinchInterface() {
        return sinchInterface;
    }
}
