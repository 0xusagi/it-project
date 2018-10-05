package com.comp30023.spain_itproject.network;

import com.comp30023.spain_itproject.domain.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//REFERENCE: https://medium.com/@prakash_pun/retrofit-a-simple-android-tutorial-48437e4e5a23
/**
 * This class is responsible for the instantiation of a RetrofitInstance and executing calls to the designated BASE_URL
 */
public class RetrofitClientInstance {

    //Set BASE_URL to this constant when running the server locally and using an emulator
    private static final String EMULATOR_URL = "http://10.13.52.80:3000";

    private static Retrofit retrofit;

    // This will be a Heroku-hosted link.
    private static final String BASE_URL = EMULATOR_URL;

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
