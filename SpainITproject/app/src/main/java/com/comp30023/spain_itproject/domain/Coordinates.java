package com.comp30023.spain_itproject.domain;

import java.io.Serializable;

public class Coordinates implements Serializable {

    private float lat;
    private float lon;

    private Coordinates(float lat, float lon) {
        this.lat = lat;
        this.lon = lon;
    }

}
