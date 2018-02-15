package com.example.guest.newyorktimesclient.ui;

import android.support.v4.app.Fragment;

public class MainActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
            return MainFragment.newInstance();
        }
}