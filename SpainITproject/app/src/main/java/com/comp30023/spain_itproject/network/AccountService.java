package com.comp30023.spain_itproject.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import com.comp30023.spain_itproject.domain.CarerUser;
import com.comp30023.spain_itproject.domain.DependentUser;
import com.comp30023.spain_itproject.domain.Location;
import com.comp30023.spain_itproject.firebase.cloud_messaging.DataMessage;

import java.util.List;

/**
 * Retrofit-compatible interface that co-ordinates all our different HTTP requests.
 */
public interface AccountService {

    public static final String USERTYPE_CARER = "Carer";
    public static final String USERTYPE_DEPENDENT = "Dependent";
    public static final String CARER_REQUEST_ACCEPT = "accept";
    public static final String CARER_REQUEST_REJECT = "reject";

    @FormUrlEncoded
    @POST("/users/new")
    Call<UserModel> registerUser(
                            @Field("name") String name,
                            @Field("mobile") String phoneNumber,
                            @Field("password") String pin,
                            @Field("userType") String userType,
                            @Field("firebaseToken") String firebaseToken
    );

    @FormUrlEncoded
    @POST("/user/login")
    Call<UserModel> loginUser(
                            @Field("mobile") String phoneNumber,
                            @Field("password") String pin,
                            @Field("firebaseToken") String firebaseToken
    );


    @GET("/carers/{id}")
    Call<CarerUser> getCarer(
            @Path("id") String id);

    @DELETE("/carers/{id}")
    Call<CarerUser> deleteCarer(
            @Path("id") String id
    );

    @GET("/dependents/{id}")
    Call<DependentUser> getDependent(
            @Path("id") String id);

    @DELETE("/dependents/{id}")
    Call<DependentUser> deleteDependent(
            @Path("id") String id
    );

    @DELETE("/locations/{id}")
    Call<Location> deleteLocation(
            @Path("id") String id
    );

    // Get a UserModel which will be a name corresponding to the phone number of a dependent user
    @GET("/dependent/name/{mobile}")
    Call<UserModel> getDependentNameFromPhoneNumber(
            @Path("mobile") String phoneNumber);



    @FormUrlEncoded
    @POST("/dependent/{id}/addLocation")
    Call<Location> addLocationToDependent(
                            @Path("id") String dependentId,
                            @Field("googleId") String googleId,
                            @Field("lat") double latitude,
                            @Field("long") double longitude,
                            @Field("displayName") String displayName);


    @FormUrlEncoded
    @PUT("/carers/{id}/addDependent")
    Call<ResponseBody> addDependent(
            @Path("id") String carerId,
            @Field("mobile") String dependentPhoneNumber);


    @FormUrlEncoded
    @PUT("/dependents/{dependentId}/acceptCarer/{carerId}")
    Call<ResponseBody> acceptRequest(
            @Path("dependentId") String dependentId,
            @Path("carerId") String carerId,
            @Field("accept") String accept
    );

    @GET("/dependents/{id}/carers")
    Call<List<CarerUser>> getCarersOfDependent(
            @Path("id") String id
    );

    @GET("/carers/{id}/dependents")
    Call<List<DependentUser>> getDependentsOfCarer(
            @Path("id") String id
    );

    @GET("/dependent/{id}/locations")
    Call<List<Location>> getLocationsOfDependent(
            @Path("id") String id
    );

    @GET("/dependents/{id}/pendingCarers")
    Call<List<CarerUser>> getPendingCarersOfDependent(
            @Path("id") String id
    );

    //CONFIRM
    @FormUrlEncoded
    @PUT("/dependents/{id}/")
    Call<ResponseBody> updateDependentToken(
            @Path("id") String id,
            @Field("firebaseToken") String token
    );

    @FormUrlEncoded
    @PUT("/carers/{id}/")
    Call<ResponseBody> updateCarerToken(
            @Path("id") String id,
            @Field("firebaseToken") String token
    );

    //CONFIRM
    @FormUrlEncoded
    @POST("/dependent/{id}/getHelp")
    Call<ResponseBody> requestHelp(
            @Path("id") String id,
            @Field(DataMessage.DATA_MESSAGE_BODY) String message
    );

    @FormUrlEncoded
    @POST("/user/sendMessage")
    Call<ResponseBody> sendChat(
            @Field("senderId") String senderId,
            @Field("receiverId") String receiverId,
            @Field ("message") String message
    );

    @FormUrlEncoded
    @POST("/user/verify")
    Call<ResponseBody> verify(
            @Field("mobile") String mobile,
            @Field("verificationCode") String verificationCode
    );

    @PUT("/user/{id}/logout")
    Call<ResponseBody> logout(
            @Path("id") String id
    );
}
