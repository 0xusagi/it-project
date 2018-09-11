package com.comp30023.spain_itproject.validation;

import java.io.Serializable;
import java.util.ArrayList;

public class DetailsValidator implements Serializable {

    private static DetailsValidator singleton;

    private ArrayList<DetailsValidator> validators;

    public DetailsValidator() {
        validators = new ArrayList<DetailsValidator>();
    }

    public void checkDetails(String name, String phoneNumber, String pin, String confirmPin, boolean isDependent) throws InvalidDetailsException {

        for (DetailsValidator validator : validators) {
            validator.checkDetails(name, phoneNumber, pin, confirmPin, isDependent);
        }

    }

    public static DetailsValidator getInstance() {

        if (singleton == null) {
            singleton = new DetailsValidator();
            addValidator(new EqualPinsValidator());
            addValidator(new PinLengthValidator());
            addValidator(new PhoneNumberLengthValidator());
        }
        return singleton;
    }

    public static void addValidator(DetailsValidator validator) {
        singleton.validators.add(validator);
    }

}
