package com.comp30023.spain_itproject.controller;


import com.comp30023.spain_itproject.domain.Dependent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Singleton class for account controller since only one controller can exist at one time
 */
public class CarerController {

    public CarerController() {

    }

    // TODO add other methods here

    public Dependent getDependentFromServer(String phoneNumber) {
        //
    }

    private String sendRequestToServer() {
        return "";
    }

    public ArrayList<Dependent> getDependentsOfCarer(String carerPhoneNumber) {
        // Make a request to the server to get a list of dependents which are being taken care of
        // by the carer
        // Expects a JSON file in the form of a string
        String response = getDependentsOfCarerFromServer(String carerPhoneNumber);
        String jsonString = getJSONStringFromResponse(response);

        ArrayList<Dependent> dependents = new ArrayList<Dependent>();

        // Parse the JSON file
        try {
            // Create JSON Object which represents the string
            JSONObject jsonObject = new JSONObject(jsonString);

            // Get the list of dependents
            JSONArray dependentsJSON = jsonObject.getJSONArray("dependents");


            // Loop through the list and store
            for (int i=0; i<dependentsJSON.length(); i++) {
                Dependent dependent = makeDependentFromJson((dependentsJSON.getJSONObject(i)));

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

    private Dependent makeDependentFromJson(JSONObject jsonObject) throws JSONException {
        try {
            Dependent dependent = new Dependent(jsonObject.getString("name"),
                    jsonObject.getString("phoneNumber"),
                    jsonObject.getString("pin"));

            return dependent;
        } catch (JSONException e) {
            return null;
        }
    }

}
