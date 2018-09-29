package com.comp30023.spain_itproject.domain;

import com.comp30023.spain_itproject.uicontroller.AccountController;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Extends user by storing lists of objects related to being a Dependent user
 */
public class DependentUser extends User {

    @SerializedName("destinations")
    private ArrayList<Location> locations;

    private Location currentLocation;

    private ArrayList<CarerUser> carers;

    private ArrayList<CarerUser> pendingCarers;

    public DependentUser(String name, String phoneNumber, String pin, String id) {
        super(name, phoneNumber, pin, id);

        locations = new ArrayList<Location>();
        carers = new ArrayList<CarerUser>();
    }

    /**
     * @return The list of locations associated with the dependent account
     */
    public ArrayList<Location> getLocations() {
        return locations;
    }

    /**
     * @return The list of carers associated with the dependent account
     */
    public ArrayList<CarerUser> getCarers() {
        return carers;
    }

    /**
     * @return The list of carers that have requested to aid this dependent
     */
    public ArrayList<CarerUser> getPendingCarers() {
        return pendingCarers;
    }

    /**
     * Adds a location to a Dependent's list locally and externally
     * @param location The location to be added
     * @throws Exception Thrown if there is an error when communicating with the server
     */
    public void addLocation(Location location) throws Exception {

        //Add location to dependent on the database
        //Throws exception if error occurs
        //If this statement not completed, does not add the location locally
        AccountController.getInstance().addLocationToDependent(this, location);

        //Add location to dependent locally so consistent
        locations.add(location);
    }

    /**
     * Responds to the request of a CarerUser to be one of their dependents
     * @param carer The CarerUser that has made the request
     * @param accept The response to the request
     * @throws Exception Thrown if there is an issue when contacting the server
     */
    public void respondToRequest(CarerUser carer, boolean accept) throws Exception {

        //Shift the carer from pending to accepted on the server
        AccountController.getInstance().acceptCarer(this, carer, accept);

        //Shift the carer from pending to accepted locally
        pendingCarers.remove(carer);
        carers.add(carer);

    }
}
