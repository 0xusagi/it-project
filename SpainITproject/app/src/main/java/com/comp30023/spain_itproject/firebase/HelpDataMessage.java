package com.comp30023.spain_itproject.firebase;

import android.content.Context;
import android.content.Intent;

import com.comp30023.spain_itproject.HelpRequestActivity;
import com.comp30023.spain_itproject.ui.MessageReceivedActivity;

public class HelpDataMessage extends DataMessage {

    public HelpDataMessage(String senderId, String senderName, String messageBody) {
        super(senderId, senderName, messageBody);
    }

    @Override
    public void launchActivity(Context context) {

        Intent intent = new Intent(context, MessageReceivedActivity.class);
        intent.putExtra(HelpRequestActivity.EXTRA_MESSAGE, this);
        context.startActivity(intent);

    }
}
