package com.comp30023.spain_itproject.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import com.comp30023.spain_itproject.domain.User;


public interface AccountService {

    @POST("user/login")
    Call<User> loginUser(@Body String phoneNumber, @Body String pin);
}
