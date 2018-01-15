package com.subletparking.subletparking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

import static android.R.attr.data;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {
    public static String userId;
    private CallbackManager callbackManager;
    //private TextView info;
    private LoginButton loginbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);
        //info = (TextView)findViewById(R.id.info);
        loginbutton = (LoginButton)findViewById(R.id.facebook_login_button);

        loginbutton.setReadPermissions("email");
        /*
        // For connecting when connected
        boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        //loggedIn = AccessToken.getCurrentAccessToken() == null;
        if (loggedIn) {
            fbLoginSuccessfull();
        }*/

        // Callback registration
        loginbutton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                userId = loginResult.getAccessToken().getUserId();
                fbLoginSuccessfull(userId);
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

    }

    public void fbLoginSuccessfull(String userId) {
        Toast.makeText(this , "Welcome" + userId, Toast.LENGTH_LONG).show(); // Makes a small message.
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

    public void launchSignUpActivity(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        finish();
        startActivity(intent);
    }

    public void launchParkingSpotListActivity(View view) {
        Intent intent = new Intent(this, ParkingSpotListActivity.class);
        finish();
        startActivity(intent);
    }

    public String getUserId()
    {
        return userId;
    }
}
