package com.comp30023.spain_itproject.domain;

import java.io.Serializable;
import java.util.ArrayList;

public class CarerUser extends User implements Serializable {

    private ArrayList<DependentUser> dependents;

    public CarerUser(String name, String phoneNumber, String pin) {
        super(name, phoneNumber, pin);

        dependents = new ArrayList<>();
    }

    public void addDependent(DependentUser dependent) {
        dependents.add(dependent);
    }

    public ArrayList<DependentUser> getDependents() {
        return dependents;
    }

}
