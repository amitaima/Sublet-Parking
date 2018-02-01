package com.subletparking.subletparking;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.telecom.Call;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 12/18/2017.
 */

public class ProfileFragment extends Fragment {

    MyApplication ap;
    View myView;
    Button openDialog;
    Dialog myDialog;
    Button submitButton,close;
    ImageButton addTimeButton, menuButton;
    EditText insertAddress, insertTimeStart, insertTimeEnd, insertPrice;
    String address, timeStart, timeEnd;
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    int price;
    public int numberOfLines = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.profile_layout, container, false);
        ap = (MyApplication)((ParkingSpotListActivity)this.getActivity()).getApplication();
        openDialog = (Button) myView.findViewById(R.id.addParkingButton);
        menuButton = (ImageButton) myView.findViewById(R.id.menuButton1);
        mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);

            }
        });

        openDialog.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v){
                myAlertDialog();
           }
        });

        return myView;
    }

    public void myAlertDialog(){
        myDialog = new Dialog(getActivity());
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.customdialog);
        myDialog.setTitle("Add Parking Dialog");

        submitButton = (Button)myDialog.findViewById(R.id.submitButton);
        close = (Button)myDialog.findViewById(R.id.close);
        addTimeButton = (ImageButton)myDialog.findViewById(R.id.addTimeButton);
        insertAddress = (EditText) myDialog.findViewById(R.id.insertAddress);
        insertTimeStart = (EditText)myDialog.findViewById(R.id.insertTimeStart);
        insertTimeEnd = (EditText)myDialog.findViewById(R.id.insertTimeEnd);
        insertPrice = (EditText)myDialog.findViewById(R.id.insertPrice);

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

                    //get all of the info from the layout
                    address = insertAddress.getText().toString();
                    timeStart = insertTimeStart.getText().toString();
                    timeEnd = insertTimeEnd.getText().toString();
                    price = Integer.parseInt(insertPrice.getText().toString());
                    /////////////////////////////////////

                    Parking parking = new Parking(id, 3.3, 3.3, address, timeStart + " to " + timeEnd, price, 0, 0, false, false);
                    //Parking parking = new Parking(id, 3.3, 3.3, "moreshet 101", "1 to 3", 12, 0, 0); //demo parking
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
            public void onResponse(retrofit2.Call<Parking> call, Response<Parking> response) {

                if(response.isSuccessful()) {
                    Toast.makeText(getActivity(), response.body().toString(), Toast.LENGTH_LONG);
                }
            }
            @Override
            public void onFailure(retrofit2.Call<Parking> call, Throwable t) {}
        });
    }
}

