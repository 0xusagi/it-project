package com.comp30023.spain_itproject.network;

import com.comp30023.spain_itproject.domain.CarerUser;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * A class to correspond with the JSON model (user.js) on the MongoDB
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

    public static CarerUser parseCarer(Response<?> response) {
        Converter<ResponseBody, CarerUser> converter =
                RetrofitClientInstance.getRetrofitInstance()
                        .responseBodyConverter(CarerUser.class, new Annotation[0]);

        CarerUser carer = null;

        try {
            carer = converter.convert(response.errorBody());
        } catch (IOException e) {

        }
        return carer;
    }
}
