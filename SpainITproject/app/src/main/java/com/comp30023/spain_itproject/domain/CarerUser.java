package com.comp30023.spain_itproject.domain;

import com.comp30023.spain_itproject.uicontroller.AccountController;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Extends user by storing lists of objects related to being a Carer user
 */
public class CarerUser extends User implements Serializable {

    @SerializedName("dependents")
    private List<String> dependentIds;

    private List<DependentUser> confirmedDependents;

    @SerializedName("pendingDependents")
    private List<String> pendingDependents;

    private List<DependentUser> pDependents;

    public CarerUser(String name, String phoneNumber, String pin, String id) {
        super(name, phoneNumber, pin, id);
    }

    /**
     * @return The list of stored dependents
     */
    public List<DependentUser> getDependents() throws Exception {

        if (confirmedDependents == null) {
            confirmedDependents = new ArrayList<DependentUser>();
        }

        if (!dependentIds.isEmpty()) {
            confirmedDependents = AccountController.getInstance().getDependentsOfCarer(this);
            dependentIds.clear();
        }

        return confirmedDependents;
    }
}
