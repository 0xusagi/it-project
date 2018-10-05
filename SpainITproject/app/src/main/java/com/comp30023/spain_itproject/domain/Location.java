package com.comp30023.spain_itproject.domain;

import com.comp30023.spain_itproject.uicontroller.MapsService;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Location implements Serializable, DisplayName {

    @SerializedName("id")
    private int id;

    @SerializedName("latLng")
    private LatLng latLng;

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

    public Location(LatLng latLng, Address address, String displayName) {
    }

    public String getDisplayName() {
        return displayName;
    }
}
