package com.comp30023.spain_itproject;

import android.arch.lifecycle.LiveData;

import com.comp30023.spain_itproject.domain.ChatMessage;
import com.comp30023.spain_itproject.domain.User;
import com.comp30023.spain_itproject.network.BadRequestException;
import com.comp30023.spain_itproject.network.NoConnectionException;

import java.io.File;

/**
 * Class to represent the interface of a ChatService to be implemented
 */
public abstract class ChatService {

    public String getCurrentUserId() {
        return currentUserId;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    public String getChatPartnerId() {
        return chatPartnerId;
    }

    private String currentUserId;
    private String currentUserName;
    private String chatPartnerId;

    /**
     * @param currentUserId The user that is currently logged in
     * @param currentUserName The name of the user that is currently logged in
     * @param chatPartnerId The user that they are messaging
     */
    public ChatService(String currentUserId, String currentUserName, String chatPartnerId) {
        this.currentUserId = currentUserId;
        this.chatPartnerId = chatPartnerId;
        this.currentUserName = currentUserName;
    }

    /**
     * Returns the most recent message
     * Utilises LiveData in order to pass ownership of the data
     * @return The LiveData of the most recent ChatMessage
     */
    public abstract LiveData<ChatMessage> getLatestMessageLiveData();

    /**
     * Sends a message to the chat partner specified for this server
     * @param message The message to be sent
     */
    public abstract void sendMessage(String message) throws BadRequestException, NoConnectionException;

    /**
     * Send a message with a reference to an audio file
     * @param message The text body of the message
     * @param file The audio file that is being shared
     * @throws Exception Thrown when there is a connection issue
     */
    public abstract void sendAudioMessage(String message, File file);

    /**
     * Play the audio file found at the resource link
     * @param resourceLink The path to the audio file
     */
    public abstract void playAudioMessage(String resourceLink);
}
