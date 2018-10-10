package com.comp30023.spain_itproject.domain;

import com.comp30023.spain_itproject.firebase.realtime_database.ChatMessage;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public abstract class User implements DisplayName {

    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("password")
    private String password;

    private Map<String, PriorityQueue<ChatMessage>> chats;

    private List<String> chatIds;

    public User(String name, String phoneNumber, String pin, String id) {

        this.id = id;
        this.name = name;
        this.mobile = phoneNumber;
        this.password = pin;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPhoneNumber() {
        return mobile;
    }

    public boolean equals(User other) {
        return mobile.equals(other.mobile) && password.equals(other.password);
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    public List<String> getChatIds() {

        if (chatIds == null) {
            chatIds = new ArrayList<String>();
        }

        return chatIds;
    }

    public void putMessage(ChatMessage message) {

        String otherUserId;

        if (id.equals(message.getSenderId())) {
            otherUserId = message.getReceiverId();
        } else {
            otherUserId = message.getSenderId();
        }

        if (!chats.containsKey(otherUserId)) {
            chats.put(otherUserId, new PriorityQueue<ChatMessage>());
        }

        PriorityQueue<ChatMessage> messages = chats.get(otherUserId);

        boolean messageFound = false;
        for (ChatMessage existingMessage : messages) {

            if (existingMessage.equals(message)) {
                messageFound = true;
                break;
            }
        }

        if (!messageFound) {
            messages.add(message);
        }
    }

    public abstract void setChatListeners();
}
