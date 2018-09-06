package com.comp30023.spain_itproject.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Contains the current login status and information as saved in a SharedPreferences
 */
public class LoginSharedPreference {

    public static final String LOGIN = "LOGIN";

    private static final String PREF_IS_LOGGED_IN = "IS_LOGGED_IN";
    private static final String PREF_PHONE_NUMBER = "PHONE_NUMBER";
    private static final String PREF_PIN = "PIN";
    private static final String PREF_IS_DEPENDENT = "IS_DEPENDENT";

    //Status of whether the SharedPreference has been set
    private static boolean set = false;

    /**
     * The SharedPreferences Instance being used
     */
    private static SharedPreferences instance;

    /**
     * Saves the login information in the SharedPreferences instance
     *
     * @param phoneNumber The phone number of the user
     * @param pin The pin of the user
     * @param isDependent Whether the user is a dependent
     */
    public static void setLogIn(Context context, String phoneNumber, String pin, boolean isDependent) {

        checkSharedPreferences(context);

        SharedPreferences.Editor edit = instance.edit();
        edit.putBoolean(PREF_IS_LOGGED_IN, true);
        edit.putString(PREF_PHONE_NUMBER, phoneNumber);
        edit.putString(PREF_PIN, pin);
        edit.putBoolean(PREF_IS_DEPENDENT, isDependent);

        edit.commit();
    }

    /**
     * Clears the login information in the SharedPreferences
     */
    public static void setLogOut(Context context) {
        checkSharedPreferences(context);
        SharedPreferences.Editor edit = instance.edit().clear();
        edit.commit();
    }

    /**
     * Determines whether the user has signed in
     * @return Boolean value to represent whether the user has signed in
     */
    public static boolean checkLogIn(Context context) {
        checkSharedPreferences(context);
        return instance.getBoolean(PREF_IS_LOGGED_IN, false);
    }

    /**
     * @return The phone number of the current user
     */
    public static String getPhoneNumber(Context context) {
        checkSharedPreferences(context);
        return instance.getString(PREF_PHONE_NUMBER, "");
    }

    /**
     * @return The pin of the current user
     */
    public static String getPin(Context context) {
        checkSharedPreferences(context);
        return instance.getString(PREF_PIN, "");
    }

    /**
     * @return Boolean value representing whether the user is a dependent
     */
    public static boolean getIsDependent(Context context) {
        checkSharedPreferences(context);
        return instance.getBoolean(PREF_IS_DEPENDENT, true);
    }

    /**
     * Retrieves the SharedPreferences from the application context
     * @param context
     */
    private static void checkSharedPreferences(Context context) {
        if (instance == null) {
            instance = PreferenceManager.getDefaultSharedPreferences(context);
        }
    }

}
