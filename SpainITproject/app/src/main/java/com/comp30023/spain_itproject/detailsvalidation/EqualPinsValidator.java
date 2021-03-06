package com.comp30023.spain_itproject.detailsvalidation;

/**
 * Throws an error if the two pins used for account are unlike
 */
public class EqualPinsValidator extends DetailsValidator {

    public static final String DIFFERENT_PINS_MESSAGE = "PINs don\'t match, please try again";

    public void checkDetails(String name, String phoneNumber, String pin, String confirmPin, boolean isDependent) throws InvalidDetailsException {

        if (!pin.equals(confirmPin)) {
            throw new InvalidDetailsException(DIFFERENT_PINS_MESSAGE);
        }

    }

}
