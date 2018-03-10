package com.example.guest.newyorktimesclient;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

public class PollService extends IntentService {
    private static final String TAG = "PollService";
    public PollService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Log.d(TAG, "message");
        }
    }
}
