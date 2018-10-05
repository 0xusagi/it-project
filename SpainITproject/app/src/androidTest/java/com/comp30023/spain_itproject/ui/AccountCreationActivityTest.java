package com.comp30023.spain_itproject.ui;

import android.support.test.espresso.intent.rule.IntentsTestRule;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.network.AccountService;
import com.comp30023.spain_itproject.network.RetrofitClientInstance;
import com.comp30023.spain_itproject.ui.carerhome.CarerHomeActivity;
import com.comp30023.spain_itproject.ui.dependenthome.DependentHomeActivity;

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
import static org.junit.Assert.*;

public class AccountCreationActivityTest {
    private final String DEPENDENT_NAME = "Dependent1";
    private final String CARER_NAME = "Carer1";
    private final String VALID_DEPENDENT_PHONENUMBER = "1234567890";
    private final String VALID_CARER_PHONENUMBER = "0987654321";
    private final String VALID_PIN = "1111";

    private AccountService service = RetrofitClientInstance.getRetrofitInstance().create(AccountService.class);

    @Rule
    public IntentsTestRule<AccountCreationActivity> mIntentsRule = new IntentsTestRule<>(AccountCreationActivity.class);

    @Before
    public void setUp() throws Exception {
    }

    /**
     * Make sure registering an unregistered dependent shows the right home screen
     */
    @Test
    public void testRegisteringDependent() throws Exception {
        // Fill in the fields
        onView(withId(R.id.nameField)).perform(typeText(DEPENDENT_NAME), closeSoftKeyboard());
        onView(withId(R.id.phoneNumberField)).perform(typeText((VALID_DEPENDENT_PHONENUMBER)), closeSoftKeyboard());
        onView(withId(R.id.pinField)).perform(typeText(VALID_PIN), closeSoftKeyboard());
        onView(withId(R.id.confirmPinField)).perform(typeText(VALID_PIN), closeSoftKeyboard());
        onView(withId(R.id.dependentButton)).perform(click());

        // Now register the user and see what screen pops up
        onView(withId(R.id.registerButton)).perform(click());
        intended(hasComponent(DependentHomeActivity.class.getName()));

        // Delete the dependent
        service.deleteDependent(LoginSharedPreference.getId(mIntentsRule.getActivity().getApplicationContext())).execute();
    }

    /**
     * Test the screen that shows after registering a careruser
     */
    @Test
    public void testRegisteringCarer() throws Exception {
        // Fill in the fields
        onView(withId(R.id.nameField)).perform(typeText(CARER_NAME), closeSoftKeyboard());
        onView(withId(R.id.phoneNumberField)).perform(typeText((VALID_CARER_PHONENUMBER)), closeSoftKeyboard());
        onView(withId(R.id.pinField)).perform(typeText(VALID_PIN), closeSoftKeyboard());
        onView(withId(R.id.confirmPinField)).perform(typeText(VALID_PIN), closeSoftKeyboard());
        onView(withId(R.id.carerButton)).perform(click());

        // Now register the user and see what screen pops up
        onView(withId(R.id.registerButton)).perform(click());
        intended(hasComponent(CarerHomeActivity.class.getName()));

        // Delete the carer
        service.deleteCarer(LoginSharedPreference.getId(mIntentsRule.getActivity().getApplicationContext())).execute();
    }

    //TODO add tests for invalid fields

    @After
    public void tearDown() throws Exception {
    }
}