package com.comp30023.spain_itproject.domain;

import com.comp30023.spain_itproject.uicontroller.AccountController;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Extends user by storing lists of objects related to being a Dependent user
 */
public class DependentUser extends User {

    @SerializedName("destinations")
    private List<String> locationIds;

    private List<Location> locations;

    private Location currentLocation;

    //Server responds with list of ids (Strings) of carers
    private List<String> carers;
    private List<CarerUser> confirmedCarers;

    //Server responds with list of ids (Strings of pending carers
    private List<String> pendingCarers;
    private List<CarerUser> pCarers;

    public DependentUser(String name, String phoneNumber, String pin, String id) {
        super(name, phoneNumber, pin, id);

        locations = new ArrayList<Location>();
        confirmedCarers = new ArrayList<CarerUser>();
    }

    /**
     * @return The list of locations associated with the dependent account
     */
    public List<Location> getLocations() throws Exception {

        if (locations == null) {
            locations = new ArrayList<Location>();
        }

        //If locations have yet to be retrieved from server, retrieve them
        if (locationIds != null && !locationIds.isEmpty()) {
            locations = AccountController.getInstance().getLocationsOfDependent(this);
            locationIds.clear();
        }

        return locations;
    }

    /**
     * Retrieves the list of CarerUser objects from the server that are carers of the DependentUser
     * @return The list of confirmedCarers associated with the dependent account
     */
    public List<CarerUser> getCarers() throws Exception {

        if (confirmedCarers == null) {
            confirmedCarers = new ArrayList<CarerUser>();
        }

        //If carers have yet to be retrieved from the server, retrieve them
        if (!carers.isEmpty()) {

            confirmedCarers = AccountController.getInstance().getCarersOfDependent(this);
            carers.clear();
        }

        return confirmedCarers;
    }

    /**
     * @return The list of confirmedCarers that have requested to aid this dependent
     */
    public List<CarerUser> getPendingCarers() throws Exception {

        if (pCarers == null) {
            pCarers = new ArrayList<>();
        }

        //If there are pending carers that have yet to be retrieved from the server, retrieve them
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

            //UNCOMMENT WHEN ENDPOINT COMPLETE
            //pCarers = AccountController.getInstance().getPendingCarersOfDependent(this);

            //Clear list of ids as all CarerUser objects have been retrieved
            pendingCarers.clear();
        }

        return pCarers;
    }

    /**
     * Adds a location to a Dependent's list locally and externally
     * @throws Exception Thrown if there is an error when communicating with the server
     */
    public void addLocation(String googleId, double latitude, double longitude, String displayName) throws Exception {

        //Add location to dependent on the database
        //Throws exception if error occurs
        //If this statement not completed, does not add the location locally
        AccountController.getInstance().addLocationToDependent(this.getId(), googleId, latitude, longitude, displayName);

        //Add location to dependent locally so consistent
        if (locations == null) {
            locations = new ArrayList<Location>();
        }
        Location location = new Location(googleId, latitude, longitude, displayName);
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
        AccountController.getInstance().respondToCarerRequest(this, carer, accept);

        //Shift the carer from pending to accepted locally
        getPendingCarers().remove(carer);
        if (accept) {

            if (confirmedCarers == null) {
                confirmedCarers = new ArrayList<CarerUser>();
            }
            getCarers().add(carer);
        }


    }

    /**
     * @return Whether the dependent has requests by carers
     */
    public boolean hasPendingCarers() {
        return !pendingCarers.isEmpty() || ((pCarers != null) && !pCarers.isEmpty());
    }
}
