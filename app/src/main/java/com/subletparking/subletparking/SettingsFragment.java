package com.subletparking.subletparking;

import android.app.Dialog;
import android.app.Fragment;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Switch;

import com.facebook.login.LoginManager;

import static android.app.Notification.VISIBILITY_PUBLIC;
import static android.app.NotificationManager.IMPORTANCE_DEFAULT;
import static android.app.NotificationManager.IMPORTANCE_HIGH;

/**
 * Created by User on 12/18/2017.
 */

public class SettingsFragment extends Fragment {

    View myView;
    ImageButton menuButton;
    DrawerLayout mDrawerLayout;
    Button colorThemeButton, fontSizeButton, logOutButton, notificationTestButton;
    Switch notificationSwitch;
    Dialog myDialog;

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
        notificationTestButton = (Button) myView.findViewById(R.id.notificationTestButton);
        boolean switchState = notificationSwitch.isChecked();

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);

            }
        });

        notificationSwitch.setChecked(true);

        notificationTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an explicit intent for an Activity in your app
                Intent intent = new Intent(myView.getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(myView.getContext(), 0, intent, 0);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(myView.getContext())
                        .setSmallIcon(R.drawable.ic_test)
                        .setContentTitle("test notification title")
                        .setContentText("this is a test notification text")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("this is the expanded text of the test notification text. Press to enter the settings"))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setVisibility(VISIBILITY_PUBLIC)
                        .setContentIntent(pendingIntent)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(IMPORTANCE_HIGH)
                        .setColor(getResources().getColor(R.color.colorAccent))
                        .setAutoCancel(true);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(myView.getContext());
                notificationManager.notify(0,notificationBuilder.build());

            }
        });

        colorThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myColorDialog();
            }
        });

        fontSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTextDialog();
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

    public void myColorDialog() {
        myDialog = new Dialog(getActivity());
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.color_dialog);
        myDialog.setTitle("App Color Theme");
        myDialog.show();

        Button defaultButton = (Button) myDialog.findViewById(R.id.defaultColorButton);
        Button redButton = (Button) myDialog.findViewById(R.id.redColorButton);
        Button greenButton = (Button) myDialog.findViewById(R.id.greenColorButton);
        Button pinkButton = (Button) myDialog.findViewById(R.id.pinkColorButton);
        Button yellowButton = (Button) myDialog.findViewById(R.id.yellowColorButton);

        defaultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel();
            }
        });

        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel();
            }
        });

        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel();
            }
        });

        pinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel();
            }
        });

        yellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel();
            }
        });


    }

    public void myTextDialog() {
        myDialog = new Dialog(getActivity());
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.text_size_dialog);
        myDialog.setTitle("App Text Size");
        myDialog.show();

        Button closeButton = (Button) myDialog.findViewById(R.id.close);
        Button submitButton = (Button) myDialog.findViewById(R.id.submitButton);
        NumberPicker np = (NumberPicker) myDialog.findViewById(R.id.textSizeNP);

        np.setMinValue(10);
        np.setMaxValue(20);
        np.setWrapSelectorWheel(true);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel();
            }
        });


    }

}
