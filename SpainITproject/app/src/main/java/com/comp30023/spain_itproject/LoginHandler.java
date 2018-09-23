package com.comp30023.spain_itproject;

import android.content.Context;
import android.content.Intent;

import com.comp30023.spain_itproject.domain.User;
import com.comp30023.spain_itproject.ui.CarerHomeActivity;
import com.comp30023.spain_itproject.ui.DependentHomeActivity;
import com.comp30023.spain_itproject.ui.LoginSharedPreference;
import com.comp30023.spain_itproject.ui.StartActivity;
import com.comp30023.spain_itproject.uicontroller.AccountController;

import java.io.IOException;

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
     * @param isDependent Whether the user to be logged in is a Dependent
     */
    public void login(Context context, String name, String phoneNumber, String pin,
                                boolean isDependent, String id) {
        LoginSharedPreference.setLogIn(context, name, phoneNumber, pin, isDependent, id);

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
