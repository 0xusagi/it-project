package com.comp30023.spain_itproject.ui.carerhome;

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

    private static AccountService service;

    private static String userId;
    private static String dependentId;

    @Rule
    public IntentsTestRule<CarerHomeActivity> mIntentsRule = new IntentsTestRule<>(
            CarerHomeActivity.class);

    @BeforeClass
    public static void beforeSetup() throws Exception {
        service = RetrofitClientInstance.getRetrofitInstance().create(AccountService.class);

        //Setup the dependent
        service.registerUser("Dependent1", "1111111111", "1111", "Dependent", FirebaseInstanceId.getInstance().getToken()).execute();

        // Register the carer
        service.registerUser("Carer1", "0000000000", "1111", "Carer", FirebaseInstanceId.getInstance().getToken()).execute();
    }

    @Before
    public void setUp() throws Exception {
        // Register a fake account for testing
        LoginHandler.getInstance().login(getTargetContext(), "0000000000", "1111");
        userId = LoginSharedPreference.getId(getTargetContext());
    }

    /**
     * Check if the signout button is working
     */
    @Test
    public void verifySignOut() {
        // Clicks the signout button
        onView(withId(R.id.carerHome_settingsButton))
                .perform(click());

        intended(hasComponent(new ComponentName(getTargetContext(), StartActivity.class)));
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
        // Add dependents to the carer
        service.addDependent(userId, "1111111111");

        // Accept the request
        service.acceptRequest(dependentId, userId, "accept");

        // Check if the list has only one field
        ListView listView = mIntentsRule.getActivity().findViewById(R.id.carerHome_dependentsList);
        assert(listView.getAdapter().getCount() == 1);

        // Check if the listView can be clicked
        onData(anything()).inAdapterView(withId(R.id.carerHome_dependentsList)).atPosition(0).perform(click());;
    }

    @After
    public void tearDown() throws Exception {
        // Logout each time
        LoginHandler.getInstance().logout(getTargetContext());
    }

    @AfterClass
    public static void afterTearDown() throws Exception {
        // Remove the fake account that was created from the server
        service.deleteCarer(userId).execute();
        service.deleteDependent(dependentId).execute();
    }
}
