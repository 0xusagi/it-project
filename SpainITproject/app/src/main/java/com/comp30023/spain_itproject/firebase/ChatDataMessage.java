package com.comp30023.spain_itproject.firebase;

import android.content.Context;

public class ChatDataMessage extends DataMessage {


    public ChatDataMessage(String senderId, String messageBody) {
        super(senderId, messageBody);
    }

    @Override
    public void handle(Context context) {

    }
}
