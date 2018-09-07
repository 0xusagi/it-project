package com.comp30023.spain_itproject.domain;

import java.io.Serializable;

public class User implements Serializable {

    private String name;
    private String phoneNumber;
    private String pin;

    public User(String name, String phoneNumber, String pin) {

        this.name = name;
        this.phoneNumber = phoneNumber;
        this.pin = pin;

    }


}
