package com.comp30023.spain_itproject.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import com.comp30023.spain_itproject.domain.CarerUser;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.domain.Location;

/**
 * Retrofit-compatible interface that co-ordinates all our different HTTP requests.
 */
public interface AccountService {

    public static final String CARER_TYPE = "Carer";
    public static final String DEPENDENT_TYPE = "Dependent";


    @FormUrlEncoded
    @POST("/users/new")
    Call<UserModel> registerUser(
                            @Field("name") String name,
                            @Field("mobile") String phoneNumber,
                            @Field("password") String pin,
                            @Field("userType") String userType);


    @FormUrlEncoded
    @POST("user/login")
    Call<UserModel> loginUser(
                            @Field("mobile") String phoneNumber,
                            @Field("password") String pin);


    @GET("/carers/{id}")
    Call<CarerUser> getCarer(
            @Path("id") String id);


    @GET("/dependents/{id}")
    Call<DependentUser> getDependent(
            @Path("id") String id);


    //CONFIRM
    @FormUrlEncoded
    @POST("/carers/{id}/addDependent")
    Call<DependentUser> addDependent(
                            @Path("id") String carerId,
                            @Field("mobile") String dependentPhoneNumber);


    //CONFIRM
    @FormUrlEncoded
    @POST("dependents/{id}/locations/new")
    Call<ResponseBody> addLocationToDependent(
                            @Path("id") String dependentId,
                            @Field("Location") Location location);

}
