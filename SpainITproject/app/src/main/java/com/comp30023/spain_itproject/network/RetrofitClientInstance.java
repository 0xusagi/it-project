package com.comp30023.spain_itproject.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This class is responsible for the instantiation of a RetrofitInstance.
 */
//REFERENCE: https://medium.com/@prakash_pun/retrofit-a-simple-android-tutorial-48437e4e5a23
public class RetrofitClientInstance {

    private static Retrofit retrofit;
    // This will be a Heroku-hosted link.
    private static final String BASE_URL = "http://localhost:3000";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}
