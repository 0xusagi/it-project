package com.comp30023.spain_itproject.firebase;

import android.content.Context;
import android.content.Intent;

import com.comp30023.spain_itproject.ui.BroadcastActivity;
import com.comp30023.spain_itproject.ui.LoginHandler;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Class that handles FirebaseMessagingService updates when token changed or message received
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        final Context context = getApplicationContext();

        //Update token on server and locally
        LoginHandler.getInstance().updateToken(context, token);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //Check if the message contains data
        if (remoteMessage.getData().size() > 0) {

            try {

                DataMessage message = DataMessage.toDataMessage(remoteMessage.getData());
                broadcastMessage(message);

            //TODO maybe send error message to server to retry?
            } catch (InvalidMessageException e) {
            }
        }
    }

    /**
     * Broadcasts the message for the activity to handle
     * @param message The message being broadcast
     */
    private void broadcastMessage(DataMessage message) {

        Intent broadcast = new Intent();
        broadcast.setAction(BroadcastActivity.HANDLE_MESSAGE);
        broadcast.putExtra(BroadcastActivity.EXTRA_MESSAGE, message);
        sendBroadcast(broadcast);
    }

}