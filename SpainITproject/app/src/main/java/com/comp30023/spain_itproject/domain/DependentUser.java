package com.comp30023.spain_itproject.domain;

import com.comp30023.spain_itproject.uicontroller.AccountController;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Extends user by storing lists of objects related to being a Dependent user
 */
public class DependentUser extends User {

    @SerializedName("destinations")
    private ArrayList<Location> locations;

    private Location currentLocation;

    //Server responds with list of ids (Strings) of carers
    private ArrayList<String> carers;
    private ArrayList<CarerUser> confirmedCarers;

    //Server responds with list of ids (Strings of pending carers
    private ArrayList<String> pendingCarers;
    private ArrayList<CarerUser> pCarers;

    public DependentUser(String name, String phoneNumber, String pin, String id) {
        super(name, phoneNumber, pin, id);

        locations = new ArrayList<Location>();
        confirmedCarers = new ArrayList<CarerUser>();
    }

    /**
     * @return The list of locations associated with the dependent account
     */
    public ArrayList<Location> getLocations() {
        return locations;
    }

    /**
     * Retrieves the list of CarerUser objects from the server that are carers of the DependentUser
     * @return The list of confirmedCarers associated with the dependent account
     */
    public ArrayList<CarerUser> getCarers() throws Exception {

        //Initialise ArrayList if not done
        if (confirmedCarers == null) {
            confirmedCarers = new ArrayList<CarerUser>();
        }

        //If there are ids of carers that have not been retrieved, retrieve the associated CarerUser object
        if (!carers.isEmpty()) {

            for (String id : carers) {

                boolean contains = false;

                //Ensure that not already downloaded/in the list
                for (CarerUser carer : confirmedCarers) {
                    if (id.equals(carer.getId())) {
                        contains = true;
                        break;
                    }
                }

                //Retrieve the carer and add it to the list
                if (!contains) {
                    confirmedCarers.add(AccountController.getInstance().getCarer(id));
                }
            }

            //Clear list of ids as all CarerUser objects have been retrieved
            carers.clear();
        }

        return confirmedCarers;
    }

    /**
     * @return The list of confirmedCarers that have requested to aid this dependent
     */
    public ArrayList<CarerUser> getPendingCarers() throws Exception {

        if (pCarers == null) {
            pCarers = new ArrayList<CarerUser>();
        }

        //If there are ids of carers that have not been retrieved, retrieve the associated CarerUser object
        if (!pendingCarers.isEmpty()) {

            for (String id : pendingCarers) {

                boolean contains = false;

                //Ensure that not already downloaded/in the list
                for (CarerUser carer : pCarers) {
                    if (id.equals(carer.getId())) {
                        contains = true;
                        break;
                    }
                }

                //Retrieve the carer and add it to the list
                if (!contains) {
                    pCarers.add(AccountController.getInstance().getCarer(id));
                }
            }

            //Clear list of ids as all CarerUser objects have been retrieved
            pendingCarers.clear();
        }

        return pCarers;
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
        //AccountController.getInstance().acceptCarer(this, carer, accept);

        //Shift the carer from pending to accepted locally
        pCarers.remove(carer);

        if (confirmedCarers == null) {
            confirmedCarers = new ArrayList<CarerUser>();
        }
        confirmedCarers.add(carer);

    }

    public boolean hasPendingCarers() {
        return !pendingCarers.isEmpty();
    }
}
