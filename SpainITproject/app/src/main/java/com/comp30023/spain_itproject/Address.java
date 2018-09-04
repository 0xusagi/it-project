package com.comp30023.spain_itproject;

public class Address {

    private String number;
    private String streetName;
    private String suburb;
    private State state;

    public Address(String number, String streetName, String suburb, State state) {
        this.number = number;
        this.streetName = streetName;
        this.suburb = suburb;
        this.state = state;
    }

    public String getNumber() {
        return number;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getSuburb() {
        return suburb;
    }

    public State getState() {
        return state;
    }


}
