package com.example.guest.newyorktimesclient.mvp.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.example.guest.newyorktimesclient.BuildConfig;
import com.example.guest.newyorktimesclient.api.NytApi;
import com.example.guest.newyorktimesclient.base.BasePresenter;
import com.example.guest.newyorktimesclient.mvp.model.Article;
import com.example.guest.newyorktimesclient.mvp.view.MainView;
import com.example.guest.newyorktimesclient.utils.NewsMapper;
import com.example.guest.newyorktimesclient.utils.QueryPreferences;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by l1maginaire on 2/28/18.
 */

public class NewsPresenter extends BasePresenter<MainView> {
    private static final String API_KEY = BuildConfig.API_KEY;

    @Inject
    protected NytApi apiService;
    @Inject
    protected NewsMapper mapper;
    @Inject
    protected Context context;

    @Inject
    public NewsPresenter() {}

    @SuppressLint("CheckResult")
    public void getRecentNews(int limit, int offset) {
        apiService.getNews(limit, API_KEY, offset)
                .subscribe((response) -> {
                    List<Article> news = mapper.mapRecent(response);
                    Log.d("TITLE of [0]", news.get(0).getTitle());
                    QueryPreferences.setLastResultId(context, news.get(0).getTitle());
                    view.onClearItems();
                    view.onNewsLoaded(news);
                });
    }

    @SuppressLint("CheckResult")
    public void getQueryNews(String query, int offset) {
        apiService.getQuery(query, offset, API_KEY)
                .map(data -> data.getResponse().getDocs())
                .subscribe((response) -> {
                    List<Article> news = mapper.mapQuery(response);
                    Log.d("TITLE of [0]", news.get(0).getTitle());
                    QueryPreferences.setLastResultId(context, news.get(0).getTitle());
                    view.onClearItems();
                    view.onNewsLoaded(news);
                });
    }
}
