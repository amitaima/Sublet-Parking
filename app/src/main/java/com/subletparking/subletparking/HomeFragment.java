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
import java.lang.Exception;
import com.facebook.FacebookButtonBase;
import com.facebook.login.widget.LoginButton;
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
        searchParking();
        searchButton = (Button) myView.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchParking();
            }
        });
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
        }
        return strs;
    }


    public void searchParking() {
        Toast.makeText(getActivity(), "searching...", Toast.LENGTH_LONG).show(); // Makes a small message.
        MyApplication ap = (MyApplication)((ParkingSpotListActivity)this.getActivity()).getApplication();
        Call<List<Parking>> call = ap.getApiService().getHomePage();
        try {
            call.enqueue(new Callback<List<Parking>>(){
                @Override
                public void onResponse(Call<List<Parking>> call, Response<List<Parking>> response) {
                    int statusCode = response.code();
                    List<Parking> parkingPage = response.body();
                    String[] parkings = parkingsToString(parkingPage);
                    ListAdapter listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, parkings);
                    ListView listview = (ListView) myView.findViewById(R.id.listView);
                    listview.setAdapter(listAdapter);
                }
                @Override
                public void onFailure(Call<List<Parking>> call, Throwable t) {
                    // Log error here since request failedString[] parkings = parkingsToString(parkingPage);
                    String[] parkings = {"1", "2", "3",t.getMessage()};
                    ListAdapter listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, parkings);
                    ListView listview = (ListView) myView.findViewById(R.id.listView);
                    listview.setAdapter(listAdapter);
                }
            });
        } catch(Exception e){Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();}//try getting the page;
    }
}

