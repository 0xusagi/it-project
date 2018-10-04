package com.comp30023.spain_itproject.ui.carerhome;

import android.support.test.espresso.intent.rule.IntentsTestRule;

import com.comp30023.spain_itproject.network.AccountService;
import com.comp30023.spain_itproject.network.RetrofitClientInstance;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class AddDependentActivityTest {

    @Rule
    IntentsTestRule<AddDependentActivity> mIntentRule = new IntentsTestRule<>(
            AddDependentActivity.class);

    private AccountService service;

    @Before
    public void setUp() throws Exception {
        service = RetrofitClientInstance.getRetrofitInstance().create(AccountService.class);
    }

    @Test
    public void testSearchingDependent() {
        // Create the dependent

    }

    @After
    public void tearDown() throws Exception {
    }
}