package com.comp30023.spain_itproject.uicontroller;

import android.util.Pair;

import com.comp30023.spain_itproject.domain.CarerUser;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.network.AccountService;
import com.comp30023.spain_itproject.network.BadRequestException;
import com.comp30023.spain_itproject.network.RetrofitClientInstance;
import com.comp30023.spain_itproject.network.UserModel;
import com.comp30023.spain_itproject.domain.Location;
import com.comp30023.spain_itproject.domain.User;
import com.comp30023.spain_itproject.ui.LoginHandler;
import com.comp30023.spain_itproject.ui.LoginSharedPreference;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


/**
 * Account controller acts as a bridge between the client and the server and makes use of the
 * Retrofit REST API for exchanging messages between client and server
 */
public class AccountController {

    public static final String MESSAGE_SERVER_FAILURE = "Please try again";

    private String userId = null;

    private static AccountController instance;
    public static AccountController getInstance() {
        if (instance == null) {
            instance = new AccountController();
        }

        return instance;
    }

    private static AccountService service;
    private void checkService() {
        if (service == null) {
            service = RetrofitClientInstance.getRetrofitInstance().create(AccountService.class);
        }
    }

    /**
     * Registers an account for the following details
     * @return The registered account
     * @throws Exception Error for either connection failure or bad response. Includes message.
     */
    public User registerAccount(String name, String phoneNumber, String pin, Boolean isDependent)
            throws Exception {
        checkService();

        String firebaseToken = FirebaseInstanceId.getInstance().getId();

        System.out.println("Account Registration: Token: " + firebaseToken);

        String userType = isDependent ? AccountService.DEPENDENT_TYPE : AccountService.CARER_TYPE;

        //Create the call to the server
        Call<UserModel> call = service.registerUser(name, phoneNumber, pin, userType/*, firebaseToken*/);

        try {

            //Execute the call to the server
            Response<UserModel> response = call.execute();

            //If response is successful, return the created user from the response body
            if (response.isSuccessful()) {

                UserModel userModel = response.body();
                User user;

                userId = userModel.getId();

                //Create the type of user based on input (corresponds to user returned from server
                if (isDependent) {
                    user = new DependentUser(userModel.getName(), phoneNumber, pin, userId);
                } else {
                    user = new CarerUser(userModel.getName(), phoneNumber, pin, userId);
                }

                return user;

            //If the response is unsuccessful, throw the Exception with the message
            } else {
                throw new BadRequestException(response.message());
            }

        } catch (IOException e) {
            throw new Exception(MESSAGE_SERVER_FAILURE);
        }
    }

    /**
     * Requests a user's account from the server
     * @param phoneNumber The user's login ID
     * @param pin The user's login password
     * @return A Pair, First value is user's ID, Second value is whether the account is a dependent
     * @throws Exception Error for either connection failure or bad response. Includes message.
     */
    public Pair<String, Boolean> login(String phoneNumber, String pin) throws Exception {
        checkService();

        String firebaseToken = FirebaseInstanceId.getInstance().getId();

        //Create the call to the server
        Call<UserModel> call = service.loginUser(phoneNumber, pin/*, firebaseToken*/);

        System.out.println("Login: Token: " + firebaseToken);

        try {
            //Execute the call to the server
            Response<UserModel> response = call.execute();

            //If the response is successful, return the id and whether the account is dependent
            if (response.isSuccessful()) {

                String userType = response.body().getUserType();
                boolean isDependent = userType.equals(AccountService.DEPENDENT_TYPE);

                userId = response.body().getId();

                Pair<String, Boolean> pair = new Pair<String, Boolean>(userId, isDependent);

                return pair;

            //If the response is unsuccessful, throw an exception with the error message
            } else {
                throw new BadRequestException(response.message());
            }

        } catch (IOException e) {
            throw new Exception(MESSAGE_SERVER_FAILURE);
        }
    }

