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

        System.out.println("Message type: " + type);
        System.out.println("Sender id: " + senderId);
        System.out.println("Sender name: " + senderName);
        System.out.println("Message: " + body);

        /*Class classType;

        switch (type) {
            case MESSAGE_TYPE_CHAT:
                message = new ChatDataMessage(senderId, senderName, body);
                classType = null;

                break;
            case MESSAGE_TYPE_HELP:
                message = new HelpDataMessage(senderId, senderName, body);
                classType = HelpRequestActivity.class;

                break;
            default:
                message = null;
                classType = null;
                break;
        }

        if (message != null) {

            Intent intent = new Intent(this, classType);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            intent.putExtra(EXTRA_MESSAGE, message);

            int uniqueInt = (int) System.currentTimeMillis() & 0xff);


            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), uniqueInt, intent,  PendingIntent.FLAG_UPDATE_CURRENT);

            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
            notificationBuilder.setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());


            message.handle(getApplicationContext());
        }*/
    }
}