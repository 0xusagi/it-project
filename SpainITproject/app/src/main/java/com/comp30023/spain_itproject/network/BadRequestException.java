package com.comp30023.spain_itproject.network;

/**
 * An exception representing a server response is unsuccessful
 */
public class BadRequestException extends Exception {

    private int responseCode;

    public BadRequestException(ErrorResponse error) {
        super(error.getMessage());
        responseCode = error.getCode();
    }

    public int getRsponseCode() {
        return responseCode;
    }
}
