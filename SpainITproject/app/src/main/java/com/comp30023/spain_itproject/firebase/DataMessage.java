package com.comp30023.spain_itproject.firebase;

import android.content.Context;

import java.io.Serializable;

public abstract class DataMessage implements Serializable {

    private String senderId;
    private String messageBody;

    public DataMessage(String senderId, String messageBody) {

        this.senderId = senderId;
        this.messageBody = messageBody;
    }

    public abstract void handle(Context context);

}
