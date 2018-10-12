package com.comp30023.spain_itproject;

import android.arch.lifecycle.LiveData;

import com.comp30023.spain_itproject.domain.ChatMessage;
import com.comp30023.spain_itproject.domain.User;

/**
 * Class to represent the interface of a ChatService to be implemented
 */
public abstract class ChatService {

    private User currentUser;
    private User chatPartner;

    /**
     * @param currentUser The user that is currently logged in
     * @param chatPartner The user that they are messaging
     */
    public ChatService(User currentUser, User chatPartner) {
        this.currentUser = currentUser;
        this.chatPartner = chatPartner;
    }

    /**
     * Returns the most recently message
     * Utilises LiveData in order to pass ownership of the data
     * @return The LiveData of the most recent ChatMessage
     */
    public abstract LiveData<ChatMessage> getLatestMessageLiveData();

    /**
     * Sends a message to the chat partner specified for this server
     * @param message The message to be sent
     */
    public abstract void sendMessage(ChatMessage message);
}
