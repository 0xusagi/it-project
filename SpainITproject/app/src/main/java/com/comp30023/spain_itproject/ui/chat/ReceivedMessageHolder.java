package com.comp30023.spain_itproject.ui.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.ChatMessage;

/**
 * A ViewHolder for a received message
 */
public class ReceivedMessageHolder extends MessageHolder {

    TextView nameText;

    public ReceivedMessageHolder(@NonNull View itemView) {
        super(itemView);

        nameText = (TextView) itemView.findViewById(R.id.text_message_name);
    }

    void bind(Context context, ChatMessage message) {
        super.bind(context, message);
        //nameText.setText(message.getSenderName());
    }
}