package com.example.guest.newyorktimesclient.utils;

import android.support.v7.widget.SearchView;
import android.util.Log;

/**
 * Created by l1maginaire on 2/12/18.
 */

public class OnQueryTextListenerWrapper implements SearchView.OnQueryTextListener{
    private static final String TAG = "TextQuery: ";

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }
}
