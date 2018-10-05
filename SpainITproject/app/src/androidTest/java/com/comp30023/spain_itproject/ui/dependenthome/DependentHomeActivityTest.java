package com.comp30023.spain_itproject.ui.dependenthome;

import android.content.Context;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.comp30023.spain_itproject.domain.CarerUser;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.domain.Location;
import com.comp30023.spain_itproject.ui.LoginHandler;
import com.comp30023.spain_itproject.ui.LoginSharedPreference;
import com.comp30023.spain_itproject.uicontroller.AccountController;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(AndroidJUnit4.class)
public class DependentHomeActivityTest {

    @Rule
    public ActivityTestRule<DependentHomeActivity> activityTestRule = new ActivityTestRule<DependentHomeActivity>(DependentHomeActivity.class);

    DependentUser user;
    List<CarerUser> pendingCarers;
    List<CarerUser> carers;
    List<Location> locations;

    AccountController accountControllerMock;
    LoginHandler loginHandlerMock;


    @Before
    public void init() {

        accountControllerMock = mock(AccountController.class);
        loginHandlerMock = mock(LoginHandler.class);

        user = new DependentUser("Name","1234567890",null, null);

        pendingCarers = new ArrayList<CarerUser>();
        carers = new ArrayList<CarerUser>();
        locations = new ArrayList<Location>();

        for (int i = 0; i < 3; i++) {
            CarerUser carer = new CarerUser("PendingCarer" + i, "000000000" + i, null, null);
            pendingCarers.add(carer);

            carer = new CarerUser("ConfirmedCarer" + i, "111111111" + i, null, null);
            carers.add(carer);

            Location location = new Location(null, "Location" + i);
            locations.add(location);
        }

        try {

            when(AccountController.getInstance()).thenReturn(accountControllerMock);

            when(accountControllerMock.getDependent(
                    anyString()
            )).thenReturn(user);

            doNothing().when(accountControllerMock).respondToCarerRequest(
                    any(DependentUser.class),
                    any(CarerUser.class),
                    anyBoolean()
            );

            when(LoginHandler.getInstance()).thenReturn(loginHandlerMock);

            doNothing().when(loginHandlerMock).logout(any(Context.class));

            when(LoginSharedPreference.getId(any(Context.class))).thenReturn(null);
            when(user.getCarers()).thenReturn(carers);
            when(user.getPendingCarers()).thenReturn(pendingCarers);
            when(user.getLocations()).thenReturn(locations);
            when(any(CarerUser.class).getDependents()).thenReturn(null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loadsPendingCarersTest() {



    }

}