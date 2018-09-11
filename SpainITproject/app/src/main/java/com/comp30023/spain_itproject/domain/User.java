package com.comp30023.spain_itproject.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("pin")
    private String pin;

    public User(String name, String phoneNumber, String pin) {

        this.name = name;
        this.phoneNumber = phoneNumber;
        this.pin = pin;

    }


}
