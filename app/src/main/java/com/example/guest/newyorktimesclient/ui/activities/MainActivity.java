package com.example.guest.newyorktimesclient.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.guest.newyorktimesclient.R;
import com.example.guest.newyorktimesclient.base.BaseActivity;
import com.example.guest.newyorktimesclient.di.components.DaggerNewsComponent;
import com.example.guest.newyorktimesclient.mvp.model.Article;
import com.example.guest.newyorktimesclient.mvp.model.LatestModel.News;
import com.example.guest.newyorktimesclient.mvp.presenter.NewsPresenter;
import com.example.guest.newyorktimesclient.mvp.view.MainView;
import com.example.guest.newyorktimesclient.utils.NetworkChecker;
import com.example.guest.newyorktimesclient.utils.NewsAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements MainView {

    @Inject
    protected NewsPresenter presenter;

    @BindView(R.id.posts_recycle_view)
    RecyclerView recyclerView;
    @BindView(R.id.errorLayout)
    FrameLayout errorLayout;
    @BindView(R.id.btn_repeat)
    Button repeatButton;

    private NewsAdapter adapter;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        setupAdapter();
        loadNews();
    }

    private void loadNews() {
        if (NetworkChecker.isNetAvailable(this)) {
            presenter.getNews();
        } else {
        }
    }

    private void setupAdapter() {
        recyclerView = findViewById(R.id.posts_recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new NewsAdapter(getLayoutInflater());
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void resolveDaggerDependencies() {
        DaggerNewsComponent.builder()
                .applicationComponent(getApplicationComponent())
                .empsModule(new EmpsModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onEmpsLoaded(List<Article> articles) {
        adapter.addNews(articles);
    }

    @Override
    public void onClearItems() {
        adapter.clearNews();
    }
}