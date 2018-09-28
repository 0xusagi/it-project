package com.comp30023.spain_itproject.detailsvalidation;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Checks details for a new user against constraints and throws an error if these aren't upheld
 * Implements the singleton and composition GoF patterns
 */
public class DetailsValidator implements Serializable {

    private static DetailsValidator singleton;

    private ArrayList<DetailsValidator> validators;

    public DetailsValidator() {
        validators = new ArrayList<DetailsValidator>();
    }

    /**
     * Checks details against multiple constraints
     * @param name
     * @param phoneNumber
     * @param pin
     * @param confirmPin
     * @param isDependent
     * @throws InvalidDetailsException
     */
    public void checkDetails(String name, String phoneNumber, String pin, String confirmPin, boolean isDependent) throws InvalidDetailsException {

        for (DetailsValidator validator : validators) {
            validator.checkDetails(name, phoneNumber, pin, confirmPin, isDependent);
        }
    }

    public static DetailsValidator getInstance() {

        if (singleton == null) {
            singleton = new DetailsValidator();
            singleton.addValidator(new EqualPinsValidator());
            singleton.addValidator(new PinLengthValidator());
            singleton.addValidator(new PhoneNumberLengthValidator());
        }
        return singleton;
    }

    public void addValidator(DetailsValidator validator) {
        validators.add(validator);
    }

    public ArrayList<DetailsValidator> getValidators() {
        return validators;
    }

}
