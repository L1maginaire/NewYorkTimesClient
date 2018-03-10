package com.example.guest.newyorktimesclient.mvp.presenter;

import com.example.guest.newyorktimesclient.BuildConfig;
import com.example.guest.newyorktimesclient.api.NytApi;
import com.example.guest.newyorktimesclient.base.BasePresenter;
import com.example.guest.newyorktimesclient.mvp.model.LatestModel.Response;
import com.example.guest.newyorktimesclient.mvp.model.Article;
import com.example.guest.newyorktimesclient.mvp.view.MainView;
import com.example.guest.newyorktimesclient.utils.NewsMapper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by l1maginaire on 2/28/18.
 */

public class NewsPresenter extends BasePresenter<MainView> implements Observer<Response> {
    private static final String API_KEY = BuildConfig.API_KEY;

    @Inject
    protected NytApi apiService;
    @Inject
    protected NewsMapper mapper;

    @Inject
    public NewsPresenter() {
    }

    public void getNews(int limit, int offset) {
        Observable<Response> observable = apiService.getDefault(limit, API_KEY, offset);
        subscribe(observable, this);
    }

    @Override
    public void onError(Throwable e) {
    }

    @Override
    public void onComplete() {}

    @Override
    public void onSubscribe(Disposable d) {}

    @Override
    public void onNext(Response response) {
        List<Article> news = mapper.mapNews(response);
        getView().onClearItems();
        getView().onEmpsLoaded(news);
    }
}
