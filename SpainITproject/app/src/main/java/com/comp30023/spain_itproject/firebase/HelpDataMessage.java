package com.comp30023.spain_itproject.firebase;

import android.content.Context;
import android.content.Intent;

import com.comp30023.spain_itproject.HelpRequestActivity;

public class HelpDataMessage extends DataMessage {

    public HelpDataMessage(String senderId, String messageBody) {
        super(senderId, messageBody);
    }

    @Override
    public void handle(Context context) {

        Intent intent = new Intent(context, HelpRequestActivity.class);
        intent.putExtra(HelpRequestActivity.EXTRA_MESSAGE, this);
        context.startActivity(intent);

    }
}
