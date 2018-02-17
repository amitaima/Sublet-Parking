package com.subletparking.subletparking;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//retrofit


/**
 * Created by User on 12/18/2017.
 */

public class HomeFragment extends Fragment implements OnMapReadyCallback{

    GoogleMap mGoogleMap;
    MapView mMapView;
    View myView;
    FloatingSearchView mSearchView;
    DrawerLayout mDrawerLayout;
    PlaceAutocompleteFragment placeAutoComplete;

    private Button searchButton;
    private ListView listview;
    private ArrayList<String> stringArrayList;
    private EditText editText;
    private ArrayAdapter<String> stringArrayAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.home_layout, container, false);

        //mSearchView = (FloatingSearchView) myView.findViewById(R.id.floating_search_view);
        mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        //mSearchView.attachNavigationDrawerToMenuButton(mDrawerLayout);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        /*mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchParking("new york");
            }
        });*/

        placeAutoComplete = (PlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.floating_search_view);
        final ImageView searchIcon = (ImageView)((LinearLayout)placeAutoComplete.getView()).getChildAt(0);

// Set the desired icon
        searchIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_black_24dp));

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(Gravity.START);
            }
        });

        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.d("Maps", "Place selected: " + place.getName());
                LatLng l = place.getLatLng();
                CameraPosition SearchedPos = CameraPosition.builder().target(l).zoom(16).bearing(0).build();
                mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(SearchedPos));
            }

            @Override
            public void onError(Status status) {
                Log.d("Maps", "An error occurred: " + status);
            }
        });

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
        MyApplication ap = (MyApplication) this.getActivity().getApplication();
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

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getActivity());
        mGoogleMap = googleMap;
        googleMap.setMapType((GoogleMap.MAP_TYPE_NORMAL));

        mGoogleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                CameraPosition curr = mGoogleMap.getCameraPosition();
            }
        });

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


    public void searchParking(String strAddress) {
        Toast.makeText(getActivity(), "searching...", Toast.LENGTH_LONG).show(); // Makes a small message.
    }
}

