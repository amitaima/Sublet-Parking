package com.subletparking.subletparking;

import android.app.Dialog;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import java.text.DateFormat;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.subletparking.subletparking.R.id.parent;

//retrofit


/**
 * Created by User on 12/18/2017.
 */

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap mGoogleMap;
    MapView mMapView;
    View myView;
    FloatingSearchView mSearchView;
    DrawerLayout mDrawerLayout;
    PlaceAutocompleteFragment placeAutoComplete;
    Dialog myDialog;
    Marker myMarker;

    Button close, submitButton, orderButton, startTimeButton, endTimeButton, dateButton;
    RatingBar ratingBar;
    ImageView parkingImage;
    TextView addressText, numberOfRatings, priceText, availableTimeText, distanceText, parkingSizeText, gateText, parkingDescriptionText, sumPriceText;
    DatePicker datePicker;
    TimePicker timePicker;
    String year, month, day, startTime, endTime, date, date2;
    LatLng myPosition;
    int hour, minute, startHour, startMinute, endHour, endMinute;
    boolean pickedDate = false, pickedStartTime = false, pickedEndTime = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.home_layout, container, false);
        mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        placeAutoComplete = (PlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.floating_search_view);
        final ImageView searchIcon = (ImageView) ((LinearLayout) placeAutoComplete.getView()).getChildAt(0);

        // Set the desired icon
        searchIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_black_24dp));
        // Change the icon to grey //
        searchIcon.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.SRC_IN);

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
                CameraPosition SearchedPos = CameraPosition.builder().target(l).zoom(15).bearing(0).build();
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
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    public void getParkings() {
        MyApplication ap = (MyApplication) this.getActivity().getApplication();
        Call<List<Parking>> call = ap.getApiService().getHomePage();
        try {
            call.enqueue(new Callback<List<Parking>>() {
                @Override
                public void onResponse(Call<List<Parking>> call, Response<List<Parking>> response) {
                    int statusCode = response.code();
                    List<Parking> parkingPage = response.body();
                    for (int i = 0; i < parkingPage.size(); i++) {
                        LatLng l = new LatLng(parkingPage.get(i).getLongitude(), parkingPage.get(i).getLatitude());
                        mGoogleMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_parking_pin1_green))
                                .title(parkingPage.get(i).getAddress())
                                .position(l))
                                .setTag(parkingPage.get(i));
//needs to be green or red according to some bool from the server for whether it is currently occupied or not, will be added later
                    }
                }

                @Override
                public void onFailure(Call<List<Parking>> call, Throwable t) {
                    // Log error here since request failedString[] parkings = parkingsToString(parkingPage);
                    Snackbar mySnackbar = Snackbar.make(getView(), t.getMessage(), Snackbar.LENGTH_LONG);
                    mySnackbar.show();
                }
            });
        } catch (Exception e) {
            Snackbar mySnackbar = Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_LONG);
            mySnackbar.show();
        }//try getting the page;*/
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getActivity());
        mGoogleMap = googleMap;
        googleMap.setMapType((GoogleMap.MAP_TYPE_NORMAL));
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
            Toast.makeText(getActivity(), "Turn on location for use of button", Toast.LENGTH_LONG).show();
        }

        googleMap.setPadding(0, 100, 0, 0);

        mGoogleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                CameraPosition curr = mGoogleMap.getCameraPosition();
            }
        });

        myMarker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(32.793523, 35.037458))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_parking_pin1_green))
                .title("lev hamifratz"));
        googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_parking_pin1_red))
                .title("moreshet, levona 294")
                .position(new LatLng(32.824685, 35.234116)));

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                myParkingDialog(marker);
                return false;
            }
        });

        CameraPosition Liberty = CameraPosition.builder().target(new LatLng(32.824685, 35.234116)).zoom(15).bearing(0).build();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));
        getParkings();
    }

    public void myParkingDialog(final Marker marker) {
        myDialog = new Dialog(getActivity());
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.parking_dialog);
        myDialog.setTitle("Parking Dialog");
        myDialog.show();

        orderButton = (Button)myDialog.findViewById(R.id.orderButton);
        close = (Button)myDialog.findViewById(R.id.close);
        ratingBar = (RatingBar)myDialog.findViewById(R.id.MyRating);
        parkingImage = (ImageView) myDialog.findViewById(R.id.parkingImage);
        addressText = (TextView)myDialog.findViewById(R.id.addressText);
        numberOfRatings = (TextView)myDialog.findViewById(R.id.numberOfRatings);
        priceText = (TextView)myDialog.findViewById(R.id.priceText);
        availableTimeText = (TextView)myDialog.findViewById(R.id.availableTimeText);
        distanceText = (TextView)myDialog.findViewById(R.id.distanceText);
        parkingSizeText = (TextView)myDialog.findViewById(R.id.parkingSizeText);
        gateText = (TextView)myDialog.findViewById(R.id.gateText);
        parkingDescriptionText = (TextView)myDialog.findViewById(R.id.parkingDescriptionText);
        final Parking current = (Parking)marker.getTag();
        addressText.setText(marker.getTitle());

        double rating = current.getRating();
        ratingBar.setRating((float)rating);
        String raters = "(" + Integer.toString(current.getNumberOfRaters()) + ")";
        numberOfRatings.setText(raters);
        String price = Integer.toString(current.getCostPerHour()) + "₪\nper hour";
        priceText.setText(price);
        String time = "Unoccupied on: " + current.getHours();
        availableTimeText.setText(time);
        String distance = "200" + " meters" + " from destination"; //curretly redundant
        distanceText.setText(distance);
        String size = current.getSize() + " parking";
        parkingSizeText.setText(size);
        String gate = "with";
        if (current.getIsGate() == false) {gate = "without";}
        gate = "Parking " + gate + " a gate";
        gateText.setText(gate);
        parkingDescriptionText.setText(current.getDescription());


        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel(); // Exits existing dialog
                myOrderDialog();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel(); // Exits dialog
            }
        });

    }

    public void myOrderDialog () {
        myDialog = new Dialog(getActivity());
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.order_dialog);
        myDialog.setTitle("Order Dialog");
        myDialog.show();

        startTimeButton = (Button)myDialog.findViewById(R.id.startTimeButton);
        endTimeButton = (Button)myDialog.findViewById(R.id.endTimeButton);
        dateButton = (Button)myDialog.findViewById(R.id.dateButton);
        submitButton = (Button)myDialog.findViewById(R.id.submitButton);
        close = (Button)myDialog.findViewById(R.id.close);
        sumPriceText = (TextView)myDialog.findViewById(R.id.sumPriceText);

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel(); // Exits existing dialog
                dateDialog();
            }
        });

        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel(); // Exits existing dialog
                timeDialog(1);
            }
        });

        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel(); // Exits existing dialog
                timeDialog(2);
            }
        });

        if (pickedDate == true)
        {
            date = year + "-" + month + "-" + day;
            DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
            Date result = null;
            try {
                result =  df.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar c = Calendar.getInstance();
            c.setTime(result);
            c.add(Calendar.DATE, 1);
            Date newDate = c.getTime();
            date2 = df.format(newDate);
            dateButton.setHint(date);
        }
        if (pickedStartTime == true)
        {
            if (startHour<10) startTime = "0"; else startTime = ""; //pad with 0
            if (startMinute <10)
                startTime += String.valueOf(startHour) + ":" + "0" + String.valueOf(startMinute) + ":00";//pad with 0s
            else
                startTime += String.valueOf(startHour) + ":" + String.valueOf(startMinute) + ":00";//pad with 0s
            startTimeButton.setHint(startTime);
        }
        if (pickedEndTime == true)
        {
            if (endHour < 10) endTime = "0"; else endTime = "";//pad with 0
            if (endMinute <10)
                endTime += String.valueOf(endHour) + ":" + "0" + String.valueOf(endMinute) + ":00";//pad with 0s
            else
                endTime += String.valueOf(endHour) + ":" + String.valueOf(endMinute) + ":00";//pad with 0s
            endTimeButton.setHint(endTime);
        }

        if(date !=null && startTime != null && endTime != null)
        {
            int price = 12; // get price of parking here
            if(endHour>startHour) // checking if the time passess a day.
            {
                sumPriceText.setText("Price: " + price*(endHour - startHour));
            }
            else
            {
                sumPriceText.setText("Price: " + price*(endHour+24 - startHour));
            }
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel(); // Exits existing dialog
                MyApplication ap = (MyApplication) getActivity().getApplication();
                String startDatetime = date+" "+startTime, endDatetime = date2+" "+endTime;
                ap.getApiService().requestParking(addressText.getText().toString(), startDatetime, endDatetime).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(retrofit2.Call<String> call, Response<String> response)
                    {
                        if(response.body().equals("available"))
                            Toast.makeText(getActivity(), "The parking has been successfully oredered!", Toast.LENGTH_LONG).show();
                        else if (response.body().equals("available"))
                            Toast.makeText(getActivity(), "Oh no! Looks like the hours you requested aren't available!", Toast.LENGTH_LONG).show();
                        else Toast.makeText(getActivity(), "Oh no! It seems like a bug as occured, sorry!", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onFailure(retrofit2.Call<String> call, Throwable t) {}
                });
                //Toast.makeText(getActivity(), "Order has been submitted\n" + date + "\n" + startTime + " - " + endTime + "\n" + sumPriceText.getText(), Toast.LENGTH_LONG).show(); // Makes a small message.
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel(); // Exits existing dialog
            }
        });

    }

    public void timeDialog(final int id) {
        myDialog = new Dialog(getActivity());
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.time_dialog);
        myDialog.setTitle("Time Dialog");
        myDialog.show();

        submitButton = (Button)myDialog.findViewById(R.id.submitButton);
        close = (Button)myDialog.findViewById(R.id.close);
        timePicker = (TimePicker)myDialog.findViewById(R.id.timePicker);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel(); // Exits existing dialog
                hour = timePicker.getCurrentHour(); // Getting time and making it from int to string
                minute = timePicker.getCurrentMinute();
                if(id == 1)
                {
                    pickedStartTime = true;
                    startHour = hour;
                    startMinute = minute;
                }
                else
                {
                    pickedEndTime = true;
                    endHour = hour;
                    endMinute = minute;
                }
                myOrderDialog(); // with changes
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel(); // Exits existing dialog
                if(id == 1)
                {
                    pickedStartTime = false;
                }
                else
                {
                    pickedEndTime = false;
                }
                myOrderDialog(); // without changes
            }
        });


    }

    public void dateDialog() {
        myDialog = new Dialog(getActivity());
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.date_dialog);
        myDialog.setTitle("Date Dialog");
        myDialog.show();

        submitButton = (Button)myDialog.findViewById(R.id.submitButton);
        close = (Button)myDialog.findViewById(R.id.close);
        datePicker = (DatePicker)myDialog.findViewById(R.id.datePicker);

        year = String.valueOf(datePicker.getYear()); // Getting date and making it from int to string
        if (datePicker.getMonth()<10) month = "0"; else month = ""; //pad with 0
        month += String.valueOf(datePicker.getMonth());
        if (datePicker.getDayOfMonth()<10) day = "0"; else day = ""; //pad with 0
        day += String.valueOf(datePicker.getDayOfMonth());

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel(); // Exits existing dialog
                year = String.valueOf(datePicker.getYear()); // Getting date and making it from int to string
                if (datePicker.getMonth()<10) month = "0"; else month = ""; //pad with 0
                month += String.valueOf(datePicker.getMonth()+1);
                if (datePicker.getDayOfMonth()<10) day = "0"; else day = ""; //pad with 0
                day += String.valueOf(datePicker.getDayOfMonth());
                pickedDate=true;
                myOrderDialog(); // with changes
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel(); // Exits existing dialog
                pickedDate=false;
                myOrderDialog(); // without changes
            }
        });
    }
}

