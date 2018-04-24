package com.subletparking.subletparking;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.telecom.Call;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.codemybrainsout.placesearch.PlaceSearchDialog;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.data;
import static android.app.Activity.RESULT_OK;

/**
 * Created by User on 12/18/2017.
 */

public class ProfileFragment extends Fragment {

    MyApplication ap;
    View myView;
    Button openDialog, pickAddressButton;
    Dialog myDialog;
    Button submitButton,close;
    ImageButton addTimeButton, menuButton;
    EditText insertTimeStart, insertTimeEnd, insertPrice, insertDescription;
    PlaceAutocompleteFragment insertAddressPlace;
    String address, timeStart, timeEnd, size, description;
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    int price;
    double placeLat=0, placeLon=0;
    public int numberOfLines = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.profile_layout, container, false);
        ap = (MyApplication) this.getActivity().getApplication();
        openDialog = (Button) myView.findViewById(R.id.addParkingButton);
        menuButton = (ImageButton) myView.findViewById(R.id.menuButton1);
        mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.START);

            }
        });
        getHasParking();
        openDialog.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v){
                myAlertDialog();
           }
        });

        return myView;
    }

    public void addressDialog()
    {
        PlaceSearchDialog placeSearchDialog = new PlaceSearchDialog.Builder(getActivity())
                .setLocationNameListener(new PlaceSearchDialog.LocationNameListener() {
                    @Override
                    public void locationName(String locationName) {
                        //set textview or edittext
                        Geocoder coder = new Geocoder(getActivity());
                        try {
                            ArrayList<Address> adresses = (ArrayList<Address>) coder.getFromLocationName(locationName, 50);
                            for(Address add : adresses){
                                placeLon = add.getLongitude();
                                placeLat = add.getLatitude();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .build();
        placeSearchDialog.show();
    }

    public void myAlertDialog(){
        myDialog = new Dialog(getActivity());
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.customdialog);
        myDialog.setTitle("Add Parking Dialog");

        submitButton = (Button)myDialog.findViewById(R.id.submitButton);
        close = (Button)myDialog.findViewById(R.id.close);
        pickAddressButton = (Button) myDialog.findViewById(R.id.pickAddressButton);
        addTimeButton = (ImageButton)myDialog.findViewById(R.id.addTimeButton);
        //insertAddressPlace = (PlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.floating_search_view);
        insertTimeStart = (EditText)myDialog.findViewById(R.id.insertTimeStart);
        insertTimeEnd = (EditText)myDialog.findViewById(R.id.insertTimeEnd);
        insertPrice = (EditText)myDialog.findViewById(R.id.insertPrice);
        insertDescription = (EditText)myDialog.findViewById(R.id.insertDescription);
        Spinner spinner = (Spinner) myDialog.findViewById(R.id.parkingSizeSpinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.sizes_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                size = (String)parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        /*insertAddressPlace.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                placeLat = place.getLatLng().latitude;
                placeLon = place.getLatLng().longitude;
            }

            @Override
            public void onError(Status status) {

            }
        });*/

        pickAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                addressDialog();
            }
        });

        submitButton.setEnabled(true);
        close.setEnabled(true);
        addTimeButton.setEnabled(true);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Parking has been submited", Toast.LENGTH_LONG).show(); // Makes a small message.

                // write here the function to add the Parking to server.
                try {
                    long id = ap.getUserId();
                    int PLACE_PICKER_REQUEST=1;

                    //get all of the info from the layout

                    /*PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    Intent intent;
                    try {
                        intent = builder.build((Activity) getActivity().getApplicationContext());
                        startActivityForResult(intent, PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }*/

                    /*public void onActivityResult(int requestCode, int resultCode, Intent intentData)
                    {
                        if(requestCode==PLACE_PICKER_REQUEST)
                        {
                            if(resultCode==RESULT_OK)
                            {
                                Place place= PlacePicker.getPlace(intentData, this);
                                String address = String.format("Place: %s",place.getAddress());
                            }
                        }

                    }*/


                    timeStart = insertTimeStart.getText().toString();
                    timeEnd = insertTimeEnd.getText().toString();
                    price = Integer.parseInt(insertPrice.getText().toString());
                    description = insertDescription.getText().toString();//CRASHES
                    /////////////////////////////////////

                    Parking parking = new Parking(id, placeLat, placeLon, address, timeStart + " to " + timeEnd, price, 0, 0, size, description, false);
                    //demo parking; still needs: picker from a map to get both address and lat/lon,
                    sendParking(parking);
                    //get the application (MyApplication) from the activity; then get the id from the application (MyApplication)
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                myDialog.cancel(); // Exits dialog
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel(); // Exits dialog
            }
        });

        addTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout topLayoutParent = (LinearLayout)myDialog.findViewById(R.id.timeLayout);
                // add layout
                LinearLayout horizontolLayout = new LinearLayout(getActivity());
                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                horizontolLayout.setLayoutParams(p1);
                horizontolLayout.setOrientation(LinearLayout.HORIZONTAL);
                topLayoutParent.addView(horizontolLayout);

                // Add edittext
                EditText et = new EditText(getActivity());
                // Args: width, height, weight
                LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                p2.setMargins(25,25,25,25);
                et.setLayoutParams(p2);
                et.setInputType(InputType.TYPE_CLASS_DATETIME |InputType.TYPE_DATETIME_VARIATION_TIME);
                et.setHint("start time");
                et.setId(numberOfLines + 1);
                numberOfLines++;
                horizontolLayout.addView(et);

                // Add edittext
                EditText et1 = new EditText(getActivity());
                et1.setLayoutParams(p2);
                et1.setInputType(InputType.TYPE_CLASS_DATETIME |InputType.TYPE_DATETIME_VARIATION_TIME);
                et1.setHint("end time");
                et1.setId(numberOfLines + 1);
                numberOfLines++;
                horizontolLayout.addView(et1);
            }
        });


        myDialog.show();
    }

    public void sendParking(Parking park) {
            ap.getApiService().insertParking(park).enqueue(new Callback<Parking>() {
            @Override
            public void onResponse(retrofit2.Call<Parking> call, Response<Parking> response)
            {
                if(response.isSuccessful()) {
                    Toast.makeText(getActivity(), response.body().toString(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(retrofit2.Call<Parking> call, Throwable t) {}
        });
    }
    public void getHasParking() {
        ap.getApiService().hasParking(ap.getUserId()).enqueue(new Callback<String>() {
            TextView ans = (TextView)myView.findViewById(R.id.hasParkingAns);
            @Override
            public void onResponse(retrofit2.Call<String> call, Response<String> response) {
                //deteremine whether there is already a saved parking
                if(response.isSuccessful()) {
                    String txt = (response.body().equals("success"))? "This user has a parking saved in the database" : "This user has no parking saved in the database";
                    ans.setText(txt);
                }
            }
            @Override
            public void onFailure(retrofit2.Call<String> call, Throwable t) {ans.setText(t.getMessage());}
        });
    }

}

