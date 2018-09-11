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

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountController {

    private static AccountService service;
    private static DetailsValidator validator;

    private static User user;

    public static boolean registerAccount(String name, String phoneNumber, String pin, String confirmPin, Boolean isDependent) throws InvalidDetailsException {

        checkService();

        if (validator == null) {
            validator = DetailsValidator.getInstance();
        }

        validator.checkDetails(name, phoneNumber, pin, confirmPin, isDependent);

        return true;
    }

    public static User login(String phoneNumber, String pin, boolean isDependent) throws IOException {

        checkService();

        Call<User> call = service.loginUser(phoneNumber, pin);

        User user = call.execute().body();


        DependentUser dependentUser = new DependentUser(null, null, null);
        dependentUser.addLocation(new Location(null,"Home"));
        dependentUser.addLocation(new Location(null,"Church"));
        dependentUser.addLocation(new Location(null,"Shop"));
        dependentUser.addLocation(new Location(null,"Etc"));
        dependentUser.addLocation(new Location(null,"Shop2"));

        return dependentUser;
    }

    public static void checkService() {
        if (service == null) {
            service = RetrofitClientInstance.getRetrofitInstance().create(AccountService.class);
        }
    }

}
