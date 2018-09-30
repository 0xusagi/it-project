package com.comp30023.spain_itproject.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.comp30023.spain_itproject.network.BadRequestException;
import com.comp30023.spain_itproject.network.UserModel;
import com.comp30023.spain_itproject.uicontroller.AccountController;

import java.io.IOException;

public class GetDependentNameFromNumberTask extends AsyncTask<Object, Void, String> {
    @Override
    protected String doInBackground(Object... objects) {
        // First argument is the phone number of the dependent to search for
        String phoneNumber = (String) objects[0];

        // Second argument is the context of the activity
        Context context = (Context) objects[1];

        try {
            String name = AccountController.getInstance().getDependentNameByPhoneNumber(phoneNumber);

            return name;
        }
        catch (IOException e) {
            // When cannot prompt whether to try again
            Toast.makeText(context, "Cannot get dependent from phone number", Toast.LENGTH_SHORT).show();
        }
        // Exception when printing invalid request
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
