package com.comp30023.spain_itproject.uicontroller;

import android.util.Pair;

import com.comp30023.spain_itproject.domain.CarerUser;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.network.AccountService;
import com.comp30023.spain_itproject.network.BadRequestException;
import com.comp30023.spain_itproject.network.ErrorResponse;
import com.comp30023.spain_itproject.network.ErrorUtils;
import com.comp30023.spain_itproject.network.NoConnectionException;
import com.comp30023.spain_itproject.network.RetrofitClientInstance;
import com.comp30023.spain_itproject.network.UserModel;
import com.comp30023.spain_itproject.domain.Location;
import com.comp30023.spain_itproject.domain.User;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


/**
 * Account controller acts as a bridge between the client and the server and makes use of the
 * Retrofit REST API for exchanging messages between client and server
 */
public class AccountController {

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
     * @throws BadRequestException If the request could not be completed because of invalid input
     * @throws NoConnectionException If the request could not be completed due to a connection issue
     */
    public User registerAccount(String name, String phoneNumber, String pin, Boolean isDependent, String token)
            throws BadRequestException, NoConnectionException {

        checkService();

        String userType = isDependent ? AccountService.USERTYPE_DEPENDENT : AccountService.USERTYPE_CARER;

        //Create the call to the server
        Call<UserModel> call = service.registerUser(name, phoneNumber, pin, userType, token);

        UserModel userModel = executeCallReturnResponse(call);
        User user;

        //Create the type of user based on input (corresponds to user returned from server
        if (isDependent) {
            user = new DependentUser(userModel.getName(), phoneNumber, pin, userModel.getId());
        } else {
            user = new CarerUser(userModel.getName(), phoneNumber, pin, userModel.getId());
        }

        return user;
    }

    /**
     * Requests a user's account from the server
     * @param phoneNumber The user's login ID
     * @param pin The user's login password
     * @return A Pair, First value is user's ID, Second value is whether the account is a dependent
     * @throws BadRequestException If the request could not be completed because of invalid input
     * @throws NoConnectionException If the request could not be completed due to a connection issue
     */
    public Pair<String, Boolean> login(String phoneNumber, String pin, String token)
            throws BadRequestException, NoConnectionException {
        checkService();

        //Create the call to the server
        Call<UserModel> call = service.loginUser(phoneNumber, pin, token);

        UserModel response = executeCallReturnResponse(call);

        String userType = response.getUserType();
        boolean isDependent = userType.equals(AccountService.USERTYPE_DEPENDENT);

        String userId = response.getId();

        Pair<String, Boolean> pair = new Pair<String, Boolean>(userId, isDependent);

        return pair;
    }

    //CONFIRM
    /**
     * Add a location to a dependent's account
     * @param dependent The dependent receiving the location
     * @param location The location to be added
     * @throws BadRequestException If the request could not be completed because of invalid input
     * @throws NoConnectionException If the request could not be completed due to a connection issue
     */
    public void addLocationToDependent(DependentUser dependent, Location location)
            throws BadRequestException, NoConnectionException {
        checkService();

        //Create the call to the server
        Call<ResponseBody> call = service.addLocationToDependent(dependent.getId(), location);

        executeCallNoResponse(call);
    }

    /**
     * Get a CarerUser from the server
     * @param id The ID of the user being requested
     * @return The requested user
     * @throws BadRequestException If the request could not be completed because of invalid input
     * @throws NoConnectionException If the request could not be completed due to a connection issue
     */
    public CarerUser getCarer(String id)
            throws BadRequestException, NoConnectionException {
        checkService();

        // Contact the server to request the list of dependents for a carer
        Call<CarerUser> call = service.getCarer(id);

        return executeCallReturnResponse(call);
    }

    /**
     * Delete a carer from the server
     * @param id
     * @return
     * @throws BadRequestException
     * @throws NoConnectionException
     */
    public CarerUser deleteCarer(String id)
        throws BadRequestException, NoConnectionException {
        checkService();

        // Contact the server to delete the carer
        Call<CarerUser> call = service.deleteCarer(id);

        return executeCallReturnResponse(call);
    }

    /**
     * Get a CarerUser from the server
     * @param id The ID of the user being requested
     * @return The requested user
     * @throws BadRequestException If the request could not be completed because of invalid input
     * @throws NoConnectionException If the request could not be completed due to a connection issue
     */
    public DependentUser getDependent(String id)
            throws BadRequestException, NoConnectionException {
        checkService();

        // Contact the server to request the list of dependents for a carer
        Call<DependentUser> call = service.getDependent(id);

        return executeCallReturnResponse(call);
    }

    public DependentUser deleteDependent(String id)
        throws BadRequestException, NoConnectionException {
        checkService();

        // Contact the server to delete the dependent
        Call<DependentUser> call = service.deleteDependent(id);

        return executeCallReturnResponse(call);
    }

    /**
     * Get a dependent name from the server by passing in a phone number
     * @param phoneNumber
     * @return
     * @throws BadRequestException If the request could not be completed because of invalid input
     * @throws NoConnectionException If the request could not be completed due to a connection issue
     */
    public String getDependentNameByPhoneNumber(String phoneNumber)
            throws BadRequestException, NoConnectionException {
        checkService();

        // Contact the server to request the name of the dependent who has the corresponding
        // phone number
        Call<UserModel> call = service.getDependentNameFromPhoneNumber(phoneNumber);

        UserModel userModel = executeCallReturnResponse(call);

        return userModel.getName();
    }

