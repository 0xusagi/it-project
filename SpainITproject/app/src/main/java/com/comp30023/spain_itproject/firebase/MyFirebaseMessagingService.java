package com.comp30023.spain_itproject.firebase;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;

import com.comp30023.spain_itproject.ui.LoginHandler;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static MyFirebaseMessagingService instance;

    public static final String DATA_MESSAGE_ID = "_id";
    public static final String DATA_MESSAGE_NAME = "name";
    public static final String DATA_MESSAGE_BODY = "message";
    public static final String DATA_MESAGE_TYPE = "type";
    public static final String DATA_MESSAGE_TIME = "time";

    public static final String MESSAGE_TYPE_CHAT = "chat";
    public static final String MESSAGE_TYPE_HELP = "help";

    public static MyFirebaseMessagingService getInstance() {

        if (instance == null) {
            instance = new MyFirebaseMessagingService();
        }
        return instance;
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        final String token = s;
        final Context context = getApplicationContext();

        //Update token on server and locally
        LoginHandler.getInstance().updateToken(context, token);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().size() > 0) {
            handleMessage(remoteMessage.getData());
        }
    }

    public void handleMessage(Bundle data) {

        String type = data.getString(DATA_MESAGE_TYPE);
        String senderId = data.getString(DATA_MESSAGE_ID);
        String senderName = data.getString(DATA_MESSAGE_NAME);
        String message = data.getString(DATA_MESSAGE_BODY);

        if (type != null) {
            startHandling(type, senderId, senderName, message);
        }
    }

    public void handleMessage(Map<String, String> data) {

        String type = data.get(DATA_MESAGE_TYPE);
        String senderId = data.get(DATA_MESSAGE_ID);
        String senderName = data.get(DATA_MESSAGE_NAME);
        String body = data.get(DATA_MESSAGE_BODY);

        if (type != null) {
            startHandling(type, senderId, senderName, body);
        }
    }

    private void startHandling(String type, String senderId, String senderName, String body) {

        DataMessage message;

        switch (type) {
            case MESSAGE_TYPE_CHAT:
                message = new ChatDataMessage(senderId, senderName, body);
                break;
            case MESSAGE_TYPE_HELP:
                message = new HelpDataMessage(senderId, senderName, body);
                break;
            default:
                message = null;
                break;
        }

        if (message != null) {
            message.handle(this);
        }
    }
}