package com.example.guest.newyorktimesclient.utils;

import android.support.v7.widget.SearchView;

/**
 * Created by l1maginaire on 2/12/18.
 */

public class OnQueryTextListenerWrapper implements SearchView.OnQueryTextListener{
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
