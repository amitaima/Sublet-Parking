package com.subletparking.subletparking;

import android.app.Application;

import com.facebook.AccessToken;
import com.google.android.gms.internal.pu;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by User on 14-Jan-18.
 */

public class MyApplication extends Application {
    private long userId;
    private String userName;
    private AccessToken userToken;
    public static final String BASE_URL = "http:/192.168.43.244:5000/"; //server url
    Gson gson = new GsonBuilder()
            .setLenient() //this relaxes the gson a lot, letting it parse malformed JSON as well
            .create();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)) //Gson converts the answer to classes
            .build();
    public interface MyApiEndpointInterface {
        // Request method and URL specified in the annotation
        // Callback for the parsed response is the last parameter
        @GET("users/{id}")
        Call<User> getUser(@Path("id") int id);
        @GET("parkings/page/{cntrLat}/{cntrLon}/{zoom}")
        Call<Map<Parking, String>> getHomePage(@Path("cntrLat") double lat, @Path("cntrLon") double lon, @Path("zoom") double zoom);
        @POST("insertparking/")
        Call<Parking> insertParking(@Body Parking parking);
        @GET("userparking/{id}")
        Call<String> hasParking(@Path("id") long id);
        @GET("request/{address}/{OPhour}/{ENDhour}")
        Call<String> requestParking(@Path("address") String address, @Path("OPhour") String OPhour, @Path("ENDhour") String ENDhour);
        @GET("getunsent/{id}")
        Call<List<Message>> getUnsentMessages(@Path("id") long id);
    }
    public MyApiEndpointInterface apiService = retrofit.create(MyApiEndpointInterface.class);

    public MyApiEndpointInterface getApiService()
    {
        return apiService;
    }
    public long getUserId() {
        return userId;
    }
    public void setUserId(long id) {
        userId = id;
    }
    public String getUserName() {return userName;}
    public void setUserName(String name) {userName = name;}
    public AccessToken getUserToken(){return userToken;}
    public void setUserToken(AccessToken at) {userToken = at;}
}
