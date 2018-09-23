package com.comp30023.spain_itproject.uicontroller;

import com.comp30023.spain_itproject.domain.CarerUser;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.network.AccountService;
import com.comp30023.spain_itproject.network.BadRequestException;
import com.comp30023.spain_itproject.network.HttpResponses;
import com.comp30023.spain_itproject.network.RetrofitClientInstance;
import com.comp30023.spain_itproject.network.UserModel;
import com.comp30023.spain_itproject.validation.DetailsValidator;
import com.comp30023.spain_itproject.domain.Location;
import com.comp30023.spain_itproject.domain.User;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;


/**
 * Account controller acts as a bridge between the client and the server and makes use of the
 * Retrofit REST API for exchanging messages between client and server
 */
public class AccountController {
    private static AccountController instance;

    private static AccountService service;
    private static DetailsValidator validator;

    public static AccountController getInstance() {
        if (instance == null) {
            instance = new AccountController();
        }

        return instance;
    }

    private void checkService() {
        if (service == null) {
            service = RetrofitClientInstance.getRetrofitInstance().create(AccountService.class);
        }
    }

    private void checkValidator() {
        if (validator == null) {
            validator = DetailsValidator.getInstance();
        }
    }

    public User registerAccount(String name, String phoneNumber, String pin,
                                       String confirmPin, Boolean isDependent)
            throws Exception {

        checkService();
        checkValidator();

        validator.checkDetails(name, phoneNumber, pin, confirmPin, isDependent);

        String userType = isDependent ? AccountService.DEPENDENT_TYPE : AccountService.CARER_TYPE;

        Call<User> call = service.registerUser(name, phoneNumber, pin, userType);
        Response<User> response = call.execute();

        User user;

        if (response.isSuccessful()) {

            switch (response.code()) {
                case HttpResponses.CREATED:

                    user = response.body();
                    break;

                default:
                    // Default: good response but user is not created
                    return null;
            }
        } else {
            // TODO check whether to use erroBOdy()
            throw new BadRequestException(response.errorBody().string());
        }

        return user;
    }

    public User login(String phoneNumber, String pin) throws Exception {

        checkService();

        Call<User> call = service.loginUser(phoneNumber, pin);

        Response<User> response = call.execute();

        User user;
        if (response.isSuccessful()) {

            switch (response.code()) {
                case 200:
                    user = response.body();
                    break;
                default:
                    // Default: good response but user is not created
                    return null;
            }

        } else {
            throw new BadRequestException("ERROR");
        }

        return user;
/*
        User loginUser;
        if (isDependent) {
            loginUser = new DependentUser(null, phoneNumber, pin, null);
        } else {
            loginUser = new CarerUser(null, phoneNumber, pin, null);
        }

        if (loginUser.equals(user)) {
            id = user.getId();
            return user;
        } else {
            throw new Exception("User not created");
        } */
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
     * @param id
     * @return
     * @throws IOException
     */
    public CarerUser getCarer(String id) throws IOException, BadRequestException {
        checkService();

        // Contact the server to request the list of dependents for a carer
        Call<CarerUser> call = service.getCarer(id);
        Response<CarerUser> response = call.execute();

        // TODO this returns the whole user so need to just get the dependents
        // Check the status codes of the response and handle accordingly
        if (response.isSuccessful()) {
            // Could be null or there exists a carer
            return response.body();
        }
        // Bad request
        else {
            // TODO maybe change to another exception for bad request
            throw new BadRequestException("ERROR! Bad request");
        }
    }

    public DependentUser getDependent(String id) throws IOException, BadRequestException {
        checkService();

        Call<DependentUser> call = service.getDependent(id);
        Response<DependentUser> response = call.execute();

        if (response.isSuccessful()) {
            // Could be null or there exists a dependent
            return response.body();
        }
        // Bad request
        else {
            throw new BadRequestException("ERROR! Bad request");
        }
    }
}