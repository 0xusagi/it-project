package com.comp30023.spain_itproject.firebase.realtime_database;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.comp30023.spain_itproject.RealTimeLocationSharingService;
import com.comp30023.spain_itproject.domain.Position;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseLocationSharingService extends RealTimeLocationSharingService {

    private static FirebaseLocationSharingService instance;
    public static FirebaseLocationSharingService getInstance() {
        if (instance ==  null) {
            instance = new FirebaseLocationSharingService();
        }
        return instance;
    }

    private static final FirebaseDatabase database = MyFirebaseDatabase.getDatabase();
    private static final DatabaseReference baseReference = database.getReference().child("locations");

    private LiveData<Position> positionLiveData;

    @Override
    public LiveData<Position> trackLocation(String userId) {

        DatabaseReference dbReference = baseReference.child(userId);

        FirebaseValueListenerLiveData listener = new FirebaseValueListenerLiveData(dbReference);

        positionLiveData = Transformations.map(listener, new Function<DataSnapshot, Position>() {
            @Override
            public Position apply(DataSnapshot snapshot) {
                return snapshot.getValue(Position.class);
            }
        });

        return positionLiveData;
    }

    @Override
    public void updateLocation(String userId, Position position) {

        DatabaseReference dbReference = baseReference.child(userId);

        dbReference.setValue(position);
    }
}
