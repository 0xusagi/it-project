package com.comp30023.spain_itproject.firebase.realtime_database;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Extension of LiveData that contains the latest child of a DatabaseReference
 */
public class FirebaseChildListenerLiveData extends LiveData<DataSnapshot> implements ChildEventListener {

    private DatabaseReference dbReference;

    /**
     * @param dbReference The DatabaseReference to listen to
     */
    public FirebaseChildListenerLiveData(DatabaseReference dbReference) {
        this.dbReference = dbReference;
    }

    /**
     * Attach the listener when the LiveData becomes active
     */
    @Override
    protected void onActive() {
        super.onActive();
        dbReference.addChildEventListener(this);
        System.out.println("Attached child listener to: " + dbReference);
    }

    /**
     * Detatch the listener when the LiveData becomes inactive
     */
    @Override
    protected void onInactive() {
        super.onInactive();
        dbReference.removeEventListener(this);
        System.out.println("Detached child listener to: " + dbReference);
    }

    /**
     * When a child is added, update the value of the LiveData
     * @param dataSnapshot The new child
     * @param s Reference to the previous child
     */
    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        if (dataSnapshot.exists()) {
            setValue(dataSnapshot);
        }
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
