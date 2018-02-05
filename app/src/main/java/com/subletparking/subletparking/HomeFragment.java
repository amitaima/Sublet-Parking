package com.subletparking.subletparking;

import android.app.Fragment;
import android.app.ListActivity;
import android.content.Context;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//retrofit
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import java.lang.Exception;
import java.util.Locale;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.facebook.FacebookButtonBase;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.*;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.R.attr.name;
import static android.content.Context.LOCATION_SERVICE;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;


/**
 * Created by User on 12/18/2017.
 */

public class HomeFragment extends Fragment implements OnMapReadyCallback{

    GoogleMap mGoogleMap;
    MapView mMapView;
    View myView;
    FloatingSearchView mSearchView;
    DrawerLayout mDrawerLayout;

    private Button searchButton;
    private ListView listview;
    private ArrayList<String> stringArrayList;
    private EditText editText;
    private ArrayAdapter<String> stringArrayAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.home_layout, container, false);


        mSearchView = (FloatingSearchView) myView.findViewById(R.id.floating_search_view);
        mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        mSearchView.attachNavigationDrawerToMenuButton(mDrawerLayout);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        try {
            mSearchView.attachNavigationDrawerToMenuButton(mDrawerLayout);
        } catch (Exception e) {
            String s = e.getMessage();
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return myView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) myView.findViewById(R.id.map);
        if(mMapView!=null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    public void getParkings()
    {
        MyApplication ap = (MyApplication)((ParkingSpotListActivity)this.getActivity()).getApplication();
        Call<List<Parking>> call = ap.getApiService().getHomePage();
        try {
            call.enqueue(new Callback<List<Parking>>(){
                @Override
                public void onResponse(Call<List<Parking>> call, Response<List<Parking>> response) {
                    int statusCode = response.code();
                    List<Parking> parkingPage = response.body();
                    for (int i=0; i<parkingPage.size();i++)
                    {
                        LatLng l = new LatLng(parkingPage.get(i).getLongitude(), parkingPage.get(i).getLatitude());
                        mGoogleMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_parking_pin1))
                                .title(parkingPage.get(i).getAddress())
                                .position(l));
                    }
                }
                @Override
                public void onFailure(Call<List<Parking>> call, Throwable t) {
                    // Log error here since request failedString[] parkings = parkingsToString(parkingPage);
                    Snackbar mySnackbar = Snackbar.make(getView(), t.getMessage(), Snackbar.LENGTH_LONG);
                    mySnackbar.show();
                }
            });
        } catch(Exception e)
            {Snackbar mySnackbar = Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_LONG);
            mySnackbar.show();}//try getting the page;*/
    }

    //@Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getActivity());
        mGoogleMap = googleMap;
        googleMap.setMapType((GoogleMap.MAP_TYPE_NORMAL));


        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(32.793523, 35.037458))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_parking_pin1))
                .title("lev hamifratz"));
        googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_parking_pin1))
                .title("moreshet, levona 294")
                .position(new LatLng(32.824685, 35.234116)));


        CameraPosition Liberty = CameraPosition.builder().target(new LatLng(32.824685, 35.234116)).zoom(16).bearing(0).build();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));
        getParkings();
    }


    public void searchParking() {
        Toast.makeText(getActivity(), "searching...", Toast.LENGTH_LONG).show(); // Makes a small message.
    }
}

