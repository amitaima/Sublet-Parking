package com.subletparking.subletparking;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ////////Set status bar color///////////
        Window window = this.getWindow();
        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorAccent));
    }

    public void launchLogInActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void launchParkingSpotListActivity(View view) {
        Intent intent = new Intent(this, ParkingSpotListActivity.class);
        finish();
        startActivity(intent);
    }
}
