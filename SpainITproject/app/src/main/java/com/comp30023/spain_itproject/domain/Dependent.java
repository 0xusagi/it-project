package com.comp30023.spain_itproject.domain;

/**
 * Dependents have a pin but the pin which is returned when taken from a server is hashed
 * so, it is possible to return the hashed pin along with name and phone number to a dependent who
 * requests the information
 */
public class Dependent extends User {

    public Dependent(String name, String phoneNumber, String pin) {
        super(name, phoneNumber, pin);
    }
}
