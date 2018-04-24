package com.subletparking.subletparking;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by User on 12/18/2017.
 */

public class MessagesFragment extends Fragment {
    TextView myTextView;
    View myView;
    ImageButton menuButton;
    DrawerLayout mDrawerLayout;
    ListView myListView;
    Adapter myAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.messages_layout, container, false);
        myListView = (ListView) myView.findViewById(R.id.listView);
        myTextView = (TextView) myView.findViewById(R.id.textView2);
        menuButton = (ImageButton) myView.findViewById(R.id.menuButton1);
        mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        MyApplication ap = (MyApplication)this.getActivity().getApplication();
        Call<List<Message>> call = ap.getApiService().getUnsentMessages(ap.getUserId());
        try {
            call.enqueue(new Callback<List<Message>>() {
                @Override
                public void onResponse(Call<List<Message> > call, Response<List<Message> > response) {
                    List<Message>  messagesList = response.body();
                    ArrayList<String> arrayList = new ArrayList<String>();
                    ArrayAdapter<String> adapter;
                    adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);
                    myListView.setAdapter(adapter);
                    messagesList.add(new Message((long) getId(),"bla","moreshet Levona 294", "2018-03-09 10:00:00" , "2018-03-09 12:00:00"));
                    for (int i = 0; i < messagesList.size(); i++)
                    {
                        Date startHour = messagesList.get(i).getOPhour();
                        Date endHour = messagesList.get(i).getENDhour();
                        String address = messagesList.get(i).getAddress();
                        adapter.add("Your parking at the address: " + address + " was ordered for certain hours");
                        //those should be used. Maybe I'll somehow make it so you know who ordered it.
                    }
                }

                @Override
                public void onFailure(Call<List<Message> > call, Throwable t) {
                    // Log error here since request failedString[] parkings = parkingsToString(parkingPage);
                    Snackbar mySnackbar = Snackbar.make(getView(), t.getMessage(), Snackbar.LENGTH_LONG);
                    mySnackbar.show();
                }
            });
        } catch (Exception e) {
            Snackbar mySnackbar = Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_LONG);
            mySnackbar.show();
        }//try getting the page
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);

            }
        });

        return myView;
    }
}
