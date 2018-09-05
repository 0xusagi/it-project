package com.comp30023.spain_itproject.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LoginSharedPreference {

    public static final String PREF_IS_LOGGED_IN = "IS_LOGGED_IN";
    public static final String PREF_PHONE_NUMBER = "PHONE_NUMBER";
    public static final String PREF_PIN = "PIN";
    public static final String PREF_IS_DEPENDENT = "IS_DEPENDENT";

    private static SharedPreferences instance;

    public static void setLogIn(Context context, String phoneNumber, String pin, boolean isDependent) {
        checkInstance(context);

        instance.edit().putBoolean(PREF_IS_LOGGED_IN, true);
        instance.edit().putString(PREF_PHONE_NUMBER, phoneNumber);
        instance.edit().putString(PREF_PIN, pin);
        instance.edit().putBoolean(PREF_IS_DEPENDENT, isDependent);
    }

    public static void setLogOut(Context context) {
        checkInstance(context);
        instance.edit().clear();
    }

    public static boolean checkLogIn(Context context) {
        checkInstance(context);
        return instance.getBoolean(PREF_IS_LOGGED_IN, false);
    }

    public static String getPhoneNumber(Context context) {
        checkInstance(context);
        return instance.getString(PREF_PHONE_NUMBER, "");
    }

    public static String getPin(Context context) {
        checkInstance(context);
        return instance.getString(PREF_PIN, "");
    }

    public static boolean getIsDependent(Context context) {
        checkInstance(context);
        return instance.getBoolean(PREF_IS_DEPENDENT, true);
    }

    private static void checkInstance(Context context) {
        if (instance == null) {
            instance = PreferenceManager.getDefaultSharedPreferences(context);
        }
    }

}
