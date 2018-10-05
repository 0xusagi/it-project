package com.comp30023.spain_itproject.network;

/**
 * A structure that corresponds to an error response from the server
 */
public class ErrorResponse {

    private String message;

    private int code;

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

}
