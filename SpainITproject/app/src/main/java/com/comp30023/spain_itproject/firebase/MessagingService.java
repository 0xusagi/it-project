package com.comp30023.spain_itproject.firebase;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.comp30023.spain_itproject.uicontroller.AccountController;
import com.google.firebase.messaging.FirebaseMessagingService;


public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        final String token = s;

        System.out.println("MessagingService - Token: " + token);
        /*@SuppressLint("StaticFieldLeak") AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {

                AccountController.getInstance().updateToken(token);
                return null;
            }
        };*/
    }
}