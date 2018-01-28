package com.subletparking.subletparking;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;

import com.facebook.login.LoginManager;

/**
 * Created by User on 12/18/2017.
 */

public class SettingsFragment extends Fragment {

    View myView;
    ImageButton menuButton;
    DrawerLayout mDrawerLayout;
    Button colorThemeButton, fontSizeButton, logOutButton;
    Switch notificationSwitch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.settings_layout, container, false);

        menuButton = (ImageButton) myView.findViewById(R.id.menuButton1);
        mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        colorThemeButton = (Button) myView.findViewById(R.id.colorThemeButton);
        fontSizeButton = (Button) myView.findViewById(R.id.fontSizeButton);
        logOutButton = (Button) myView.findViewById(R.id.logOutButton);
        notificationSwitch = (Switch) myView.findViewById(R.id.notificationSwitch);
        boolean switchState = notificationSwitch.isChecked();

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);

            }
        });

        colorThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);

            }
        });

        fontSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);

            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                LoginManager.getInstance().logOut();
                getActivity().finish();
                startActivity(intent);
            }
        });

        return myView;
    }
}
