package com.comp30023.spain_itproject.network;

import com.google.gson.annotations.SerializedName;

/**
 * A class to correspond with the JSON model on the MongoDB
 */
public class UserModel {

    @SerializedName("_id")
    public String userId;

    @SerializedName("__t")
    public String userType;

    public String name;

    public String mobile;

    public String password;

}
