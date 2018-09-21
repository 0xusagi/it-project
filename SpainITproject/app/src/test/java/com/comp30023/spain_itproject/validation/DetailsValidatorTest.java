package com.comp30023.spain_itproject.validation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class DetailsValidatorTest {
    DetailsValidator validator;

    private String invalidPhoneNumber = "0303";
    private String name = "John";
    private String validPhoneNumber = "0403928394";
    private String pin = "1111";
    private String validConfirmPin = "1111";
    private String invalidConfirmPin = "1234";
    private boolean isDependent = true;

    @Before
    public void setUp() throws Exception {
        validator = new DetailsValidator().getInstance();
    }

    // Testing that an exception is thrown when there is an invalid phone number
    @Test (expected = InvalidDetailsException.class)
    public void checkInvalidPhoneNumber() throws InvalidDetailsException {
        validator.checkDetails(name, invalidPhoneNumber, pin, validConfirmPin, isDependent);
    }

    // Testing that an exception is thrown when there is an invalid confirm pin
    @Test (expected = InvalidDetailsException.class)
    public void checkInvalidConfirmPin() throws InvalidDetailsException {
        validator.checkDetails(name, validPhoneNumber, pin, invalidConfirmPin, isDependent);
    }

    // Testing the details are valid an no exception is thrown
    @Test
    public void checkValidDetails() {
        try {
            validator.checkDetails(name, validPhoneNumber, pin, validConfirmPin, isDependent);
        } catch (InvalidDetailsException e) {
            e.printStackTrace();
        }
    }

    // Testing add validators
    @Test
    public void addValidator() {
        int initialValidatorsCount = validator.getValidators().size();

        // Add a new validator
        validator.addValidator(Mockito.mock(DetailsValidator.class));

        // Check the validator is added
        assertEquals(validator.getValidators().size(), initialValidatorsCount + 1);
    }
}