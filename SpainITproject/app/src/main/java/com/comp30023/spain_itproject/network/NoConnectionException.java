package com.comp30023.spain_itproject.network;

/**
 * An exception representing the failure of a connection attempt
 */
public class NoConnectionException extends Exception {

    public static final String MESSAGE_SERVER_FAILURE = "That request did't go through.\nCheck your internet connection and please try again.";

    public NoConnectionException() {
        super(MESSAGE_SERVER_FAILURE);
    }

}
