package com.comp30023.spain_itproject.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    private String userType;

    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("password")
    private String password;

    public User(String id, String name, String phoneNumber, String pin, String userType) {

        this.id = id;
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
