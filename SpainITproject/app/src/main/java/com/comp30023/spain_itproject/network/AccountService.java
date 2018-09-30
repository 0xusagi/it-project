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

    public static final String USERTYPE_CARER = "Carer";
    public static final String USERTYPE_DEPENDENT = "Dependent";
    public static final String BOOLEAN_TRUE = "True";
    public static final String BOOLEAN_FALSE = "False";


    @FormUrlEncoded
    @POST("/users/new")
    Call<UserModel> registerUser(
                            @Field("name") String name,
                            @Field("mobile") String phoneNumber,
                            @Field("password") String pin,
                            @Field("userType") String userType);


    @FormUrlEncoded
    @POST("/user/login")
    Call<UserModel> loginUser(
                            @Field("mobile") String phoneNumber,
                            @Field("password") String pin);


    @GET("/carers/{id}")
    Call<CarerUser> getCarer(
            @Path("id") String id);


    @GET("/dependents/{id}")
    Call<DependentUser> getDependent(
            @Path("id") String id);

    // CONFIRM
    // Get a UserModel which will be a name corresponding to the phone number of a dependent user
    @GET("/dependent/name/{mobile}")
    Call<UserModel> getDependentNameFromPhoneNumber(
            @Path("mobile") String phoneNumber);


    //CONFIRM
    @FormUrlEncoded
    @POST("/dependents/{id}/locations/new")
    Call<ResponseBody> addLocationToDependent(
                            @Path("id") String dependentId,
                            @Field("Location") Location location);

    //CONFIRM
    @FormUrlEncoded
    @POST("/carers/{id}/addDependent")
    Call<DependentUser> addDependent(
            @Path("id") String carerId,
            @Field("mobile") String dependentPhoneNumber);


    @FormUrlEncoded
    @POST("/dependents/{dependentId}/carers/{carerId}/respond")
    Call<ResponseBody> acceptRequest(
            @Path("dependentId") String dependentId,
            @Path("carerId") String carerId,
            @Field("accept") String accept
    );

}
