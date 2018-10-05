package com.comp30023.spain_itproject.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Coordinates implements Serializable {

    @SerializedName("lat")
    private float lat;

    @SerializedName("lng")
    private float lng;

    private Coordinates(float lat, float lng) {
        this.lat = lat;
        this.lng = lng;
    }

}
