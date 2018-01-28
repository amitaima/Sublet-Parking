package com.subletparking.subletparking;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by User on 12/18/2017.
 */

public class CustomerServiceFragment extends Fragment {

    View myView;
    ImageButton menuButton;
    DrawerLayout mDrawerLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.customer_service_layout, container, false);

        menuButton = (ImageButton) myView.findViewById(R.id.menuButton1);
        mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);

            }
        });

        return myView;
    }
}
