package com.comp30023.spain_itproject.firebase;

import android.content.Context;

import com.comp30023.spain_itproject.ui.LoginHandler;
import com.google.firebase.messaging.FirebaseMessagingService;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        final String token = s;
        final Context context = getApplicationContext();

        //Update token on server and locally
        LoginHandler.getInstance().updateToken(context, token);

    }

}