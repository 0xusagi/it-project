package com.comp30023.spain_itproject.firebase.realtime_database;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Extension of LiveData that stores the latest DataSnapshot from a DatabaseReference
 */
public class FirebaseValueListenerLiveData extends LiveData<DataSnapshot> implements ValueEventListener {

    private DatabaseReference dbReference;

    /**
     * @param dbReference The DatabaseReference being listened to
     */
    public FirebaseValueListenerLiveData(DatabaseReference dbReference) {
        this.dbReference = dbReference;
    }

    /**
     * Attach the listener when the LiveData becomes active
     */
    @Override
    protected void onActive() {
        super.onActive();
        dbReference.addValueEventListener(this);
    }

    /**
     * Detatch the listener when the LiveData becomes inactive
     */
    @Override
    protected void onInactive() {
        super.onInactive();
        dbReference.removeEventListener(this);
    }

    /**
     * Change the value of this LiveData when the data at the DatabaseReference changes
     * @param dataSnapshot
     */
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        setValue(dataSnapshot);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    /**
     * @return The DatabaseReference that is being listened to
     */
    public DatabaseReference getDbReference() {
        return dbReference;
    }
}
