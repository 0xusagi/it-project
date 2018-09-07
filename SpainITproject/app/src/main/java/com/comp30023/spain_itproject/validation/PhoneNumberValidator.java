package com.comp30023.spain_itproject.validation;

public class PhoneNumberValidator {

    private static final int PHONE_NUMBER_LENGTH = 10;

    // Check if the phone number length is valid
    public static boolean isValidNumber(String phoneNumber) {

        if (phoneNumber.length() != PHONE_NUMBER_LENGTH) {
            return false;
        }

        return true;
    }
}
