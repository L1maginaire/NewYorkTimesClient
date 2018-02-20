package com.example.guest.newyorktimesclient.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.example.guest.newyorktimesclient.R;

/**
 * Created by guest on 2/20/18.
 */

public class NetworkErrorActivity extends AppCompatActivity {
    private Button repeatButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_error);
        repeatButton = findViewById(R.id.btn_repeat);
        repeatButton.setOnClickListener(v -> {
            if(isOnline()){
                startActivity(new Intent(this, MainActivity.class));}
        });
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
