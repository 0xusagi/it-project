package com.comp30023.spain_itproject.ui;

import android.support.test.espresso.intent.rule.IntentsTestRule;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.network.AccountService;
import com.comp30023.spain_itproject.network.RetrofitClientInstance;
import com.comp30023.spain_itproject.network.UserModel;
import com.comp30023.spain_itproject.ui.carerhome.CarerHomeActivity;
import com.comp30023.spain_itproject.ui.dependenthome.DependentHomeActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class LoginActivityTest {

    private final String VALID_PHONENUMBER_DEPENDENT = "1234567890";
    private final String VALID_PHONENUMBER_CARER = "0987654321";
    private final String VALID_PIN = "1111";

    private AccountService service = RetrofitClientInstance.getRetrofitInstance().create(AccountService.class);

    @Rule
    public IntentsTestRule<LoginActivity> mIntentRule = new IntentsTestRule<>(LoginActivity.class);


    @Before
    public void setUp() throws Exception {
    }

    /**
     * Test the cancel button
     */
    @Test
    public void testCancelButton() {
        onView(withId(R.id.login_cancelButton)).perform(click());

        intended(hasComponent(StartActivity.class.getName()));
    }

    /**
     * Test whether an existing dependent can login
     */
    @Test
    public void testDependentLogin() throws Exception {
        // Insert the dependent into the server
        UserModel userModel = service.registerUser("Dependent1", VALID_PHONENUMBER_DEPENDENT, VALID_PIN, AccountService.USERTYPE_DEPENDENT,
                FirebaseInstanceId.getInstance().getToken())
                .execute().body();

        // Enter a valid phone number
        onView(withId(R.id.login_phoneNumberLoginField)).perform(typeText(VALID_PHONENUMBER_DEPENDENT), closeSoftKeyboard());

        // Enter a valid pin
        onView(withId(R.id.login_pinLoginField)).perform(typeText(VALID_PIN), closeSoftKeyboard());

        // Check if the new intent is displayed
        onView(withId(R.id.login_loginButton)).perform(click());
        intended(hasComponent(DependentHomeActivity.class.getName()));

        // Delete the dependent from server
        service.deleteDependent(userModel.getId()).execute();
    }

    /**
     * Test whether an existing carer can login
     */
    @Test
    public void testCarerLogin() throws Exception {
        // Create a carerUser in the server
        UserModel userModel = service.registerUser("Carer1", VALID_PHONENUMBER_CARER, VALID_PIN, AccountService.USERTYPE_CARER,
                FirebaseInstanceId.getInstance().getToken())
                .execute().body();

        // Enter a valid phone number
        onView(withId(R.id.login_phoneNumberLoginField)).perform(typeText(VALID_PHONENUMBER_CARER), closeSoftKeyboard());

        // Enter a valid pin
        onView(withId(R.id.login_pinLoginField)).perform(typeText(VALID_PIN), closeSoftKeyboard());

        // Check if the new intent is displayed
        onView(withId(R.id.login_loginButton)).perform(click());
        intended(hasComponent(CarerHomeActivity.class.getName()));

        // Remove the carer from the server
        service.deleteCarer(userModel.getId()).execute();
    }


    @After
    public void tearDown() throws Exception {
    }
}