package com.example.guest.newyorktimesclient.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guest.newyorktimesclient.di.components.DaggerNewsComponent;
import com.example.guest.newyorktimesclient.di.components.NewsComponent;
import com.example.guest.newyorktimesclient.di.modules.ContextModule;
import com.example.guest.newyorktimesclient.model.QueryModel.Doc;
import com.example.guest.newyorktimesclient.model.QueryModel.Multimedium;
import com.example.guest.newyorktimesclient.model.LatestModel.Result;
import com.example.guest.newyorktimesclient.R;
import com.example.guest.newyorktimesclient.utils.EndlessScrollImplementation;
import com.example.guest.newyorktimesclient.utils.NytApi;
import com.example.guest.newyorktimesclient.utils.QueryPreferences;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainFragment extends Fragment {
    private static final String TAG = MainFragment.class.getSimpleName();
    private String API_KEY = "0343ec428ded42d19bb3f04b015c2e2b";
    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Adapter adapter;
    private List<Result> news;
    private int offset = 0;
    private NytApi nytApi;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        news = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main, container,
                false);
        mRecyclerView = (RecyclerView) v
                .findViewById(R.id.posts_recycle_view);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addOnScrollListener(new EndlessScrollImplementation(linearLayoutManager) {
            @Override
            public void onLoadMore(int offset) {
                fetch(offset);
            }
        });
        NewsComponent daggerNewsComponent = DaggerNewsComponent.builder()
                .contextModule(new ContextModule(getContext()))
                .build();
        nytApi = daggerNewsComponent.getNewsService();
        fetch(offset);
        setupAdapter();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.main_fragment, menu);
        final MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.clearFocus();
                news = new ArrayList<>();
                fetch(0);
                setupAdapter();
                return true;
            }
        });
        searchView.setOnQueryTextListener
                (new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        Log.d(TAG, "QueryTextSubmit: " + s);
                        searchView.clearFocus();
                        QueryPreferences.setStoredQuery(getActivity(), s);
                        mCompositeDisposable.clear();
                        mCompositeDisposable.add(nytApi.getQuery(s, 1, API_KEY)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .map(data -> data.getResponse().getDocs())
                                .subscribe(results -> {
                                    for (Doc d : results) {
                                        if (d == null)
                                            continue;
                                        List<Multimedium> l = d.getMultimedia();
                                        if (l == null || l.size() == 0)
                                            continue;
                                        Multimedium m = d.getMultimedia().get(0);
                                        Result r = new Result();
                                        String picUrl = m.getUrl();
                                        if (picUrl == null || picUrl.isEmpty())
                                            continue;
                                        r.setThumbnailStandard("https://static01.nyt.com/" + picUrl);
                                        String snippet = d.getSnippet();
                                        if (snippet == null || snippet.isEmpty())
                                            continue;
                                        r.setAbstract(snippet);
                                        String printHeadline = d.getHeadline().getPrintHeadline();
                                        if (printHeadline == null || printHeadline.isEmpty())
                                            continue;
                                        r.setTitle(printHeadline);
                                        String url = d.getWebUrl();
                                        if (url == null || url.isEmpty())
                                            continue;
                                        r.setUrl(url);
                                        String date = d.getPubDate();
                                        if (date == null || date.isEmpty())
                                            continue;
                                        r.setPublishedDate(date);
                                        news.add(r);
                                    }
                                    adapter = new Adapter(news);
                                    mRecyclerView.setAdapter(adapter);
                                })
                        );
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        Log.d(TAG, "QueryTextChange: " + s);
                        return false;
                    }
                });

        searchView.setOnSearchClickListener(v -> {
            String query = QueryPreferences.getStoredQuery(getActivity());
            searchView.setQuery(query, false);
        });
    }

    private void setupAdapter() {
        if (isAdded()) {
            adapter = new Adapter(news);
            mRecyclerView.setAdapter(adapter);
        }
    }

    void fetch(final int offset) {
        mCompositeDisposable.add(nytApi.getDefault(20, API_KEY, offset).subscribeOn(Schedulers.io())
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
                    adapter.notifyItemRangeInserted(offset + counter/*todo: проверка на нули и изменения числа*/, news.size());
                })
        );
    }

    class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private List<Result> news;

        public Adapter(List<Result> news) {
            this.news = news;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
            ViewHolder holder = new ViewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Result post = news.get(position);
            String imgUrl = post.getThumbnailStandard();
            final Uri uri = Uri.parse(imgUrl);
            Picasso.with(getContext()).load(uri).resize(75, 75).into(holder.imageView);
            holder.title.setText(post.getTitle());
            holder.summary.setText(post.getAbstract());
            String s = post.getPublishedDate().substring(0, 10);
            holder.published.setText(s);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = BrowserActivity.newIntent(getActivity(), Uri.parse(post.getUrl()));
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            if (news == null)
                return 0;
            return news.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView title;
            TextView summary;
            TextView published;

            public ViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.urlToImage);
                title = (TextView) itemView.findViewById(R.id.news_title);
                summary = (TextView) itemView.findViewById(R.id.news_summary);
                published = (TextView) itemView.findViewById(R.id.publishedAt);
            }
        }
    }
}