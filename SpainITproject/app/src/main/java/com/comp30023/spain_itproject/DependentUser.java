package com.comp30023.spain_itproject;

import java.io.Serializable;
import java.util.ArrayList;

public class DependentUser extends User implements Serializable {

    private ArrayList<Location> locations;
    private Location currentLocation;

    private ArrayList<CarerUser> carers;

    public DependentUser(String name, String phoneNumber, String pin) {
        super(name, phoneNumber, pin);

        locations = new ArrayList<Location>();
    }

    public ArrayList<Location> getLocations() {
        return locations;
    }

    public void addLocation(Location location) {
        locations.add(location);
    }

}
