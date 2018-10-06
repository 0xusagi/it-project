package com.comp30023.spain_itproject.firebase;

import android.content.Context;

public class ChatDataMessage extends DataMessage {


    public ChatDataMessage(String senderId, String senderName, String messageBody) {
        super(senderId, senderName, messageBody);
    }

    @Override
    public void launchActivity(Context context) {

    }
}
