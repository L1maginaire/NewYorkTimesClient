package com.example.guest.newyorktimesclient.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.guest.newyorktimesclient.R;
import com.example.guest.newyorktimesclient.base.BaseActivity;
import com.example.guest.newyorktimesclient.di.components.DaggerNewsComponent;
import com.example.guest.newyorktimesclient.di.modules.NewsModule;
import com.example.guest.newyorktimesclient.mvp.model.Article;
import com.example.guest.newyorktimesclient.mvp.presenter.NewsPresenter;
import com.example.guest.newyorktimesclient.mvp.view.MainView;
import com.example.guest.newyorktimesclient.utils.NetworkChecker;
import com.example.guest.newyorktimesclient.utils.NewsAdapter;
import com.example.guest.newyorktimesclient.utils.OnQueryTextListenerWrapper;
import com.example.guest.newyorktimesclient.utils.QueryPreferences;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements MainView {
    private static final String TAG = "MainActivity";

    private Integer offset = 0;

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
        if (NetworkChecker.INSTANCE.isNetAvailable(this)) {
            presenter.getRecentNews(20, offset);
        } else {

        }
    }

    private void setupAdapter() {
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        final MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnCloseListener(() -> {
            searchView.clearFocus();
            return false;
        });
        searchView.setOnQueryTextListener(new OnQueryTextListenerWrapper() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d(TAG, "QueryTextSubmit: " + s);
                searchView.clearFocus();
                QueryPreferences.setStoredQuery(MainActivity.this, s);
                presenter.getQueryNews(s, 0);
                return true;
            }
        });
        return true;
    }

    @Override
    protected void resolveDaggerDependencies() {
        DaggerNewsComponent.builder()
                .applicationComponent(getApplicationComponent())
                .newsModule(new NewsModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onNewsLoaded(List<Article> articles) {
        adapter.addNews(articles);
    }

    @Override
    public void onClearItems() {
        adapter.clearNews();
    }
}