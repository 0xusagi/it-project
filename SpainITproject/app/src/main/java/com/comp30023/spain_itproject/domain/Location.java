package com.comp30023.spain_itproject.domain;

import com.comp30023.spain_itproject.uicontroller.MapsService;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Location implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("address")
    private Address address;

    private Coordinates coords;

    @SerializedName("displayName")
    private String displayName;

    @SerializedName("description")
    private String description;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("popularity")
    private float popularity;

    public Location(Address address, String displayName) {
        this.address = address;
        this.displayName = displayName;

        coords = MapsService.getCoordinates(address);

    }

    public String getDisplayName() {
        return displayName;
    }
}
