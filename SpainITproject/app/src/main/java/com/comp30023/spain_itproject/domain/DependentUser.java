package com.comp30023.spain_itproject.domain;

import com.comp30023.spain_itproject.uicontroller.AccountController;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Extends user by storing lists of objects related to being a Dependent user
 */
public class DependentUser extends User {

    @SerializedName("locations")
    private List<String> locationIds;
    private List<Location> locationObjects;

    private Location currentLocation;

    //Server responds with list of ids (Strings) of confirmedCarerIds
    @SerializedName("carers")
    private List<String> confirmedCarerIds;
    private List<CarerUser> confirmedCarerObjects;

    //Server responds with list of ids (Strings of pending confirmedCarerIds
    @SerializedName("pendingCarers")
    private List<String> pendingCarerIds;
    private List<CarerUser> pendingCarerObjects;

    public DependentUser(String name, String phoneNumber, String pin, String id) {
        super(name, phoneNumber, pin, id);

        locationObjects = new ArrayList<Location>();
        confirmedCarerObjects = new ArrayList<CarerUser>();
    }

    /**
     * @return The list of locations associated with the dependent account
     */
    public List<Location> getLocations() throws Exception {

        //If locations have yet to be retrieved from server, retrieve them
        locationObjects = AccountController.getInstance().getLocationsOfDependent(this);
        locationIds.clear();

        if (locationObjects == null) {
            locationObjects = new ArrayList<Location>();
        }

        return locationObjects;
    }

    /**
     * Retrieves the list of CarerUser objects from the server that are confirmedCarerIds of the DependentUser
     * @return The list of confirmedCarerObjects associated with the dependent account
     */
    public List<CarerUser> getCarers() throws Exception {

        //If confirmedCarerIds have yet to be retrieved from the server, retrieve them
        if (!confirmedCarerIds.isEmpty()) {

            confirmedCarerObjects = AccountController.getInstance().getCarersOfDependent(this);
            confirmedCarerIds.clear();
        }

        if (confirmedCarerObjects == null) {
            confirmedCarerObjects = new ArrayList<CarerUser>();
        }

        return confirmedCarerObjects;
    }

    /**
     * @return The list of confirmedCarerObjects that have requested to aid this dependent
     */
    public List<CarerUser> getPendingCarers() throws Exception {

        if (pendingCarerObjects == null) {
            pendingCarerObjects = new ArrayList<>();
        }

        //If there are pending confirmedCarerIds that have yet to be retrieved from the server, retrieve them
        if (pendingCarerIds != null && !pendingCarerIds.isEmpty()) {

            for (String id : pendingCarerIds) {

                boolean contains = false;

                //Ensure that not already downloaded/in the list
                for (CarerUser carer : pendingCarerObjects) {
                    if (id.equals(carer.getId())) {
                        contains = true;
                        break;
                    }
                }

                //Retrieve the carer and add it to the list
                if (!contains) {
                    pendingCarerObjects.add(AccountController.getInstance().getCarer(id));
                }
            }

            //UNCOMMENT WHEN ENDPOINT COMPLETE
            //pendingCarerObjects = AccountController.getInstance().getPendingCarersOfDependent(this);

            //Clear list of ids as all CarerUser objects have been retrieved
            pendingCarerIds.clear();
        }

        if (pendingCarerObjects == null) {
            pendingCarerObjects = new ArrayList<>();
        }

        return pendingCarerObjects;
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

            if (confirmedCarerObjects != null) {
                confirmedCarerObjects.add(carer);
            }

        }
    }

    /**
     * @return Whether the dependent has requests by confirmedCarerIds
     */
    public boolean hasPendingCarers() {
        return !pendingCarerIds.isEmpty() || ((pendingCarerObjects != null) && !pendingCarerObjects.isEmpty());
    }

    public void addLocation(String id, String name, double lat, double lng) throws Exception {

        List<Location> locations = getLocations();

        for (Location existingLocation : locations) {
            if (existingLocation.getGoogleId().equals(id)) {
                return;
            }
        }

        Location location = AccountController.getInstance().addLocationToDependent(getId(), id, lat, lng, name);

        locations.add(location);
    }

    public void deleteLocation(Location location) throws Exception{
        AccountController.getInstance().deleteLocation(location.get_id());

        locationObjects.remove(location);
    }
}
