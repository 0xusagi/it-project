package com.comp30023.spain_itproject.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class DependentUser extends User implements Serializable {

    @SerializedName("destinations")
    private ArrayList<Location> locations;

    private Location currentLocation;

    private ArrayList<CarerUser> carers;

    public DependentUser(String name, String phoneNumber, String pin, String id) {
        super(name, phoneNumber, pin, id);
        locations = new ArrayList<>();
    }

    public ArrayList<Location> getLocations() {
        return locations;
    }

    public void addLocation(Location location) {
        locations.add(location);
    }

    public boolean isDependent() {
        return true;
    }

}
