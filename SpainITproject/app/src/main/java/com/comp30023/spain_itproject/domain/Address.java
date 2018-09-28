package com.comp30023.spain_itproject.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Address implements Serializable {


    @SerializedName("addressLine1")
    private String addressLine1;

    @SerializedName("addressLine2")
    private String addressLine2;

    @SerializedName("postCode")
    private int postCode;

    @SerializedName("city")
    private String city;

    @SerializedName("state")
    private String state;

    public Address(String addressLine1, String addressLine2, int postCode, String city, String suburb, String state) {
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.postCode = postCode;
        this.city = city;
        this.state = state;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }


}
