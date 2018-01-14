package com.subletparking.subletparking;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by User on 12/18/2017.
 */

public class ProfileFragment extends Fragment {

    View myView;
    Button openDialog;
    Dialog myDialog;
    Button submitButton,close;
    ParkingSpotListActivity ac = (ParkingSpotListActivity)this.getActivity();
    MyApplication ap = (MyApplication)ac.getApplication();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.profile_layout, container, false);

        openDialog = (Button) myView.findViewById(R.id.addParkingButton);
        openDialog.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v){
                myAlertDialog();
           }
        });
        try {
            String id = ap.getUserId();
            //get the application (MyApplication) from the activity; then get the id from the application (MyApplication)
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return myView;
    }

    public void myAlertDialog(){
        myDialog = new Dialog(getActivity());
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.customdialog);
        myDialog.setTitle("My custom dialog");

        submitButton = (Button)myDialog.findViewById(R.id.submitButton);
        close = (Button)myDialog.findViewById(R.id.close);

        submitButton.setEnabled(true);
        close.setEnabled(true);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Parking has been submited", Toast.LENGTH_LONG).show(); // Makes a small message.

                // Wright here the function to add the Parking to server.

                myDialog.cancel(); // Exits dialog
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel(); // Exits dialog
            }
        });

        myDialog.show();
    }
}
