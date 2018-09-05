package com.comp30023.spain_itproject.uicontroller;

import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.domain.DetailsValidator;
import com.comp30023.spain_itproject.domain.InvalidDetailsException;
import com.comp30023.spain_itproject.domain.Location;
import com.comp30023.spain_itproject.domain.User;

public class AccountController {

    private static DetailsValidator validator;

    public static boolean registerAccount(String name, String phoneNumber, String pin, String confirmPin, Boolean isDependent) throws InvalidDetailsException {

        if (validator == null) {
            validator = DetailsValidator.getInstance();
        }

        validator.checkDetails(name, phoneNumber, pin, confirmPin, isDependent);

        return true;
    }

    public static User login(String phoneNumber, String pin, boolean isDependent) {

        DependentUser user = new DependentUser(null, null, null);
        user.addLocation(new Location(null,"Home"));
        user.addLocation(new Location(null,"Church"));
        user.addLocation(new Location(null,"Shop"));
        user.addLocation(new Location(null,"Etc"));
        user.addLocation(new Location(null,"Shop2"));

        return user;
    }

}
