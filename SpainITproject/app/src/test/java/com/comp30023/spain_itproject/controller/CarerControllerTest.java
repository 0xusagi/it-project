package com.comp30023.spain_itproject.controller;

import junit.extensions.TestSetup;

import org.junit.Before;

import java.io.BufferedReader;
import java.io.FileReader;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class CarerControllerTest {

    private String dependentsListNotEmptyJSON;

    private CarerController carerController;


    @Before
    public void setUp() throws Exception {
        // Create two json strings needed from the file
        BufferedReader br = new BufferedReader(new FileReader("com/comp30023/spain_itproject/testdata/dependentsListEmpty.json"));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            line = br.readLine();
        }

        dependentsListNotEmptyJSON = sb.toString();



        CarerController controller = Mockito.mock(CarerController.class);
        when(controller.getDependentFromServer().thenReturn(dependentsListNotEmptyJSON));

    }

    public void testEmptyListDependents() {
    }
}