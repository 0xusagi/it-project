package com.comp30023.spain_itproject;

import com.comp30023.spain_itproject.uicontroller.MapsService;

public class Location {

    private Address address;
    private Coordinates coords;
    private String tag;

    public Location(Address address, String tag) {
        this.address = address;
        this.tag = tag;

        coords = MapsService.getCoordinates(address);

    }
}
