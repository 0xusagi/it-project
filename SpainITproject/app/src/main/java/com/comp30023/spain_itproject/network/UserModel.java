package com.comp30023.spain_itproject.network;

import com.google.gson.annotations.SerializedName;

/**
 * A class to correspond with the JSON model on the MongoDB
 */
public class UserModel {

    @SerializedName("_id")
    private String userId;

    @SerializedName("__t")
    private String userType;

    private String name;

    private String mobile;

    private String password;

    public String getUserType() {
        return userType;
    }

    public String getId() {
        return userId;
    }

    public String getName() {
        return name;
    }

}
