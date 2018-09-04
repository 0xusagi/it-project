package com.comp30023.spain_itproject.uicontroller;

import com.comp30023.spain_itproject.DependentUser;
import com.comp30023.spain_itproject.Location;
import com.comp30023.spain_itproject.User;

public class AccountController {

    public static boolean registerAccount(String name, String phoneNumber, String pin, Boolean isDependent) {
        return true;
    }

    public static User login(String phoneNumber, String pin, boolean isDependent) {

        DependentUser user = new DependentUser(null, null, null);
        user.addLocation(new Location(null,"Home"));

        return user;
    }

}
