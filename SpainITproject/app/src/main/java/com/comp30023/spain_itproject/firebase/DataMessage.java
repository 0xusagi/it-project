package com.comp30023.spain_itproject.firebase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;
import java.util.Map;

/**
 * Representation of the structure of message received from another user
 */
public abstract class DataMessage implements Serializable {

    public static final String DATA_MESSAGE_ID = "_id";
    public static final String DATA_MESSAGE_NAME = "name";
    public static final String DATA_MESSAGE_BODY = "message";
    public static final String DATA_MESAGE_TYPE = "type";
    public static final String DATA_MESSAGE_TIME = "time";

    public static final String MESSAGE_TYPE_CHAT = "chat";
    public static final String MESSAGE_TYPE_HELP = "help";


    private String senderId;
    private String senderName;
    private String messageBody;

    public DataMessage(String senderId, String senderName, String messageBody) {

        this.senderId = senderId;
        this.senderName = senderName;
        this.messageBody = messageBody;
    }

    /**
     * Activate the logic associated with receiving this message
     * @param context The context of which activities can be started from
     */
    public abstract void handle(Context context);

    public String getSenderId() {
        return senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getMessageBody() {
        return messageBody;
    }

    /**
     * Formats data into DataMessage object
     * @param type Type of user to be created
     * @param senderId Sender of the message
     * @param senderName Name of the sender
     * @param message The associated message
     * @return The created DataMessage instance
     * @throws InvalidMessageException Thrown when invalid type or null type
     */
    public static DataMessage toDataMessage(String type, String senderId, String senderName, String message)
            throws InvalidMessageException {

        if (type != null) {
            switch (type) {
                case MESSAGE_TYPE_CHAT:
                    return new ChatDataMessage(senderId, senderName, message);
                case MESSAGE_TYPE_HELP:
                    return new HelpDataMessage(senderId, senderName, message);
                default:
                    throw new InvalidMessageException();
            }
        } else {
            throw new InvalidMessageException();
        }
    }

    /**
     * Formats data into DataMessage object
     * @param data The collection of data from which to create the instance from
     * @return The created DataMessage instance
     * @throws InvalidMessageException Thrown when invalid type or null type
     */
    public static DataMessage toDataMessage(Bundle data) throws InvalidMessageException {

        String type = data.getString(DATA_MESAGE_TYPE);
        String senderId = data.getString(DATA_MESSAGE_ID);
        String senderName = data.getString(DATA_MESSAGE_NAME);
        String message = data.getString(DATA_MESSAGE_BODY);

        return toDataMessage(type, senderId, senderName, message);
    }

    /**
     * Formats data into DataMessage object
     * @param data The collection of data from which to create the instance from
     * @return The created DataMessage instance
     * @throws InvalidMessageException Thrown when invalid type or null type
     */
    public static DataMessage toDataMessage(Map<String, String> data) throws InvalidMessageException {

        String type = data.get(DATA_MESAGE_TYPE);
        String senderId = data.get(DATA_MESSAGE_ID);
        String senderName = data.get(DATA_MESSAGE_NAME);
        String message = data.get(DATA_MESSAGE_BODY);

        return toDataMessage(type, senderId, senderName, message);
    }

    /**
     * Formats data (extras from intent) into DataMessage object
     * @param intent The intent containing the extras of which to create the instance from
     * @return The created DataMessage instance
     * @throws InvalidMessageException Thrown when invalid type or null type
     */
    public static DataMessage toDataMessage(Intent intent) throws InvalidMessageException {

        String type = intent.getStringExtra(DATA_MESAGE_TYPE);
        intent.removeExtra(DATA_MESAGE_TYPE);

        String senderId = intent.getStringExtra(DATA_MESSAGE_ID);
        intent.removeExtra(DATA_MESSAGE_ID);

        String senderName = intent.getStringExtra(DATA_MESSAGE_NAME);
        intent.removeExtra(DATA_MESSAGE_NAME);

        String message = intent.getStringExtra(DATA_MESSAGE_BODY);
        intent.removeExtra(DATA_MESSAGE_BODY);

        return toDataMessage(type, senderId, senderName, message);

    }

}
