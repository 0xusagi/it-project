package com.comp30023.spain_itproject.ui.carerhome;

import android.accounts.Account;
import android.content.ComponentName;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.comp30023.spain_itproject.R;
import com.comp30023.spain_itproject.domain.User;
import com.comp30023.spain_itproject.network.AccountService;
import com.comp30023.spain_itproject.network.RetrofitClientInstance;
import com.comp30023.spain_itproject.network.UserModel;
import com.comp30023.spain_itproject.ui.LoginHandler;
import com.comp30023.spain_itproject.ui.LoginSharedPreference;
import com.comp30023.spain_itproject.ui.StartActivity;
import com.comp30023.spain_itproject.uicontroller.AccountController;
import com.google.firebase.iid.FirebaseInstanceId;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class CarerHomeActivityTest {
    private static final String CARER_NAME = "Carer1";
    private static final String VALID_PHONENUMBER_CARER = "1234567890";
    private static final String VALID_PIN = "1111";

    private static AccountService service = RetrofitClientInstance.getRetrofitInstance().create(AccountService.class);

    // Store the carer user of the activity
    private static UserModel carer;
    private static String carerToken;

    @Rule
    public IntentsTestRule<CarerHomeActivity> mIntentsRule = new IntentsTestRule<>(
            CarerHomeActivity.class);

    @BeforeClass
    public static void beforeSetup() throws Exception {
        // Create a carer for the activity
        carerToken = FirebaseInstanceId.getInstance().getToken();
        carer = service.registerUser(CARER_NAME,
                VALID_PHONENUMBER_CARER,
                VALID_PIN,
                AccountService.USERTYPE_CARER,
                carerToken)
                .execute().body();

        assert(carer  != null);
    }

    @Before
    public void setUp() throws Exception {
        // Login a carer for the activity
        LoginSharedPreference.setLogIn(mIntentsRule.getActivity().getApplicationContext(),
                VALID_PHONENUMBER_CARER,
                VALID_PIN,
                false,
                carer.getId(),
                carerToken);
    }

    /**
     * Check if the signout button is working
     */
    @Test
    public void verifySignOut() {
        // Clicks the signout button
        onView(withId(R.id.carerHome_settingsButton))
                .perform(click());

        intended(hasComponent(StartActivity.class.getName()));
    }

    /**
     * Check if the add dependent button press loads the next activity
     */
    @Test
    public void verifyAddDependentButtonPress() {
        // Clicks the Add Dependent button
        onView(withId(R.id.carerHome_addDependentButton))
                .perform(click());

        intended(hasComponent(new ComponentName(getTargetContext(), AddDependentActivity.class)));
    }

    /**
     * Check the list where the carer has no dependents
     */
    @Test
    public void checkListViewNoDependents() {
        // check if the list only has one field
        ListView listView = mIntentsRule.getActivity().findViewById(R.id.carerHome_dependentsList);

        assert(listView.getAdapter().getCount() == 1);
    }

    /**
     * Check if the list has a dependent inside
     * @throws Exception
     */
    @Test
    public void checkListViewDependents() throws Exception {
        // Create a dependent to add
        String dependentPhoneNumber1 = "4837594837";
        UserModel dependentUser1 =  service.registerUser("Dependent1",
                dependentPhoneNumber1,
                "1111",
                AccountService.USERTYPE_DEPENDENT,
                FirebaseInstanceId.getInstance().getToken())
                .execute().body();

        String dependentPhoneNumber2 = "0492837261";
        UserModel dependentUser2 = service.registerUser("Dependent2",
                dependentPhoneNumber2,
                "1111",
                AccountService.USERTYPE_DEPENDENT,
                FirebaseInstanceId.getInstance().getToken())
                .execute().body();

        assert(dependentUser1 != null);
        assert(dependentUser2 != null);

        // Send the friend request
        service.addDependent(carer.getId(), dependentPhoneNumber1).execute();
        service.addDependent(carer.getId(), dependentPhoneNumber2).execute();

        // Accept the friend request
        service.acceptRequest(dependentUser1.getId(), carer.getId(), AccountService.CARER_REQUEST_ACCEPT).execute();
        service.acceptRequest(dependentUser2.getId(), carer.getId(), AccountService.CARER_REQUEST_ACCEPT).execute();

        // Click the refresh button
        onView(withId(R.id.carerHome_refreshButton)).perform(click());

        // Check if the list has only one field
        ListView listView = mIntentsRule.getActivity().findViewById(R.id.carerHome_dependentsList);
        assert(listView.getAdapter().getCount() == 2);

        // Delete the dependent
        service.deleteDependent(dependentUser1.getId()).execute();
        service.deleteDependent(dependentUser2.getId()).execute();
    }

    @After
    public void tearDown() throws Exception {
        // Logout each time
        LoginSharedPreference.setLogOut(mIntentsRule.getActivity().getApplicationContext());
    }

    @AfterClass
    public static void afterTearDown() throws  Exception {
        service.deleteCarer(carer.getId()).execute();
    }
}
