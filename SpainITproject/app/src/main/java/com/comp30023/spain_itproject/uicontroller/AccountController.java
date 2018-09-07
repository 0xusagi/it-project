package com.comp30023.spain_itproject.uicontroller;

import android.content.SharedPreferences;

import com.comp30023.spain_itproject.domain.CarerUser;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.domain.DetailsValidator;
import com.comp30023.spain_itproject.domain.InvalidDetailsException;
import com.comp30023.spain_itproject.domain.Location;
import com.comp30023.spain_itproject.domain.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AccountController {

    private static DetailsValidator validator;

    public static boolean registerAccount(String name, String phoneNumber, String pin, String confirmPin, Boolean isDependent) throws InvalidDetailsException {

        if (validator == null) {
            validator = DetailsValidator.getInstance();
        }

        validator.checkDetails(name, phoneNumber, pin, confirmPin, isDependent);

        return true;
    }

    public static User login(String phoneNumber, String pin, boolean isDependent) {

        User user;

        if (isDependent) {
            user = new DependentUser(null, null, null);
            ((DependentUser) user).addLocation(new Location(null,"Home"));
            ((DependentUser) user).addLocation(new Location(null,"Church"));
            ((DependentUser) user).addLocation(new Location(null,"Shop"));
            ((DependentUser) user).addLocation(new Location(null,"Etc"));
            ((DependentUser) user).addLocation(new Location(null,"Shop2"));
        } else {
            user = new CarerUser(null, phoneNumber, pin);

            for (int i=0; i< 14; i++) {
                ((CarerUser) user).addDependent(new DependentUser("JOHN"+i, "1234567790", "1234"));
            }
        }



        return user;
    }

    // TODO add other methods here

    public DependentUser getDependentFromServer(String phoneNumber) {
        //

        return new DependentUser("John", "1234", "1234");
    }

    private String sendRequestToServer() {
        return "";
    }

    public ArrayList<DependentUser> getDependentsOfCarer(String carerPhoneNumber) {
        // Make a request to the server to get a list of dependents which are being taken care of
        // by the carer
        // Expects a JSON file in the form of a string
        String response = getDependentsOfCarerFromServer(carerPhoneNumber);
        String jsonString = getJSONStringFromResponse(response);

        ArrayList<DependentUser> dependents = new ArrayList<>();

        // Parse the JSON file
        try {
            // Create JSON Object which represents the string
            JSONObject jsonObject = new JSONObject(jsonString);

            // Get the list of dependents
            JSONArray dependentsJSON = jsonObject.getJSONArray("dependents");


            // Loop through the list and store
            for (int i=0; i<dependentsJSON.length(); i++) {
                DependentUser dependent = makeDependentFromJson((dependentsJSON.getJSONObject(i)));

                // Check if the dependent is null
                if (dependent != null) {
                    dependents.add(dependent);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dependents;
    }

    // Helper method to send a request to the server to get the list of dependents of the carer
    private String getDependentsOfCarerFromServer(String carerPhoneNumber) {
        return "";
    }

    // Helper method to parse the response received from the server and just return the JSON part
    // of the response
    private String getJSONStringFromResponse(String response) {
        return "";
    }

    private DependentUser makeDependentFromJson(JSONObject jsonObject) throws JSONException {
        try {
            DependentUser dependent = new DependentUser(jsonObject.getString("name"),
                        jsonObject.getString("phoneNumber"),
                        jsonObject.getString("pin"));

            return dependent;
        } catch (JSONException e) {
            return null;
        }
    }
}
