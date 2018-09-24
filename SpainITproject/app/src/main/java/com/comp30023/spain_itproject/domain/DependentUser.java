package com.comp30023.spain_itproject.domain;

import com.comp30023.spain_itproject.uicontroller.AccountController;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class DependentUser extends User implements Serializable {

    private ArrayList<Location> locations;

    private Location currentLocation;

    private ArrayList<CarerUser> carers;

    public DependentUser(String name, String phoneNumber, String pin, String id) {
        super(name, phoneNumber, pin, id);
        locations = new ArrayList<Location>();
    }

    public ArrayList<Location> getLocations() {
        return AccountController.getLocations(getId());
    }

    public void addLocation(Location location) {
        locations.add(location);
    }

}
