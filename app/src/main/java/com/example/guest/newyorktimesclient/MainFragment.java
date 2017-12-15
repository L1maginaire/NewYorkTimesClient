package com.example.guest.newyorktimesclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guest.newyorktimesclient.Model.Result;
import com.example.guest.newyorktimesclient.Model.NewsArr;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {
    private static final String TAG = MainFragment.class.getSimpleName();
    private String API_KEY = "0343ec428ded42d19bb3f04b015c2e2b";
    private RecyclerView mRecyclerView;
    private int visibleThreshold = 5;
    private int previousTotalItemCount = 0;
    private StaggeredGridLayoutManager mGridLayoutManager;
    private Adapter adapter;
    private List<Result> news;
    private boolean mIsLoading;
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


    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main, container,
                false);
        mRecyclerView = (RecyclerView) v
                .findViewById(R.id.posts_recycle_view);
        mGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                visibleThreshold = visibleThreshold * mGridLayoutManager.getSpanCount();
                int lastVisibleItemPosition = 0;
                int totalItemCount = mGridLayoutManager.getItemCount();
                int[] lastVisibleItemPositions = mGridLayoutManager.findLastVisibleItemPositions(null);
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
                if (totalItemCount < previousTotalItemCount) {
                    previousTotalItemCount = totalItemCount;
                    if (totalItemCount == 0) {
                        mIsLoading = true;
                    }
                }
                if (mIsLoading && (totalItemCount > previousTotalItemCount)) {
                    mIsLoading = false;
                    previousTotalItemCount = totalItemCount;
                }
                if (!mIsLoading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
                    offset += 20;
                    fetch(offset);
                    mIsLoading = true;
                }
            }
        });
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            private boolean mProcessed = false;

            @Override
            public void onGlobalLayout() {
                if (mProcessed) {
                    return;
                }

                int width = mRecyclerView.getWidth();
                DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
                int dpWidth = Math.round(width / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
                final int COLUMN_MIN_SIZE = 75;
                int columns = dpWidth / COLUMN_MIN_SIZE;
                mGridLayoutManager.setSpanCount(columns);

                mProcessed = true;
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
        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener
                (new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        Log.d(TAG, "QueryTextSubmit: " + s);
                        QueryPreferences.setStoredQuery(getActivity(), s);
                        hideKeyboard(getActivity());
                        updateItems();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        Log.d(TAG, "QueryTextChange: " + s);
                        return false;
                    }
                });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
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
                    news.addAll(response.body().getResults());
                    adapter.notifyItemRangeInserted(offset + 20, news.size());
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
            final Uri uri = Uri.parse(post.getThumbnailStandard());
            Picasso.with(getContext()).load(uri).resize(75, 75).into(holder.iv); //TODO: pic resize
            holder.site.setText(post.getTitle());
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
            ImageView iv;
            TextView site;

            public ViewHolder(View itemView) {
                super(itemView);
                iv = (ImageView) itemView.findViewById(R.id.iv);
                site = (TextView) itemView.findViewById(R.id.postitem_site);
            }
        }
    }
}