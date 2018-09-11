package com.comp30023.spain_itproject.validation;

public class PinLengthValidator extends DetailsValidator {

    public static int PIN_LENGTH = 4;
    public static final String INVALID_PIN_LENGTH_MESSAGE = "PIN must be " + PIN_LENGTH + " digits, please try again";

    public void checkDetails(String name, String phoneNumber, String pin, String confirmPin, boolean isDependent) throws InvalidDetailsException {

        if (pin.length() != PIN_LENGTH) {
            throw new InvalidDetailsException(INVALID_PIN_LENGTH_MESSAGE);
        }

    }

}
