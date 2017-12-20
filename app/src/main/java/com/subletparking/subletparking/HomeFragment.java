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

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.*;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by User on 12/18/2017.
 */

public class HomeFragment extends Fragment {
    public static final String BASE_URL = "127.0.0.1:5000/";
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
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
        // Create a very simple REST adapter which points the GitHub API.

        String[] parkings = {"parking 1", "parking 2", "parking 3", "parking 4", "parking 5"};
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


        return myView;
    }

    public void searchParking() {
        Toast.makeText(getActivity(), "searching...", Toast.LENGTH_LONG).show(); // Makes a small message.
    }
}

