package com.comp30023.spain_itproject.ui.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.firebase.realtime_database.ChatMessage;
import com.comp30023.spain_itproject.ui.LoginSharedPreference;

import java.text.ParseException;
import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context context;
    private List<ChatMessage> messages;

    public MessageListAdapter(Context context, List<ChatMessage> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = (ChatMessage) messages.get(position);

        if (message.getSenderId().equals(LoginSharedPreference.getId(context))) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessagesHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessagesHolder(view);
        }

        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = (ChatMessage) messages.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessagesHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessagesHolder) holder).bind(message);
        }
    }



    private class ReceivedMessagesHolder extends RecyclerView.ViewHolder {

        TextView messageText;
        TextView timeText;
        TextView nameText;

        public ReceivedMessagesHolder(@NonNull View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);

        }

        void bind(ChatMessage message) {
            messageText.setText(message.getMessage());

            // Format the stored timestamp into a readable String using method.
            try {
                timeText.setText(message.getTimeStamp().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            nameText.setText(message.getSenderName());
        }
    }

    private class SentMessagesHolder extends RecyclerView.ViewHolder {

        TextView messageText;
        TextView timeText;

        public SentMessagesHolder(@NonNull View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);

        }

        void bind(ChatMessage message) {
            messageText.setText(message.getMessage());

            // Format the stored timestamp into a readable String using method.
            try {
                timeText.setText(message.getTimeStamp());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}


