package com.comp30023.spain_itproject.domain;

import com.comp30023.spain_itproject.uicontroller.AccountController;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Extends user by storing lists of objects related to being a Carer user
 */
public class CarerUser extends User implements Serializable {

    @SerializedName("dependents")
    private ArrayList<DependentUser> dependents;

    @SerializedName("pendingDependents")
    private ArrayList<DependentUser> pendingDependents;

    public CarerUser(String name, String phoneNumber, String pin, String id) {
        super(name, phoneNumber, pin, id);
    }

    /**
     * @return The list of stored dependents
     */
    public ArrayList<DependentUser> getDependents() {
        return dependents;
    }

    /**
     * Adds a DependentUser by their stored phone number
     * Adds the request first to the database and then locally
     * @param dependentPhoneNumber The phone number of the dependent to be added
     * @throws Exception Thrown if there is an error while communicating with the database
     */
    public void addDependent(String dependentPhoneNumber) throws Exception {

        //Check if this carer has already sent a request to the dependent
        for (DependentUser dependent : pendingDependents) {
            if (dependent.getPhoneNumber().equals(dependentPhoneNumber))
                throw new AlreadyAddedException();
        }

        //Adds the request to the carers account externally
        //Will throw an Exception if there is a method while communicating with the database
        DependentUser dependent = AccountController.getInstance().requestDependent(this, dependentPhoneNumber);

        pendingDependents.add(dependent);
    }
}
