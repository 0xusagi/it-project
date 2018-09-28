package com.comp30023.spain_itproject.uicontroller;

import com.comp30023.spain_itproject.network.AccountService;
import com.comp30023.spain_itproject.detailsvalidation.DetailsValidator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class AccountControllerTest {
    private static AccountService service;
    private static DetailsValidator validator;


    @Before
    public void setUp() {
        service = Mockito.mock(AccountService.class);
        validator = Mockito.mock(DetailsValidator.class);
    }

    @Test
    public void registerAccountGoodResponse() {

    }

    @Test
    public void login() {
    }

    @Test
    public void getLocations() {
    }

    @Test
    public void addDependent() {
    }

    @Test
    public void getCarer() {
    }

    @Test
    public void getDependent() {
    }
}