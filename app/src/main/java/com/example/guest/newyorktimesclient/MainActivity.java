package com.example.guest.newyorktimesclient;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class MainActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    public Fragment createFragment() {
        return MainFragment.newInstance();
    }
}