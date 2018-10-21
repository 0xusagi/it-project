package com.comp30023.spain_itproject.domain;

import com.comp30023.spain_itproject.uicontroller.MapsService;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * This is our own personal representation of Locations that co-ordinates
 * with the Google standard for locations in their places SDK.
 */
public class Location implements Serializable, DisplayName {


    // Mongo id.
    @SerializedName("_id")
    private String _id;

    @SerializedName("googleId")
    private String googleId;

    @SerializedName("lat")
    private double latitude;

    @SerializedName("long")
    private double longitude;

    @SerializedName("displayName")
    private String displayName;

    public Location(String googleId, double latitude, double longitude, String displayName) {
    }

    public String getGoogleId() {
        return googleId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String get_id() {
        return _id;
    }

}
