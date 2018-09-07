package com.comp30023.spain_itproject.domain;

public class User {

    private String name;
    private String phoneNumber;
    private String pin;

    public User(String name, String phoneNumber, String pin) {

        this.name = name;
        this.phoneNumber = phoneNumber;
        this.pin = pin;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPin() {
        return pin;
    }
}
