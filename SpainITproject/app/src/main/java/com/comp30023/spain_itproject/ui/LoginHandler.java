package com.comp30023.spain_itproject.ui;

import android.content.Context;
import android.content.Intent;

import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.domain.User;
import com.comp30023.spain_itproject.uicontroller.AccountController;

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
     * Logs in a new user with details as specified by parameters
     * @param context The activity that called the method
     * @param phoneNumber The phone number of the user to be logged in
     * @param pin The pin of the user to be logged in
     */
    public void login(Context context, String phoneNumber, String pin) throws Exception {

        User user = AccountController.getInstance().login(phoneNumber, pin);

        Boolean isDependent = user.isDependent();

        LoginSharedPreference.setLogIn(context, phoneNumber, pin, isDependent, user.getId());
        displayHomeScreen(context, isDependent);
    }

    public void register(Context context, String name, String phoneNumber, String pin, String confirmPin, boolean isDependent) throws Exception {

        User user = AccountController.getInstance().registerAccount(name, phoneNumber, pin, confirmPin, isDependent);

        LoginSharedPreference.setLogIn(context, phoneNumber, pin, isDependent, user.getId());
        displayHomeScreen(context, isDependent);

    }

    /**
     * Log in the existing user
     * @param context
     */
    public void login(Context context) {
        displayHomeScreen(context, LoginSharedPreference.getIsDependent(context));
    }

    /**
     * Helper function to display the correct home screen for corresponding user
     * after logging in
     * @param context
     * @param isDependent
     */
    private void displayHomeScreen(Context context, boolean isDependent) {

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
