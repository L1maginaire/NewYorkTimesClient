package com.example.guest.newyorktimesclient;

import android.net.Uri;
import android.util.Log;

import com.example.guest.newyorktimesclient.Model.NewsArr;
import com.example.guest.newyorktimesclient.Model.Result;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

public class Fetcher {
    int num = 24;
    //    24.json?limit=10&api-key=0343ec428ded42d19bb3f04b015c2e2b
    private static final String API_KEY = "0343ec428ded42d19bb3f04b015c2e2b";
    private static final String FETCH_RECENTS_METHOD = "flickr.photos.getRecent";
    private static final String SEARCH_METHOD = "flickr.photos.search";
    private static final Uri ENDPOINT = Uri
            .parse("http://api.nytimes.com/svc/news/v3/content/all/all/24.json?limit=10")
            .buildUpon()
            .appendQueryParameter("api-key", API_KEY)
            .build();

    public byte[] getUrlBytes(String urlSpec) throws IOException {

        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public List<Result> fetchRecentPhotos() {
        String url = buildUrl();
        return downloadGalleryItems(url);
    }

//    public List<PhotoItem> searchPhotos(String query) {
//        String url = buildUrl(SEARCH_METHOD, query);
//        return downloadGalleryItems(url);
//    }

    private List<Result> downloadGalleryItems(String url) {
        List<Result> items = new ArrayList<>();
        try {
            String jsonString = getUrlString(url);
            parseItemsViaGson(items, jsonString);
        } catch (IOException ioe) {
            Log.e("BLA", "Failed to fetch items", ioe);
        }
        return items;
    }

    private String buildUrl() {
        return ENDPOINT.toString();
    }

    private void parseItemsViaGson(List<Result> items, String jsonString) {
        Gson gson = new Gson();
        NewsArr arr = gson.fromJson(jsonString, NewsArr.class);
        for (Result r : arr.getResults()) {
            if (r.getUrl() != null) {
                items.add(r);
            }
        }
    }
}