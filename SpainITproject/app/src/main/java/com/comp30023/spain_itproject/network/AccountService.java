package com.comp30023.spain_itproject.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import com.comp30023.spain_itproject.domain.CarerUser;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.domain.Location;
import com.comp30023.spain_itproject.domain.User;

import java.util.ArrayList;


/**
 * Retrofit-compatible interface that co-ordinates all our different HTTP requests.
 */
public interface AccountService {

    public static final String CARER_TYPE = "Carer";
    public static final String DEPENDENT_TYPE = "Dependent";

    //CONFIRM
    @POST("user/login")
    Call<User> loginUser(@Field("phone_number") String phoneNumber, @Field("pin") String pin);

    @FormUrlEncoded
    @POST("/users/new")
    Call<UserModel> registerUser(@Field("name") String name,
                                 @Field("mobile") String phoneNumber,
                                 @Field("password") String pin,
                                 @Field("userType") String userType);

    //CONFIRM
    @GET("user/new")
    Call<ArrayList<Location>> getLocations(DependentUser dependent);

    //CONFIRM
    //@POST("")
    //Call<> addDependent(@Field("id") int carerId, String dependentPhoneNumber);

    //CONFIRM
    // Subject to change whether using phone number as id or not
    @GET("/carers/{id}")
    Call<CarerUser> getCarer(@Path("id") String id);

    //CONFIRM
    @GET("user/{id}")
    Call<DependentUser> getDependent(@Path("id") String phoneNumber);
}
