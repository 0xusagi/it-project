package com.comp30023.spain_itproject.domain;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public abstract class User implements DisplayName {

    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("password")
    private String password;

    public User(String name, String phoneNumber, String pin, String id) {

        this.id = id;
        this.name = name;
        this.mobile = phoneNumber;
        this.password = pin;
        this.id = id;
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

    public boolean equals(User other) {
        return mobile.equals(other.mobile) && password.equals(other.password);
    }

    public String getDisplayName() {
        return getName();
    }
}
