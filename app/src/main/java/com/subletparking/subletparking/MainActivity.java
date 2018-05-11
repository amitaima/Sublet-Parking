package com.subletparking.subletparking;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.TimeUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.okhttp.internal.Util;
import com.squareup.okhttp.internal.http.HttpMethod;

import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Timer;

import jp.wasabeef.blurry.Blurry;
import okio.Timeout;

import static android.R.attr.data;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    //private TextView info;
    private LoginButton loginbutton;
    CardView facebookCardView;
    ViewGroup rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginbutton = (LoginButton)findViewById(R.id.facebook_login_button);
        facebookCardView = (CardView)findViewById(R.id.facebookCardView);
        rootView = (ViewGroup) findViewById(R.id.rootView);

        facebookCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (v.getId() == R.id.facebookCardView){
                    loginbutton.performClick();
                }
            }
        });


        // FOR NOW THAT WAY YOU DONT NEED TO LOGOUT AND IN EVERY TIME///////////////////
//        LoginManager.getInstance().logOut();
        ////////////////////////////////////////////////////////////////////////////
        final MyApplication ap = ((MyApplication)this.getApplication());
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        //info = (TextView)findViewById(R.id.info);
        loginbutton.setReadPermissions("email");

        // Callback registration
        loginbutton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                long id = Long.parseLong(loginResult.getAccessToken().getUserId());
                ap.setUserId(id);
                ap.setUserToken(loginResult.getAccessToken());
                fbLoginSuccessfull(String.valueOf(id));
            }

            @Override
            public void onCancel() {
                fbLoginCanceled();
            }

            @Override
            public void onError(FacebookException exception) {
                fbLoginFailed();
            }

        });
        // For connecting when connected
        AccessToken loggedIn = AccessToken.getCurrentAccessToken();
        if (loggedIn!=null)
        {//loggedIn = AccessToken.getCurrentAccessToken() == null;
            String id = loggedIn.getUserId();
            ap.setUserId(Long.parseLong(id));
            ap.setUserToken(loggedIn);
            fbLoginSuccessfull(id);
        }
        ////////Set status bar color///////////
        Window window = this.getWindow();
        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.dark_gray));
    }

    public Bitmap getFacebookProfilePicture(String userID){
        URL imageURL = null;
        try {
            imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    // If facebook login is succeful it comes here
    public void fbLoginSuccessfull(String userId) {
        String firstName = "https://graph.facebook.com/" + userId + "/first_name"; //Gets users first name.
        String lastName = "https://graph.facebook.com/" + userId + "/last_name"; //Gets users last name.
        String name = "https://graph.facebook.com/" + userId + "/name?fields=first_name.limit(1)"; //Gets users name.
        //Bitmap bitmap = getFacebookProfilePicture(userId); //Gets profile picture of facebook user.
        /*
            Should add here the first name, last name and the picture to the data base.
        */
        Profile.getCurrentProfile();
        Toast.makeText(this , "Welcome" + firstName + name, Toast.LENGTH_LONG).show(); // Makes a small message.
        Intent intent = new Intent(this, ParkingSpotListActivity.class);
        finish();
        startActivity(intent);
    }

    public void fbLoginCanceled() {
        Toast.makeText(this , "Connection canceled", Toast.LENGTH_LONG).show(); // Makes a small message.
    }

    public void fbLoginFailed() {
        Toast.makeText(this , "Error in connection", Toast.LENGTH_LONG).show(); // Makes a small message.
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void launchParkingSpotListActivity(View view) {
        Intent intent = new Intent(this, ParkingSpotListActivity.class);
        finish();
        startActivity(intent);
    }
}
