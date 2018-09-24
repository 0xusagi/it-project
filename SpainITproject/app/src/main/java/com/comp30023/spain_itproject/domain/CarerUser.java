package com.comp30023.spain_itproject.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class CarerUser extends User implements Serializable {

    @SerializedName("dependents")
    private ArrayList<DependentUser> dependents;

    public CarerUser(String name, String phoneNumber, String pin, String id) {
        super(name, phoneNumber, pin, id);
    }

    public ArrayList<DependentUser> getDependents() {
        return dependents;
    }

    public boolean isDependent() {
        return false;
    }
}
