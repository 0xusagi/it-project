package com.comp30023.spain_itproject.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import com.comp30023.spain_itproject.domain.CarerUser;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.domain.Location;
import com.comp30023.spain_itproject.domain.User;

import java.util.ArrayList;


public interface AccountService {

    //CONFIRM
    @POST("user/login")
    Call<User> loginUser(@Field("phone_number") String phoneNumber, @Field("pin") String pin);

    @POST("user/new")
    Call<User> registerUser(@Field("name") String name, @Field("phone_number") String phoneNumber,
                            @Field("pin") String pin, Boolean isDependent);

    //CONFIRM
    @GET("user/new")
    Call<ArrayList<Location>> getLocations(DependentUser dependent);

    //CONFIRM
    //@POST("")
    //Call<> addDependent(@Field("id") int carerId, String dependentPhoneNumber);

    //CONFIRM
    // Subject to change whether using phone number as id or not
    @GET("user/{id}")
    Call<ArrayList<DependentUser>> getDependentsOfCarer(@Path("id") String phoneNumber);
}
