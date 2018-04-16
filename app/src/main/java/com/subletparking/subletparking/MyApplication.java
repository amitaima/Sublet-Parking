package com.subletparking.subletparking;

import android.app.Application;

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
    public static final String BASE_URL = "http:/192.168.43.43:5000/"; //server url
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
        @GET("parkings/page")
        Call<Map<Parking, String>> getHomePage();
        @POST("insertparking/")
        Call<Parking> insertParking(@Body Parking parking);
        @GET("userparking/{id}")
        Call<String> hasParking(@Path("id") long id);
        @GET("request/{address}/{OPhour}/{ENDhour}")
        Call<String> requestParking(@Path("address") String address, @Path("OPhour") String OPhour, @Path("ENDhour") String ENDhour);
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
}