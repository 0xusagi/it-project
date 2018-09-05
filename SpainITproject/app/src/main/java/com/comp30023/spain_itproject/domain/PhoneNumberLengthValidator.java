package com.comp30023.spain_itproject.domain;

public class PhoneNumberLengthValidator extends DetailsValidator {

    public static final int PHONE_NUMBER_LENGTH = 10;
    public static final String INVALID_PHONE_NUMBER_MESSAGE = "Phone number is invalid, please try again";

    @Override
    public void checkDetails(String name, String phoneNumber, String pin, String confirmPin, boolean isDependent) throws InvalidDetailsException {

        if (phoneNumber.length() != PHONE_NUMBER_LENGTH) {
            throw new InvalidDetailsException(INVALID_PHONE_NUMBER_MESSAGE);
        }

    }
}
