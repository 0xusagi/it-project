package com.comp30023.spain_itproject.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class CarerUser extends User implements Serializable {

    private static final String userType = "Carer";

    @SerializedName("dependents")
    private ArrayList<DependentUser> dependents;

    public CarerUser(String name, String phoneNumber, String pin) {
        super(name, phoneNumber, pin, userType);
    }
}
