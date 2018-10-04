package com.comp30023.spain_itproject.ui;

import android.support.test.espresso.intent.rule.IntentsTestRule;

import com.comp30023.spain_itproject.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class StartActivityTest {
    @Rule
    public IntentsTestRule<StartActivity> mIntentRule = new IntentsTestRule<>(StartActivity.class);

    @Before
    public void setUp() throws Exception {
    }

    /**
     * Test register account button
     */
    @Test
    public void testRegisterAccountButton() {
        onView(withId(R.id.createAccountButton)).perform(click());

        intended(hasComponent(AccountCreationActivity.class.getName()));
    }

    /**
     * Test login button
     */
    @Test
    public void testLoginButton() {
        onView(withId(R.id.loginButton)).perform(click());

        intended(hasComponent(AccountCreationActivity.class.getName()));
    }

    @After
    public void tearDown() throws Exception {
    }
}