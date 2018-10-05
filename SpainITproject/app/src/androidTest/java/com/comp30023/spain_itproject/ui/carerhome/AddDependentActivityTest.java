package com.comp30023.spain_itproject.ui.carerhome;

import android.support.test.espresso.intent.rule.IntentsTestRule;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.network.AccountService;
import com.comp30023.spain_itproject.network.RetrofitClientInstance;
import com.comp30023.spain_itproject.network.UserModel;
import com.comp30023.spain_itproject.ui.LoginHandler;
import com.comp30023.spain_itproject.ui.LoginSharedPreference;
import com.google.firebase.iid.FirebaseInstanceId;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

public class AddDependentActivityTest {
    private final String DEPENDENT1_NAME = "Dependent1";
    private final String VALID_PHONENUMBER_DEPENDENT1 = "0987654321";
    private static final String VALID_PIN = "1111";

    private static final String CARER_NAME = "Carer1";
    private static final String VALID_PHONENUMBER_CARER1 = "1234567890";

    private static UserModel carer;
    private static String carerToken;

    @Rule
    public IntentsTestRule<AddDependentActivity> mIntentRule = new IntentsTestRule<>(
            AddDependentActivity.class);

    private static AccountService service = RetrofitClientInstance.getRetrofitInstance().create(AccountService.class);

    @BeforeClass
    public static void beforeSetup() throws Exception {
        // Create a user
        carerToken = FirebaseInstanceId.getInstance().getToken();
        carer = service.registerUser(CARER_NAME,
                VALID_PHONENUMBER_CARER1,
                VALID_PIN,
                AccountService.USERTYPE_CARER,
                carerToken)
                .execute().body();
    }

    @Before
    public void setUp() throws Exception {
        // Login the created carer
        LoginSharedPreference.setLogIn(mIntentRule.getActivity().getApplicationContext(),
                VALID_PHONENUMBER_CARER1,
                VALID_PIN,
                false,
                carer.getId(),
                carerToken);
    }

    @Test
    public void testSearchingDependent() throws Exception {
        // Create the dependent
        UserModel dependentUser = service.registerUser(DEPENDENT1_NAME,
                VALID_PHONENUMBER_DEPENDENT1,
                VALID_PIN,
                AccountService.USERTYPE_DEPENDENT,
                FirebaseInstanceId.getInstance().getToken())
                .execute().body();

        // Fill in the phonenumber field
        onView(withId(R.id.addDependent_addMobileNumberEditText)).perform(typeText(VALID_PHONENUMBER_DEPENDENT1), closeSoftKeyboard());

        // Click the search button
        onView(withId(R.id.addDependent_searchButton)).perform(click());

        // Check whether a dialog is popped up after clicking search
        onView(withText("Add Dependent")).check(matches(isDisplayed()));

        // Delete the dependent
        service.deleteDependent(dependentUser.getId()).execute();
    }

    @Test
    public void testAddingDependentYes() throws Exception {
        // Create the dependent
        UserModel dependentUser = service.registerUser(DEPENDENT1_NAME,
                VALID_PHONENUMBER_DEPENDENT1,
                VALID_PIN,
                AccountService.USERTYPE_DEPENDENT,
                FirebaseInstanceId.getInstance().getToken())
                .execute().body();

        // Fill in the phonenumber field
        onView(withId(R.id.addDependent_addMobileNumberEditText)).perform(typeText(VALID_PHONENUMBER_DEPENDENT1), closeSoftKeyboard());

        // Click the search button
        onView(withId(R.id.addDependent_searchButton)).perform(click());

        onView(withText("Yes")).inRoot(isDialog()).perform(click());

        // Delete the dependent
        service.deleteDependent(dependentUser.getId()).execute();
    }

    @After
    public void tearDown() {
        // Logout the carer
        LoginHandler.getInstance().logout(mIntentRule.getActivity().getApplicationContext());
    }

    @AfterClass
    public static void afterTearDown() throws Exception {
        service.deleteCarer(carer.getId()).execute();
    }
}