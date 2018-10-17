package com.comp30023.spain_itproject.ui.chat;

/**
 * Message thrown to indicate no input entered yet
 */
public class NoInputException extends Exception {

    public static final String MESSAGE = "There's nothing to send yet";

    public NoInputException() {
        super(MESSAGE);
    }

}
