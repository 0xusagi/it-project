package com.comp30023.spain_itproject.firebase.cloud_messaging;

import android.content.Context;
import android.content.Intent;

import com.comp30023.spain_itproject.ui.LoginSharedPreference;
import com.comp30023.spain_itproject.ui.chat.ChatActivity;

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

        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(ChatActivity.EXTRA_CHAT_PARTNER_USER_ID, getSenderId());

        context.startActivity(intent);
    }
}
