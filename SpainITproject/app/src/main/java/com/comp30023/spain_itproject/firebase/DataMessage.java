package com.comp30023.spain_itproject.firebase;

import android.content.Context;

import java.io.Serializable;

public class DataMessage implements Serializable {

    private String senderId;
    private String senderName;
    private String messageBody;

    public DataMessage(String senderId, String senderName, String messageBody) {

        this.senderId = senderId;
        this.messageBody = messageBody;
    }

    public void launchActivity(Context context) {

    }

    public String getSenderId() {
        return senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getMessageBody() {
        return messageBody;
    }

}
