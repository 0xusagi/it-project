package com.comp30023.spain_itproject.domain;

import android.support.annotation.NonNull;

import com.comp30023.spain_itproject.Clock;
import com.google.gson.internal.bind.util.ISO8601Utils;

import java.io.Serializable;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;

/**
 * A class to represent a chat message sent amongst two users
 */
public class ChatMessage implements Serializable, Comparable<ChatMessage> {

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setAudioResourceLink(String audioResourceLink) {
        this.audioResourceLink = audioResourceLink;
    }

    private String message;
    private String senderId;

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    private String senderName;
    private String receiverId;
    private String timeStamp;
    private String audioResourceLink;

    public ChatMessage(String senderId, String senderName, String receiverId, String message, String audioResourceLink) {

        this.senderId = senderId;
        this.senderName = senderName;
        this.receiverId = receiverId;
        this.message = message;
        this.audioResourceLink = audioResourceLink;

        timeStamp = Clock.getCurrentLocalTimeStamp();
    }

    public ChatMessage(){

    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getMessage() {
        return message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getAudioResourceLink() {
        return audioResourceLink;
    }

    /**
     * Compares this instance of a ChatMessage to the parameter
     * @param other The other chat message
     * @return 1 if this chat message was sent prior to the other chat message,
     *      -1 if this chat message was sent after the other chat message
     *      0 if both chat messages were sent at the same instant
     */
    @Override
    public int compareTo(@NonNull ChatMessage other) {

        try {
            Date date1 = ISO8601Utils.parse(timeStamp, new ParsePosition(0));
            Date date2 = ISO8601Utils.parse(other.timeStamp, new ParsePosition(0));

            return -1*date1.compareTo(date2);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Chat messages can be considered equivalent if they have the same sender at the same instant
     * @param obj The other chat message being compared against
     * @return True if the messages are equal
     */
    public boolean equals(ChatMessage obj) {

        try {
            Date date1 = ISO8601Utils.parse(timeStamp, new ParsePosition(0));
            Date date2 = ISO8601Utils.parse(obj.timeStamp, new ParsePosition(0));

            return senderId.equals(obj.senderId) && date1.compareTo(date2) != 0;

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return false;
    }
}
