package com.comp30023.spain_itproject.firebase;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FirebaseQueryLiveData extends LiveData<DataSnapshot> implements ChildEventListener {

    private final DatabaseReference query;

    public enum ListenerType {
        CHILD,
        VALUE,
        SINGLE_VALUE
    }

    private ListenerType listenerType;

    private ValueEventListener valueListener;
    private ChildEventListener childListener;

    public FirebaseQueryLiveData(DatabaseReference ref, ListenerType type) {
        this.query = ref;
        listenerType = type;

        valueListener = new MyValueEventListener();
        childListener = this;
    }

    @Override
    protected void onActive() {

        switch (listenerType) {
            case VALUE:
                query.addValueEventListener(valueListener);
                break;
            case CHILD:
                query.addChildEventListener(childListener);
                break;
            case SINGLE_VALUE:
                query.addListenerForSingleValueEvent(valueListener);
        }

    }


    @Override
    protected void onInactive() {

        switch (listenerType) {
            case VALUE:
                query.removeEventListener(valueListener);
                break;
            case CHILD:
                query.removeEventListener(childListener);
                break;
        }
    }


    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, @Nullable String s) {

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

    public class MyValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists()) {
                setValue(dataSnapshot);
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

}
