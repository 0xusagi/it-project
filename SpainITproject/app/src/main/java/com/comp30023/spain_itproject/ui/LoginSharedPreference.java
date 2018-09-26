package com.comp30023.spain_itproject.ui;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Contains the current login status and information as saved in a SharedPreferences
 */
public class LoginSharedPreference {
    // Instance of Shared preference
    private static SharedPreferences instance;

    private static final String PREF_PATH = "com.comp30023.spain_itproject.LOGIN_PREF";

    private enum Pref {
        IS_LOGGED_IN,
        ID,
        PHONE_NUMBER,
        PIN,
        IS_DEPENDENT
    }

    /**
     * Check if there exists a shared preference otherwise get it
     * @param context
     */
    private static void checkInstance(Context context) {
        if (instance == null) {
            instance = context.getSharedPreferences(PREF_PATH, Context.MODE_PRIVATE);
        }
    }

    /**
     * Saves the login information in the SharedPreferences instance
     * @param context
     * @param phoneNumber
     * @param pin
     * @param isDependent
     * @param id
     */
    public static void setLogIn(Context context, String phoneNumber, String pin,
                                boolean isDependent, String id) {
        checkInstance(context);

        // Store the information of the user
        SharedPreferences.Editor editor = instance.edit();
        editor.putBoolean(Pref.IS_LOGGED_IN.name(), true);
        editor.putString(Pref.PHONE_NUMBER.name(), phoneNumber);
        editor.putString(Pref.PIN.name(), pin);
        editor.putBoolean(Pref.IS_DEPENDENT.name(), isDependent);
        editor.putString(Pref.ID.name(), id);

        editor.commit();
    }

    /**
     * Clears the login information in the SharedPreferences
     * @param context
     */
    public static void setLogOut(Context context) {
        checkInstance(context);

        SharedPreferences.Editor editor = instance.edit();

        editor.clear();
        editor.commit();
    }

    /**
     * Determines whether the user has signed in
     * @param context
     * @return Boolean value to represent whether the user has signed in
     */
    public static boolean checkLogIn(Context context) {
        checkInstance(context);
        return instance.getBoolean(Pref.IS_LOGGED_IN.name(), false);
    }

    /**
     * Get the id of user
     * @param context
     * @return id if exists, null otherwise
     */
    public static String getId(Context context) {
        checkInstance(context);
        return instance.getString(Pref.ID.name(), null);
    }

    /**
     * Get phone number of user
     * @param context
     * @return phone number if exists, null otherwise
     */
    public static String getPhoneNumber(Context context) {
        checkInstance(context);
        return instance.getString(Pref.PHONE_NUMBER.name(), null);
    }

    /**
     * Get the pin of user
     * @param context
     * @return pin if exists, null otherwise
     */
    public static String getPin(Context context) {
        checkInstance(context);
        return instance.getString(Pref.PIN.name(), null);
    }

    /**
     * Get whether user is a dependent or not
     * @param context
     * @return
     */
    public static boolean getIsDependent(Context context) {
        checkInstance(context);
        return instance.getBoolean(Pref.IS_DEPENDENT.name(), true);
    }
}
