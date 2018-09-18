package com.comp30023.spain_itproject.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    private String userType;

    private String id;

    private String name;

    private String mobile;

    private String password;

    public User(String name, String phoneNumber, String pin, String userType) {

        this.name = name;
        this.mobile = phoneNumber;
        this.password = pin;
        this.userType = userType;
    }

    public String getName() {
        return name;
    }


    public String getId() {
        return id;
    }

    public String getPhoneNumber() {
        return mobile;
    }

    public String getPin() {
        return password;
    }

    public boolean equals(User other) {
        return mobile.equals(other.mobile) && password.equals(other.password);
    }
}
