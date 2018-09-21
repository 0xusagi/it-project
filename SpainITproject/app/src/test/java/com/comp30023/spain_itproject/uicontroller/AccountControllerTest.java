package com.comp30023.spain_itproject.uicontroller;

import android.telecom.Call;

import com.comp30023.spain_itproject.network.AccountService;
import com.comp30023.spain_itproject.network.RetrofitClientInstance;
import com.comp30023.spain_itproject.validation.DetailsValidator;
import com.comp30023.spain_itproject.validation.InvalidDetailsException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.xml.validation.Validator;

import static org.junit.Assert.*;

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