package com.comp30023.spain_itproject.firebase.realtime_database;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Class to redirect access to FirebaseDatabase instance
 */
public class MyFirebaseDatabase {

    private static FirebaseDatabase instance;

    public static FirebaseDatabase getDatabase() {

        if (instance == null) {
            instance = FirebaseDatabase.getInstance();
            instance.setPersistenceEnabled(true);
        }
        return instance;
    }
}
