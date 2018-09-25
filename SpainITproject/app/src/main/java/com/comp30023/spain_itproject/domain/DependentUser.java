package com.comp30023.spain_itproject.domain;

import com.comp30023.spain_itproject.uicontroller.AccountController;
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
        locations = new ArrayList<Location>();
        carers = new ArrayList<CarerUser>();
    }

    public ArrayList<Location> getLocations() {
        return locations;
    }

    public ArrayList<CarerUser> getCarers() {
        return carers;
    }

    public void addLocation(Location location) {
        locations.add(location);
    }

    public boolean isDependent() {
        return true;
    }

    public void addCarer(CarerUser carer) {
        carers.add(carer);
    }
}
