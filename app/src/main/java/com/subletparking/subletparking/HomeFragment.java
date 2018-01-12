package com.subletparking.subletparking;

import android.app.Fragment;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
//retrofit
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.*;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by User on 12/18/2017.
 */

public class HomeFragment extends Fragment {
    public static final String BASE_URL = "http://192.168.43.43:5000/"; //server url
    Gson gson = new GsonBuilder()
            .setLenient()//this relaxes the gson a lot, letting it parse malformed JSON as well
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
        Call<List<Parking>> getHomePage();
    }

    View myView;
    private Button searchButton;
    private ListView listview;
    private ArrayList<String> stringArrayList;
    private EditText editText;
    private ArrayAdapter<String> stringArrayAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.home_layout, container, false);
        MyApiEndpointInterface apiService = retrofit.create(MyApiEndpointInterface.class);
        Call<List<Parking>> call = apiService.getHomePage();
        call.enqueue(new Callback<List<Parking>>(){
            @Override
            public void onResponse(Call<List<Parking>> call, Response<List<Parking>> response) {
                int statusCode = response.code();
                List<Parking> parkingPage = response.body();
                String[] parkings = parkingsToString(parkingPage);
                ListAdapter listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, parkings);
                ListView listview = (ListView) myView.findViewById(R.id.listView);
                listview.setAdapter(listAdapter);


                searchButton = (Button) myView.findViewById(R.id.searchButton);
                searchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchParking();
                    }
                });
            }
            @Override
            public void onFailure(Call<List<Parking>> call, Throwable t) {
                // Log error here since request failedString[] parkings = parkingsToString(parkingPage);
                String[] parkings = {"1", "2", "3",t.getMessage()};
                ListAdapter listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, parkings);
                ListView listview = (ListView) myView.findViewById(R.id.listView);
                listview.setAdapter(listAdapter);


                searchButton = (Button) myView.findViewById(R.id.searchButton);
                searchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchParking();
                    }
                });
            }
        }); //try getting the page;
        return myView;
    }
    public String[] parkingsToString(List<Parking> parkings) {
        int pSize = parkings.size();
        String[] strs = new String[pSize];
        for (int i = 0; i < pSize; i++) {
            strs[i] = "";
            strs[i] = strs[i].concat("Adress: ");
            strs[i] = strs[i].concat(parkings.get(i).getAddress());
            strs[i] = strs[i].concat("\nHours: ");
            strs[i] = strs[i].concat(parkings.get(i).getHours());
            strs[i] = strs[i].concat("\nFor: ");
            strs[i] = strs[i].concat(String.valueOf(parkings.get(i).getCostPerHour()));
            strs[i] = strs[i].concat(" per hour\nRating: ");
            strs[i] = strs[i].concat(String.valueOf(parkings.get(i).getRating()));
            strs[i] = strs[i].concat("Adress: ");
        }
        return strs;
    }

    public void searchParking() {
        Toast.makeText(getActivity(), "searching...", Toast.LENGTH_LONG).show(); // Makes a small message.
    }
}

