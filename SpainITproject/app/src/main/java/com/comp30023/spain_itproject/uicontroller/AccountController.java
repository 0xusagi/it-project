package com.comp30023.spain_itproject.uicontroller;

import android.accounts.Account;
import android.widget.Toast;

import com.comp30023.spain_itproject.domain.CarerUser;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.network.AccountService;
import com.comp30023.spain_itproject.network.RetrofitClientInstance;
import com.comp30023.spain_itproject.validation.DetailsValidator;
import com.comp30023.spain_itproject.validation.InvalidDetailsException;
import com.comp30023.spain_itproject.domain.Location;
import com.comp30023.spain_itproject.domain.User;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Account controller acts as a bridge between the client and the server and makes use of the
 * Retrofit REST API for exchanging messages between client and server
 */
public class AccountController {

    private static AccountService service;
    private static DetailsValidator validator;

    private static User user;

    public static User registerAccount(String name, String phoneNumber, String pin,
                                       String confirmPin, Boolean isDependent)
            throws InvalidDetailsException, IOException {

        checkService();

        if (validator == null) {
            validator = DetailsValidator.getInstance();
        }

        validator.checkDetails(name, phoneNumber, pin, confirmPin, isDependent);

        Call<User> call = service.registerUser(name, phoneNumber, pin, isDependent);

        User newUser = call.execute().body();

        return newUser;
    }

    public static User login(String phoneNumber, String pin, boolean isDependent) throws IOException {

        checkService();

        Call<User> call = service.loginUser(phoneNumber, pin);

        User user = call.execute().body();

        return user;
    }

    public ArrayList<Location> getLocations(DependentUser dependent) throws IOException {
        checkService();

        Call<ArrayList<Location>> call = service.getLocations(dependent);

        return call.execute().body();
    }

    public void addDependent(CarerUser carer, String dependentPhoneNumber) {
        checkService();

        //Call<DependentUser> call = service.addDependent(carer.getId(), dependentPhoneNumber);
    }

    /**
     * Get a dependents list which corresponds to a carer
     * @param carerId
     * @return
     * @throws IOException
     */
    public static ArrayList<DependentUser> getDependentsOfCarer(String carerPhoneNumber) throws IOException {
        checkService();

        Call<ArrayList<DependentUser>> call = service.getDependentsOfCarer(carerPhoneNumber);

        return call.execute().body();
    }

    private static void checkService() {
        if (service == null) {
            service = RetrofitClientInstance.getRetrofitInstance().create(AccountService.class);
        }
    }
}