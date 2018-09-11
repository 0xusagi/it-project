package com.comp30023.spain_itproject.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;

import com.comp30023.spain_itproject.domain.User;


public interface AccountService {

    @POST("user/login")
    Call<User> loginUser(@Field("phone_number") String phoneNumber, @Field("pin") String pin);

    @POST("user/new")
    Call<User> registerUser(@Body User user);
}
