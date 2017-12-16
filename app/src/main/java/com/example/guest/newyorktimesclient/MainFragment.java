package com.example.guest.newyorktimesclient;

import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guest.newyorktimesclient.Model.LatestModel.NewsArr;
import com.example.guest.newyorktimesclient.Model.QueryModel.Doc;
import com.example.guest.newyorktimesclient.Model.QueryModel.Multimedium;
import com.example.guest.newyorktimesclient.Model.QueryModel.QueryArr;
import com.example.guest.newyorktimesclient.Model.LatestModel.Result;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {
    private static final String TAG = MainFragment.class.getSimpleName();
    private String API_KEY = "0343ec428ded42d19bb3f04b015c2e2b";
    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Adapter adapter;
    private List<Result> news;
    private int offset = 0;

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
        fetch(offset);
        setupAdapter();
        updateItems();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.main_fragment, menu);
        final MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener
                (new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        Log.d(TAG, "QueryTextSubmit: " + s);
                        App.getApi().getQuery("USSR", 2, API_KEY).enqueue(new Callback<QueryArr>() {
                            @Override
                            public void onResponse(Call<QueryArr> call, Response<QueryArr> response) {
                                if (response.isSuccessful() || response.body() != null) {
                                    news = new ArrayList<>();
                                    com.example.guest.newyorktimesclient.Model.QueryModel.Response resp = response.body().getResponse();
                                    List<Doc> docs = resp.getDocs();
                                    for (Doc d : docs) {
                                        Result r = new Result();
                                        Multimedium m = d.getMultimedia().get(1);
                                        String picUrl = "https://static01.nyt.com/" + m.getUrl();
                                        r.setThumbnailStandard(picUrl);
                                        String snippet = d.getSnippet();
                                        r.setTitle(snippet);
                                        String url = d.getWebUrl();
                                        r.setUrl(url);
                                        if (picUrl == null || picUrl == "" || snippet == null || snippet == "" || url == null || url == "")
                                            continue;
                                        news.add(r);
                                    }
                                    setupAdapter();
                                } else {
                                    try {
                                        Log.d(TAG, response.body().getCopyright());
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<QueryArr> call, Throwable t) {
                                t.printStackTrace();
                                Toast.makeText(getActivity(), "An error occurred during networking", Toast.LENGTH_SHORT).show();
                            }
                        });
                        searchView.clearFocus();//TODO: backbutton&adapter
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        Log.d(TAG, "QueryTextChange: " + s);
                        return false;
                    }
                });

        searchView.setOnSearchClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                String query = QueryPreferences.getStoredQuery(getActivity());
                searchView.setQuery(query, false);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_clear:
                QueryPreferences.setStoredQuery(getActivity(), null);
                updateItems();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupAdapter() {
        if (isAdded()) {
            adapter = new Adapter(news);
            mRecyclerView.setAdapter(adapter);
        }
    }

    private void updateItems() {
        String query = QueryPreferences.getStoredQuery(getActivity());
//        new Fetcher(query).execute();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    void fetch(final int offset) {
        App.getApi().getDefault(20, API_KEY, offset).enqueue(new Callback<NewsArr>() {
            @Override
            public void onResponse(Call<NewsArr> call, Response<NewsArr> response) {
                if (response.isSuccessful() || response.body() != null) {
                    List<Result> callbackArr = response.body().getResults();
                    for (int i = 0; i < callbackArr.size(); i++) {
                        Result r = callbackArr.get(i);
                        String summary = r.getAbstract();
                        String title = r.getTitle();
                        String pic = r.getThumbnailStandard();
                        if (r == null || summary.isEmpty() || title.isEmpty() || pic.isEmpty())
                            continue;
                        news.add(r);
                    }
                    adapter.notifyItemRangeInserted(offset + 20/*todo: проверка на нули и изменения числа*/, news.size());
                } else {
                    try {
                        Log.d(TAG, response.body().getResults().toString());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<NewsArr> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(), "An error occurred during networking", Toast.LENGTH_SHORT).show();
            }
        });
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