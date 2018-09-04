package com.comp30023.spain_itproject;

import java.util.ArrayList;

public class DependentUser extends User {

    public ArrayList<Location> locations;

    public DependentUser(String name, String phoneNumber, String pin) {
        super(name, phoneNumber, pin);
    }

    public ArrayList<Location> getLocations() {
        return locations;
    }

}
