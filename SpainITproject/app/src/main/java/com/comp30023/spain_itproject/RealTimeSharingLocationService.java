package com.comp30023.spain_itproject;

import android.arch.lifecycle.LiveData;

import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.domain.Position;
import com.comp30023.spain_itproject.domain.User;

public abstract class RealTimeSharingLocationService {

        public abstract void updateLocation(String userId, Position position);

        public abstract LiveData<Position> trackLocation(String userId);
}
