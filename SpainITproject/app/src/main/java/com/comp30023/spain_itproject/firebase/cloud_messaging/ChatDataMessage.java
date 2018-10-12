package com.comp30023.spain_itproject.firebase.cloud_messaging;

import android.content.Context;

/**
 * The chat message that is received.
 */
public class ChatDataMessage extends DataMessage {


    public ChatDataMessage(String senderId, String senderName, String messageBody) {
        super(senderId, senderName, messageBody);
    }

    //TODO
    @Override
    public void handle(Context context) {

    }
}
