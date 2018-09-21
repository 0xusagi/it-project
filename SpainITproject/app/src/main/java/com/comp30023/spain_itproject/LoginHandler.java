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

    /**
     * String to store the ...User in the intent when passed to the ...HomeActivity
     */
    public static final String PASSED_USER = "PASSED USER";

    /**
     * Determines whether a user has been logged in
     * @return Boolean value of whether a user's credentials are stored
     */
    public static boolean isLoggedIn(Context context) {
        return LoginSharedPreference.checkLogIn(context);
    }

    /**
     * Logs in a new user with details as specified by parameters
     * @param context The activity that called the method
     * @param phoneNumber The phone number of the user to be logged in
     * @param pin The pin of the user to be logged in
     * @param isDependent Whether the user to be logged in is a Dependent
     */
    public static void newLogin(Context context, String phoneNumber, String pin, boolean isDependent) throws Exception {

        try {
            LoginSharedPreference.setLogIn(context, phoneNumber, pin, isDependent);
            login(context);
        } catch (Exception e) {
            LoginSharedPreference.setLogOut(context);
        }
    }

    /**
     * Log in the existing user
     * @param context
     */
    public static void login(Context context) throws Exception {

        if (!LoginSharedPreference.checkLogIn(context)) {
            return;
        }

        Intent intent;

        String phoneNumber = LoginSharedPreference.getPhoneNumber(context);
        String pin = LoginSharedPreference.getPin(context);
        boolean isDependent = LoginSharedPreference.getIsDependent(context);

        User user;
        user = AccountController.login(phoneNumber, pin, isDependent);

        if (isDependent) {
            intent = new Intent(context, DependentHomeActivity.class);
        } else {
            intent = new Intent(context, CarerHomeActivity.class);
        }

        intent.putExtra(PASSED_USER, user);
        context.startActivity(intent);
    }

    /**
     * Logs out the current user and begins the StartActivity
     * @param context
     */
    public static void logout(Context context) {

        LoginSharedPreference.setLogOut(context);
        Intent intent = new Intent(context, StartActivity.class);
        context.startActivity(intent);

    }

}
