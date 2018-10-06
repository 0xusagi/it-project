package com.comp30023.spain_itproject.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;

import com.comp30023.spain_itproject.HelpRequestActivity;
import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.ui.BroadcastActivity;
import com.comp30023.spain_itproject.ui.LoginHandler;
import com.comp30023.spain_itproject.ui.MessageReceivedActivity;
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

    public static final String EXTRA_MESSAGE = "MESSAGE";

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

            DataMessage message = toDataMessage(remoteMessage.getData());
            broadcastMessage(message);

        }
    }

    private void broadcastMessage(DataMessage message) {
        Intent broadcast = new Intent();
        broadcast.setAction(BroadcastActivity.OPEN_NEW_ACTIVITY);
        sendBroadcast(broadcast);
    }

    public static DataMessage toDataMessage(String type, String senderId, String senderName, String message) {


        switch (type) {
            case MESSAGE_TYPE_CHAT:
                return new ChatDataMessage(senderId, senderName, message);
            case MESSAGE_TYPE_HELP:
                return new HelpDataMessage(senderId, senderName, message);
            default:
                return new DataMessage(senderId, senderName, message);
        }

    }

    public static DataMessage toDataMessage(Bundle data) {

        String type = data.getString(DATA_MESAGE_TYPE);
        String senderId = data.getString(DATA_MESSAGE_ID);
        String senderName = data.getString(DATA_MESSAGE_NAME);
        String message = data.getString(DATA_MESSAGE_BODY);

        return toDataMessage(type, senderId, senderName, message);
    }

    public static DataMessage toDataMessage(Map<String, String> data) {

        String type = data.get(DATA_MESAGE_TYPE);
        String senderId = data.get(DATA_MESSAGE_ID);
        String senderName = data.get(DATA_MESSAGE_NAME);
        String message = data.get(DATA_MESSAGE_BODY);

        return toDataMessage(type, senderId, senderName, message);
    }
}