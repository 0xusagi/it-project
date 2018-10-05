package com.comp30023.spain_itproject.domain;

import com.comp30023.spain_itproject.uicontroller.MapsService;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Location implements Serializable, DisplayName {

    @SerializedName("googleId")
    private String googleId;

    @SerializedName("latitide")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("displayName")
    private String displayName;

    public Location(String googleId, double latitude, double longitude, String displayName) {
    }

    public String getDisplayName() {
        return displayName;
    }
}