    /**
     * Sends the response of a friend request to the server
     * @param dependent The dependent who received the request
     * @param carer The carer that sent the request
     * @param accept The boolean response to the request
     * @throws BadRequestException If the request could not be completed because of invalid input
     * @throws NoConnectionException If the request could not be completed due to a connection issue
     */
    public void respondToCarerRequest(DependentUser dependent, CarerUser carer, boolean accept)
            throws BadRequestException, NoConnectionException {

        checkService();

        String stringAccept = accept ? AccountService.CARER_REQUEST_ACCEPT : AccountService.CARER_REQUEST_REJECT;

        Call<ResponseBody> call = service.acceptRequest(dependent.getId(), carer.getId(), stringAccept);

        executeCallNoResponse(call);
    }

    /**
     * Add a dependent to the carer by their phone number
     * @param carerId The carer who is adding the dependent
     * @param dependentPhoneNumber The dependent who is being added's phone number
     * @throws BadRequestException If the request could not be completed because of invalid input
     * @throws NoConnectionException If the request could not be completed due to a connection issue
     */
    public void requestDependent(String carerId, String dependentPhoneNumber)
            throws BadRequestException, NoConnectionException {

        checkService();

        //Create the call to the server
        Call<ResponseBody> call = service.addDependent(carerId, dependentPhoneNumber);

        executeCallNoResponse(call);
    }

    /**
     * Retrieve from the server the carers corresponding to a dependent's account
     * @param dependentUser
     * @return
     * @throws BadRequestException If the request could not be completed because of invalid input
     * @throws NoConnectionException If the request could not be completed due to a connection issue
     */
    public List<CarerUser> getCarersOfDependent(DependentUser dependentUser)
            throws BadRequestException, NoConnectionException {

        checkService();

        Call<List<CarerUser>> call = service.getCarersOfDependent(dependentUser.getId());

        return executeCallReturnResponse(call);
    }

    /**
     * Retrieve from the server the dependents corresponding to a carer's account
     * @param carerUser
     * @return
     * @throws BadRequestException If the request could not be completed because of invalid input
     * @throws NoConnectionException If the request could not be completed due to a connection issue
     */
    public List<DependentUser> getDependentsOfCarer(CarerUser carerUser)
            throws BadRequestException, NoConnectionException {

        checkService();

        Call<List<DependentUser>> call = service.getDependentsOfCarer(carerUser.getId());

        return executeCallReturnResponse(call);
    }

    /**
     * Retrieve from the server the locations corresponding to a dependent's account
     * @param user
     * @return
     * @throws BadRequestException If the request could not be completed because of invalid input
     * @throws NoConnectionException If the request could not be completed due to a connection issue
     */
    public List<Location> getLocationsOfDependent(DependentUser user)
            throws BadRequestException, NoConnectionException {

        checkService();

        Call<List<Location>> call = service.getLocationsOfDependent(user.getId());

        return executeCallReturnResponse(call);
    }

    /**
     * Retrieves from the server the pending carers corresponding to the dependent's account
     * @param user
     * @return
     * @throws BadRequestException If the request could not be completed because of invalid input
     * @throws NoConnectionException If the request could not be completed due to a connection issue
     */
    public List<CarerUser> getPendingCarersOfDependent(DependentUser user)
            throws BadRequestException, NoConnectionException {

        checkService();

        Call<List<CarerUser>> call = service.getPendingCarersOfDependent(user.getId());

        return executeCallReturnResponse(call);
    }

    /**
     * Executes the call and returns the response or throws an error if one occurred during execution
     * @param call The call to be executed
     * @param <T> The type of call that is being made
     * @return The response from the call if successful
     * @throws BadRequestException If the request could not be completed because of invalid input
     * @throws NoConnectionException If the request could not be completed due to a connection issue
     */
    private <T> T executeCallReturnResponse(Call<T> call)
            throws BadRequestException, NoConnectionException {

        try {
            Response<T> response = call.execute();

            if (response.isSuccessful()) {
                return response.body();
            } else {

                ErrorResponse error = ErrorUtils.parseError(response);
                throw new BadRequestException(error);
            }

        } catch (IOException e) {
            throw new NoConnectionException();
        }
    }

    /**
     * Executes a call that does not expect a response or throws an error if one occurred during execution
     * @param call The call to be executed
     * @throws BadRequestException If the request could not be completed because of invalid input
     * @throws NoConnectionException If the request could not be completed due to a connection issue
     */
    private void executeCallNoResponse(Call<ResponseBody> call)
            throws BadRequestException, NoConnectionException {
        try {

            Response<ResponseBody> response = call.execute();

            if (!response.isSuccessful()) {
                ErrorResponse error = ErrorUtils.parseError(response);
                throw new BadRequestException(error);
            }

        } catch (IOException e) {
            throw new NoConnectionException();
        }
    }

    /**
     * Update the users token on the server
     * @param token The new token
     */
    public void updateToken(String userId, boolean isDependent, String token) {

        checkService();

        Call<ResponseBody> call;

        if (isDependent) {
            call = service.updateDependentToken(userId, token);
        } else {
            call = service.updateCarerToken(userId, token);
        }

        try {
            Response<ResponseBody> response = call.execute();

        } catch (IOException e) {
            // TODO If this update fails, should the app close or log out?
            e.printStackTrace();
        }
    }

    public void sendHelpRequest(DependentUser requester, String message)
            throws BadRequestException, NoConnectionException {

        checkService();

        Call<ResponseBody> call = service.requestHelp(requester.getId(), message);

        executeCallNoResponse(call);
    }
}