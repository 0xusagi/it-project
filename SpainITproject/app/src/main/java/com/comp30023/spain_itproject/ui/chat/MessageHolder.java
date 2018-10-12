package com.comp30023.spain_itproject.ui.chat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.ChatMessage;

import java.text.ParseException;

/**
 * A view holder for messages that display the same attributes
 * Adapted from tutorial: https://blog.sendbird.com/android-chat-tutorial-building-a-messaging-ui
 */
public abstract class MessageHolder extends RecyclerView.ViewHolder {

    TextView messageText;
    TextView timeText;

    public MessageHolder(@NonNull View itemView) {
        super(itemView);

        messageText = (TextView) itemView.findViewById(R.id.text_message_body);
        timeText = (TextView) itemView.findViewById(R.id.text_message_time);

    }

    void bind(ChatMessage message) {
        messageText.setText(message.getMessage());

        try {
            timeText.setText(message.getTimeStamp());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
