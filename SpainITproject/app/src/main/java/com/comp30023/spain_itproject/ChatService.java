package com.comp30023.spain_itproject;

import android.arch.lifecycle.LiveData;

import com.comp30023.spain_itproject.domain.ChatMessage;
import com.comp30023.spain_itproject.domain.User;

import java.io.File;

/**
 * Class to represent the interface of a ChatService to be implemented
 */
public abstract class ChatService {

    private String currentUserId;
    private boolean isDependent;
    private String chatPartnerId;

    /**
     * @param currentUserId The user that is currently logged in
     * @param chatPartnerId The user that they are messaging
     */
    public ChatService(String currentUserId, String chatPartnerId) {
        this.currentUserId = currentUserId;
        this.chatPartnerId = chatPartnerId;
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
    public abstract void sendMessage(String message) throws Exception;

    public abstract void sendAudioMessage(String message, File file) throws Exception;

    public abstract void playAudioMessage(String resourceLink);
}
