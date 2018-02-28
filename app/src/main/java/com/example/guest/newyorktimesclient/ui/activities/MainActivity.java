package com.example.guest.newyorktimesclient.ui.activities;

import android.support.v4.app.Fragment;

import com.example.guest.newyorktimesclient.ui.fragments.MainFragment;
import com.example.guest.newyorktimesclient.ui.SingleFragmentActivity;

public class MainActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
            return new MainFragment();
        }
}