package com.comp30023.spain_itproject.uicontroller;

import com.comp30023.spain_itproject.domain.CarerUser;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.network.AccountService;
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

    private static AccountService service;
    private static DetailsValidator validator;

    private static User user = null;

    public User getUser() {
        return user;
    }

    private static void checkService() {
        if (service == null) {
            service = RetrofitClientInstance.getRetrofitInstance().create(AccountService.class);
        }
    }

    private static void checkValidator() {
        if (validator == null) {
            validator = DetailsValidator.getInstance();
        }
    }

    public static User registerAccount(String name, String phoneNumber, String pin,
                                       String confirmPin, Boolean isDependent)
            throws Exception {

        checkService();
        checkValidator();

        validator.checkDetails(name, phoneNumber, pin, confirmPin, isDependent);

        String userType = isDependent ? AccountService.DEPENDENT_TYPE : AccountService.CARER_TYPE;

        Call<UserModel> call = service.registerUser(name, phoneNumber, pin, userType);
        Response<UserModel> response = call.execute();

        UserModel userModel = null;
        User newUser = null;

        if (response.isSuccessful()) {

            switch (response.code()) {
                case HttpResponses.CREATED:

                    userModel = response.body();

                    if (isDependent) {
                        newUser = new DependentUser(name, phoneNumber, pin, userModel.userId);
                    } else {
                        newUser = new CarerUser(name, phoneNumber, pin, userModel.userId);
                    }

                    break;

                default:
                    throw new Exception(response.errorBody().string());
            }
        } else {
            throw new Exception(response.errorBody().string());
        }

        user = newUser;
        return newUser;
    }

    public static User login(String phoneNumber, String pin, boolean isDependent) throws Exception {

        /*checkService();

        Call<User> call = service.loginUser(phoneNumber, pin);

        Response<User> response = call.execute();

        User user = null;
        if (response.isSuccessful()) {

            switch (response.code()) {
                case 201:
                    user = response.body();
                    break;
                case 404:
                    throw new Exception(response.errorBody().string());
                default:
                    break;
            }

        } else {
            throw new NetworkConnectionFailureException();
        }

        return user;*/

        User loginUser;
        if (isDependent) {
            loginUser = new DependentUser(null, phoneNumber, pin, null);
        } else {
            loginUser = new CarerUser(null, phoneNumber, pin, null);
        }

        if (loginUser.equals(user)) {
            return user;
        } else {
            throw new Exception("User not created");
        }
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
     * @param carerPhoneNumber
     * @return
     * @throws IOException
     */
    public static ArrayList<DependentUser> getDependentsOfCarer(String carerPhoneNumber) throws IOException {
        checkService();

        Call<ArrayList<DependentUser>> call = service.getDependentsOfCarer(carerPhoneNumber);

        return call.execute().body();
    }

    public static DependentUser getDependent(String phoneNumber) throws IOException {
        checkService();

        Call<DependentUser> call = service.getDependent(phoneNumber);

        return call.execute().body();
    }
}