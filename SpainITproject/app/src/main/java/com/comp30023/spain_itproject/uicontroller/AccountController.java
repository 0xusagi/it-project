package com.comp30023.spain_itproject.uicontroller;

import android.accounts.Account;
import android.widget.Toast;

import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.network.AccountService;
import com.comp30023.spain_itproject.network.RetrofitClientInstance;
import com.comp30023.spain_itproject.validation.DetailsValidator;
import com.comp30023.spain_itproject.validation.InvalidDetailsException;
import com.comp30023.spain_itproject.domain.Location;
import com.comp30023.spain_itproject.domain.User;

public class AccountController {

    private static AccountService service;
    private static DetailsValidator validator;

    public static boolean registerAccount(String name, String phoneNumber, String pin, String confirmPin, Boolean isDependent) throws InvalidDetailsException {

        checkService();

        if (validator == null) {
            validator = DetailsValidator.getInstance();
        }

        validator.checkDetails(name, phoneNumber, pin, confirmPin, isDependent);

        return true;
    }

    public static User login(String phoneNumber, String pin, boolean isDependent) {

        checkService();

        DependentUser user = new DependentUser(null, null, null);
        user.addLocation(new Location(null,"Home"));
        user.addLocation(new Location(null,"Church"));
        user.addLocation(new Location(null,"Shop"));
        user.addLocation(new Location(null,"Etc"));
        user.addLocation(new Location(null,"Shop2"));

        return user;
    }

    public static void checkService() {
        if (service == null) {
            service = RetrofitClientInstance.getRetrofitInstance().create(AccountService.class);
        }
    }

}
