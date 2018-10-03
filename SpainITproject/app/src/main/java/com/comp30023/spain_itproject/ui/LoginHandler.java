package com.comp30023.spain_itproject.ui;

import android.content.Context;
import android.content.Intent;
import android.util.Pair;

import com.comp30023.spain_itproject.domain.User;
import com.comp30023.spain_itproject.ui.carerhome.CarerHomeActivity;
import com.comp30023.spain_itproject.ui.dependenthome.DependentHomeActivity;
import com.comp30023.spain_itproject.uicontroller.AccountController;
import com.comp30023.spain_itproject.detailsvalidation.DetailsValidator;

/**
 * Logs in users and starts appropriate activity
 *      For DependentUser, starts DependentHomeActivity
 *      For CarerUser, starts CarerHomeActivity
 * Stores information when logged in
 */
public class LoginHandler {

    private static LoginHandler instance;

    public static LoginHandler getInstance() {
        if (instance == null) {
            instance = new LoginHandler();
        }

        return instance;
    }
    /**
     * Determines whether a user has been logged in
     * @return Boolean value of whether a user's credentials are stored
     */
    public boolean isLoggedIn(Context context) {
        return LoginSharedPreference.checkLogIn(context);
    }

    /**
     * Logs in user into the server with details as specified by parameter and receives their ID and account type
     * @param context The activity that called the method
     * @param phoneNumber The phone number of the user to be logged in
     * @param pin The pin of the user to be logged in
     */
    public void login(Context context, String phoneNumber, String pin) throws Exception {

        Pair<String, Boolean> response = AccountController.getInstance().login(phoneNumber, pin);

        String userId = response.first;
        Boolean isDependent = response.second;

        LoginSharedPreference.setLogIn(context, phoneNumber, pin, isDependent, userId);
        displayHomeScreen(context);
    }

    /**
     * Register a new account, set that account as logged in and then pass to the next activity
     * @param context The activity calling the method
     * @param name
     * @param phoneNumber
     * @param pin
     * @param confirmPin
     * @param isDependent
     * @throws Exception Thrown with a message if there is an error in the registration process
     */
    public void register(Context context, String name, String phoneNumber, String pin, String confirmPin, boolean isDependent) throws Exception {

        DetailsValidator.getInstance().checkDetails(name, phoneNumber, pin, confirmPin, isDependent);

        User user = AccountController.getInstance().registerAccount(name, phoneNumber, pin, isDependent);

        LoginSharedPreference.setLogIn(context, phoneNumber, pin, isDependent, user.getId());
        displayHomeScreen(context);
    }

    /**
     * Log in the existing user
     * @param context The activity calling the method
     */
    public void continueLogin(Context context) {
        displayHomeScreen(context);
    }

    /**
     * Helper function to display the correct home screen for corresponding user
     * after logging in
     * @param context
     */
    private void displayHomeScreen(Context context) {

        boolean isDependent = LoginSharedPreference.getIsDependent(context);

        Intent intent;
        if (isDependent) {
            intent = new Intent(context, DependentHomeActivity.class);
        } else {
            intent = new Intent(context, CarerHomeActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Logs out the current user and begins the StartActivity
     * @param context
     */
    public static void logout(Context context) {

        LoginSharedPreference.setLogOut(context);
        Intent intent = new Intent(context, StartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
