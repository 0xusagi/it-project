package com.comp30023.spain_itproject.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import com.comp30023.spain_itproject.domain.CarerUser;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.domain.Location;
import com.comp30023.spain_itproject.domain.User;

import java.util.ArrayList;
import java.util.List;


/**
 * Retrofit-compatible interface that co-ordinates all our different HTTP requests.
 */
public interface AccountService {

    public static final String CARER_TYPE = "Carer";
    public static final String DEPENDENT_TYPE = "Dependent";

    @FormUrlEncoded
    @POST("user/login")
    Call<UserModel> loginUser(
                            @Field("mobile") String phoneNumber,
                            @Field("password") String pin
                            );

    @FormUrlEncoded
    @POST("/users/new")
    Call<UserModel> registerUser(
                            @Field("name") String name,
                            @Field("mobile") String phoneNumber,
                            @Field("password") String pin,
                            @Field("userType") String userType
                            );

    //CONFIRM
    @FormUrlEncoded
    @GET("/dependents/{id}/locations")
    Call<List<Location>> getLocations(
                            @Path("id") String dependentId
                            );

    //CONFIRM
    //@POST("")
    //Call<> addDependent(@Field("id") int carerId, String dependentPhoneNumber);

    //CONFIRM
    @GET("/carers/{id}")
    Call<CarerUser> getCarer(@Path("id") String id);

    //CONFIRM
    @GET("/dependents/{id}")
    Call<DependentUser> getDependent(@Path("id") String id);

    //CONFIRM
    @FormUrlEncoded
    @GET("/dependents/{id}/carers")
    Call<List<CarerUser>> getCarersOfDependent(
                            @Path("id") String dependentId
                            );

    //CONFIRM
    @FormUrlEncoded
    @GET("/carers/{id}/dependents")
    Call<List<DependentUser>> getDependentsOfCarer(
                            @Path("id") String carerId
                            );

    //CONFIRM
    @FormUrlEncoded
    @PUT("/dependents/{id}")
    Call<User> updateDependentLocation(
                            @Path("id") String dependentId,
                            @Field("lat") float lat,
                            @Field("lng") float lng
                            );


}
