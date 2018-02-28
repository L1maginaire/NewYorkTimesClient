package com.example.guest.newyorktimesclient.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by l1maginaire on 2/28/18.
 */

public class Article implements Parcelable{
    protected Article(Parcel in) {
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
