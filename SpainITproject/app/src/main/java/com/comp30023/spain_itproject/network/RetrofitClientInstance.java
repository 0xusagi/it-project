package com.comp30023.spain_itproject.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//REFERENCE: https://medium.com/@prakash_pun/retrofit-a-simple-android-tutorial-48437e4e5a23
public class RetrofitClientInstance {

    private static Retrofit retrofit;
    //Change to our own
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

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
