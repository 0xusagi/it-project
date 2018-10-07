package com.comp30023.spain_itproject.firebase;

import android.content.Context;
import android.content.Intent;

import com.comp30023.spain_itproject.HelpRequestActivity;
import com.comp30023.spain_itproject.ui.MessageReceivedActivity;

/**
 * The help request message that is received.
 */
public class HelpDataMessage extends DataMessage {


    public HelpDataMessage(String senderId, String senderName, String messageBody) {
        super(senderId, senderName, messageBody);
    }

    /**
     * Loads a MessageReceivedActivity
     * @param context The context from which the activity is loaded from
     */
    @Override
    public void handle(Context context) {

        Intent intent = new Intent(context, MessageReceivedActivity.class);
        context.startActivity(intent);

    }
}