    //CONFIRM
    /**
     * Add a dependent to the carer by their phone number
     * @param carer The carer who is adding the dependent
     * @param dependentPhoneNumber The dependent who is being added's phone number
     * @throws Exception Thrown with a message if there is no contact made with the server or if unsuccessful response
     */
    public DependentUser addDependent(CarerUser carer, String dependentPhoneNumber) throws Exception {
        checkService();

        //Create the call to the server
        Call<DependentUser> call = service.addDependent(carer.getId(), dependentPhoneNumber);

        try{
            //Execut the call to the server
            Response<DependentUser> response = call.execute();

            //If response is successful, return the dependent
            if (response.isSuccessful()) {

                DependentUser dependent = response.body();
                return dependent;

            //If the response is unsuccessful, throw an Exception with a message for the reason
            } else {
                throw new Exception(response.message());
            }

        } catch (IOException e) {
            throw new Exception(MESSAGE_SERVER_FAILURE);
        }
    }

    //CONFIRM
    /**
     * Add a location to a dependent's account
     * @param dependent The dependent receiving the location
     * @param location The location to be added
     * @throws Exception Thrown with a message if there is no contact made with the server or if unsuccessful response
     */
    public void addLocationToDependent(DependentUser dependent, Location location) throws Exception {
        checkService();

        //Create the call to the server
        Call<ResponseBody> call = service.addLocationToDependent(dependent.getId(), location);

        try {
            //Execute the call to the server
            Response<ResponseBody> response = call.execute();

            //If unsuccessful, throw an exception with the reason
            if (!response.isSuccessful()) {
                throw new Exception(response.message());
            }
        } catch (IOException e) {
            throw new Exception(MESSAGE_SERVER_FAILURE);
        }
    }

    /**
     * Get a CarerUser from the server
     * @param id The ID of the user being requested
     * @return The requested user
     * @throws Exception Thrown with a message if there is no contact made with the server or if unsuccessful response
     */
    public CarerUser getCarer(String id) throws Exception {
        checkService();

        // Contact the server to request the list of dependents for a carer
        Call<CarerUser> call = service.getCarer(id);

        try {
            Response<CarerUser> response = call.execute();

            // Check the status codes of the response and handle accordingly
            if (response.isSuccessful()) {

                // Could be null or there exists a carer
                CarerUser user = response.body();
                return user;
            }

            // Bad request
            else {
                // TODO maybe change to another exception for bad request
                throw new BadRequestException("ERROR! Bad request: " + response.message());
            }

        } catch (IOException e) {
            throw new Exception(MESSAGE_SERVER_FAILURE);
        }
    }

    /**
     * Get a CarerUser from the server
     * @param id The ID of the user being requested
     * @return The requested user
     * @throws Exception Thrown with a message if there is no contact made with the server or if unsuccessful response
     */
    public DependentUser getDependent(String id) throws Exception {
        checkService();

        // Contact the server to request the list of dependents for a carer
        Call<DependentUser> call = service.getDependent(id);

        Task<InstanceIdResult> firebaseToken = FirebaseInstanceId.getInstance().getInstanceId();
        firebaseToken.addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                System.out.println("Token: " + instanceIdResult.getToken());
            }
        });

        System.out.println("GetDependent: Token: " + firebaseToken);

        try {
            Response<DependentUser> response = call.execute();

            // Check the status codes of the response and handle accordingly
            if (response.isSuccessful()) {

                // Could be null or there exists a carer
                DependentUser user = response.body();

                return user;
            }

            // Bad request
            else {
                // TODO maybe change to another exception for bad request
                throw new BadRequestException("ERROR! Bad request: " + response.message());
            }

        } catch (IOException e) {
            throw new Exception(MESSAGE_SERVER_FAILURE);
        }
    }

    public void updateToken(String token) {

        checkService();

        assert userId != null;
        Call<ResponseBody> call = service.updateToken(userId, token);

        try {
            Response<ResponseBody> response = call.execute();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}