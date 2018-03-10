package com.example.guest.newyorktimesclient.mvp.presenter;

import android.content.Context;
import android.util.Log;

import com.example.guest.newyorktimesclient.BuildConfig;
import com.example.guest.newyorktimesclient.api.NytApi;
import com.example.guest.newyorktimesclient.base.BasePresenter;
import com.example.guest.newyorktimesclient.mvp.model.Article;
import com.example.guest.newyorktimesclient.mvp.model.LatestModel.Response;
import com.example.guest.newyorktimesclient.mvp.model.QueryModel.Doc;
import com.example.guest.newyorktimesclient.mvp.view.MainView;
import com.example.guest.newyorktimesclient.utils.NewsMapper;
import com.example.guest.newyorktimesclient.utils.QueryPreferences;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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
    public NewsPresenter() {
    }

    public void getRecentNews(int limit, int offset) {
        Observable<Response> observable = apiService.getDefault(limit, API_KEY, offset);
        subscribe(observable, new Observer<Response>() {
            @Override
            public void onNext(Response response) {
                List<Article> news = mapper.mapRecent(response);
                Log.d("TITLE", news.get(0).getTitle());
                QueryPreferences.setLastResultId(context, news.get(0).getTitle());
                view.onClearItems();
                view.onNewsLoaded(news);
            }

            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onComplete() {}
        });
    }
    public void getQueryNews(String query, int offset){
        Observable<List<Doc>> observable = apiService.getQuery(query, offset, API_KEY)
                .map(data -> data.getResponse().getDocs());//todo целесообразность
        subscribe(observable, new Observer<List<Doc>>() {
            @Override
            public void onNext(List<Doc> response) {
                List<Article> news = mapper.mapQuery(response);
                Log.d("TITLE", news.get(0).getTitle());
                QueryPreferences.setLastResultId(context, news.get(0).getTitle());//todo
                view.onClearItems();
                view.onNewsLoaded(news);
            }

            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onComplete() {}
        });
    }
}
