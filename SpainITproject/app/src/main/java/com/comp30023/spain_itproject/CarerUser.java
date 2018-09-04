package com.comp30023.spain_itproject;

import java.util.ArrayList;

public class CarerUser extends User {

    public ArrayList<DependentUser> dependents;

    public CarerUser(String name, String phoneNumber, String pin) {
        super(name, phoneNumber, pin);
    }

}
