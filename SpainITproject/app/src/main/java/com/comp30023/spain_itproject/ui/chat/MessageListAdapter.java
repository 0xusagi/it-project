package com.comp30023.spain_itproject.ui.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.ChatMessage;
import com.comp30023.spain_itproject.domain.User;

import java.util.List;

/**
 * Adapted for chat messages
 * Adapted from tutorial: https://blog.sendbird.com/android-chat-tutorial-building-a-messaging-ui
 */
public class MessageListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context context;
    private List<ChatMessage> messages;

    private User currentUser;

    public MessageListAdapter(Context context, List<ChatMessage> messages, User currentUser) {
        this.context = context;
        this.messages = messages;
        this.currentUser = currentUser;
    }

    /**
     * @return The current amount of messages
     */
    @Override
    public int getItemCount() {
        return messages.size();
    }

    /**
     * Determine the sender of the message
     * @param position The message being checked
     * @return Integer corresponding to the constant values
     */
    @Override
    public int getItemViewType(int position) {

        ChatMessage message = (ChatMessage) messages.get(position);

        if (message.getSenderId().equals(currentUser.getId())) {
            return VIEW_TYPE_MESSAGE_SENT;

        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    /**
     * Determines the correct ViewHolder to inflate
     * @param parent The parent view
     * @param viewType The type of the view that is being evaluated
     * @return The correct view holder for the new view
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = null;
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case VIEW_TYPE_MESSAGE_SENT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
                viewHolder = new SentMessageHolder(view);
                break;

            case VIEW_TYPE_MESSAGE_RECEIVED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
                viewHolder = new ReceivedMessageHolder(view);
                break;

        }

        return viewHolder;
    }

    /**
     * Binds message to the ViewHolder
     * @param holder The ViewHolder of the message
     * @param position The position of the message in the list
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        //Get the message in the specified position
        ChatMessage message = (ChatMessage) messages.get(position);

        ((MessageHolder) holder).bind(message);
    }
}


