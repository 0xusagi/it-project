package com.comp30023.spain_itproject.ui;

import android.support.test.espresso.intent.rule.IntentsTestRule;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.network.AccountService;
import com.comp30023.spain_itproject.network.RetrofitClientInstance;
import com.comp30023.spain_itproject.ui.dependenthome.DependentHomeActivity;
import com.comp30023.spain_itproject.uicontroller.AccountController;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class LoginActivityTest {

    private static final String VALID_PHONENUMBER_DEPENDENT = "1234567890";
    private static final String VALID_PHONENUMBER_CARER = "0987654321";
    private static final String VALID_PIN = "1111";

    @ClassRule
    public static IntentsTestRule<LoginActivity> mIntentRule = new IntentsTestRule<>(LoginActivity.class);

    private static String dependentId;
    private static String carerId;

    @BeforeClass
    public static void beforeSetUp() throws Exception {
        // Register a dependent so that can sign in later
        LoginHandler.getInstance().register(mIntentRule.getActivity().getApplicationContext(),
                "Dependent1",
                VALID_PHONENUMBER_DEPENDENT,
                VALID_PIN,
                VALID_PIN,
                true);

        dependentId = LoginSharedPreference.getId(mIntentRule.getActivity().getApplicationContext());

        // Logout
        LoginHandler.getInstance().logout(mIntentRule.getActivity().getApplicationContext());

        // Register a carer
        LoginHandler.getInstance().register(mIntentRule.getActivity().getApplicationContext(),
                "Carer1",
                VALID_PHONENUMBER_CARER,
                VALID_PIN,
                VALID_PIN,
                false);

        carerId = LoginSharedPreference.getId(mIntentRule.getActivity().getApplicationContext());

        // Logout
        LoginHandler.getInstance().logout(mIntentRule.getActivity().getApplicationContext());

    }

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

    @Test
    public void testDependentLogin() {
        // Enter a valid phone number
        onView(withId(R.id.phoneNumberLoginField)).perform(typeText(VALID_PHONENUMBER_DEPENDENT), closeSoftKeyboard());

        // Enter a valid pin
        onView(withId(R.id.pinLoginField)).perform(typeText(VALID_PIN), closeSoftKeyboard());

        // Check if the new intent is displayed
        onView(withId(R.id.login_loginButton)).perform(click());
        intended(hasComponent(DependentHomeActivity.class.getName()));
    }


    @After
    public void tearDown() throws Exception {
    }
}