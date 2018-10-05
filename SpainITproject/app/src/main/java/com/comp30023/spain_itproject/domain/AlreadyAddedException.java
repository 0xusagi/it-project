package com.comp30023.spain_itproject.domain;

public class AlreadyAddedException extends Exception {

    public static final String MESSAGE = "This dependent has already received their request";

    public AlreadyAddedException() {
        super(MESSAGE);
    }

}
