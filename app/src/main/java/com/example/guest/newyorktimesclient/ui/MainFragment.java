package com.example.guest.newyorktimesclient.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.guest.newyorktimesclient.BuildConfig;
import com.example.guest.newyorktimesclient.R;
import com.example.guest.newyorktimesclient.di.components.DaggerNewsComponent;
import com.example.guest.newyorktimesclient.di.components.NewsComponent;
import com.example.guest.newyorktimesclient.di.modules.ContextModule;
import com.example.guest.newyorktimesclient.interfaces.NytApi;
import com.example.guest.newyorktimesclient.model.LatestModel.Result;
import com.example.guest.newyorktimesclient.model.QueryModel.Doc;
import com.example.guest.newyorktimesclient.model.QueryModel.Multimedium;
import com.example.guest.newyorktimesclient.utils.EndlessScrollImplementation;
import com.example.guest.newyorktimesclient.utils.NewsAdapter;
import com.example.guest.newyorktimesclient.utils.OnQueryTextListenerWrapper;
import com.example.guest.newyorktimesclient.utils.QueryPreferences;
import com.example.guest.newyorktimesclient.utils.WrapContentLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainFragment extends Fragment {
    private static final String TAG = MainFragment.class.getSimpleName();
    private String API_KEY = BuildConfig.API_KEY;
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<Result> news = new ArrayList<>();
    private int offset = 0;
    private NytApi nytApi;
    private CompositeDisposable compositeDisposable;
    private Button repeatButton;
    private FrameLayout errorLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main, container,
                false);
        errorLayout = v.findViewById(R.id.errorLayout);
        repeatButton = v.findViewById(R.id.btn_repeat);
        repeatButton.setOnClickListener(v1 -> {
            if (isOnline()) {
                repeatButton.setVisibility(View.INVISIBLE);
                errorLayout.setVisibility(View.INVISIBLE);
                fetchRecent(offset);
                setupAdapter();
            }
        });
        recyclerView = (RecyclerView) v.findViewById(R.id.posts_recycle_view);
        LinearLayoutManager linearLayoutManager = new WrapContentLinearLayoutManager(getActivity());
        compositeDisposable = new CompositeDisposable();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(new EndlessScrollImplementation(linearLayoutManager) {
            @Override
            public void onLoadMore(int offset) {
                fetchRecent(offset); //todo: switch between recent and query
            }
        });
        NewsComponent daggerNewsComponent = DaggerNewsComponent.builder()
                .contextModule(new ContextModule(getContext()))
                .build();
        nytApi = daggerNewsComponent.getNewsService();
        if (!isOnline()) {
            repeatButton.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.VISIBLE);
        } else {
            fetchRecent(offset);
            setupAdapter();
        }
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.main_fragment, menu);
        final MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnCloseListener(() -> {
            searchView.clearFocus();
            news = new ArrayList<>();
            fetchRecent(0);
            setupAdapter();
            return true;
        });
        searchView.setOnQueryTextListener
                (new OnQueryTextListenerWrapper() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        Log.d(TAG, "QueryTextSubmit: " + s);
                        searchView.clearFocus();
                        QueryPreferences.setStoredQuery(getActivity(), s);
                        compositeDisposable.clear();
                        compositeDisposable.add(nytApi.getQuery(s, 1, API_KEY)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .map(data -> data.getResponse().getDocs())
                                .subscribe(results -> {
                                    fetchByQuery(results);
                                    setupAdapter();
                                })
                        );
                        return true;
                    }
                });

        searchView.setOnSearchClickListener(v -> {
            String query = QueryPreferences.getStoredQuery(getActivity());
            searchView.setQuery(query, false);
        });
    }

    private void setupAdapter() {
        if (isAdded()) {
            adapter = new NewsAdapter(news, getContext());
            recyclerView.setAdapter(adapter);
        }
    }

    void fetchByQuery(List<Doc> results) {
        news = new ArrayList<>();
        for (Doc doc : results) {
            if (doc == null)
                continue;
            List<Multimedium> l = doc.getMultimedia();
            if (l == null || l.size() == 0)
                continue;
            Multimedium m = doc.getMultimedia().get(0);
            Result result = new Result();
            String picUrl = m.getUrl();
            if (picUrl == null || picUrl.isEmpty())
                continue;
            result.setThumbnailStandard("https://static01.nyt.com/" + picUrl);
            String snippet = doc.getSnippet();
            if (snippet == null || snippet.isEmpty())
                continue;
            result.setAbstract(snippet);
            String printHeadline = doc.getHeadline().getPrintHeadline();
            if (printHeadline == null || printHeadline.isEmpty())
                continue;
            result.setTitle(printHeadline);
            String url = doc.getWebUrl();
            if (url == null || url.isEmpty())
                continue;
            result.setUrl(url);
            String date = doc.getPubDate();
            if (date == null || date.isEmpty())
                continue;
            result.setPublishedDate(date);
            news.add(result);
        }
    }

    void fetchRecent(final int offset) {
        compositeDisposable.add(nytApi.getDefault(20, API_KEY, offset).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(data -> data.getResults())
                .subscribe(results -> {
                    int counter = 0;
                    for (int i = 0; i < results.size(); i++) {
                        Result r = results.get(i);
                        String summary = r.getAbstract();
                        String title = r.getTitle();
                        String pic = r.getThumbnailStandard();
                        String url = r.getUrl();
                        if (r == null || summary.isEmpty() || title.isEmpty() || pic.isEmpty() || url.isEmpty())
                            continue;
                        news.add(r);
                        counter++;
                    }
                    adapter.notifyItemRangeInserted(offset + counter/*todo: null-check and counter-check*/, news.size());
                })
        );
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}