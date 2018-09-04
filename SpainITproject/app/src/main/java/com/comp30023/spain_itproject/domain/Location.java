package com.comp30023.spain_itproject.domain;

import com.comp30023.spain_itproject.uicontroller.MapsService;

import java.io.Serializable;

public class Location implements Serializable {

    private Address address;
    private Coordinates coords;
    private String tag;

    public Location(Address address, String tag) {
        this.address = address;
        this.tag = tag;

        coords = MapsService.getCoordinates(address);

    }

    public String getTag() {
        return tag;
    }
}
