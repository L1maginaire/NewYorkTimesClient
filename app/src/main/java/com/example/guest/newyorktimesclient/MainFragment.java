package com.example.guest.newyorktimesclient;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import android.widget.Button;
import android.widget.Toast;

import com.example.guest.newyorktimesclient.Model.Result;
import com.example.guest.newyorktimesclient.Model.NewsArr;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {
    private String API_KEY = "0343ec428ded42d19bb3f04b015c2e2b";
    RecyclerView mRecyclerView;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    GridLayoutManager mGridLayoutManager;
    Adapter adapter;
    List<Result> news;
    Button b1;
    Button b2;
    private boolean mIsLoading;
    private int offset=0;

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
//        Handler responseHandler = new Handler();
//        mThumbnailDownloader = new ThumbnailDownloader<>(responseHandler);
//        mThumbnailDownloader.setThumbnailDownloadListener(
//                new ThumbnailDownloader.ThumbnailDownloadListener<PhotoHolder>() {
//                    @Override
//                    public void onThumbnailDownloaded(PhotoHolder photoHolder,
//                                                      Bitmap bitmap) {
//                        Drawable drawable = new BitmapDrawable(getResources(),
//                                bitmap);
//                        photoHolder.bindDrawable(drawable);
//                    }
//                }
//        );
//        mThumbnailDownloader.start();
//        mThumbnailDownloader.getLooper();
//        Log.i("TAG", "Background thread started");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main, container,
                false);

        mRecyclerView = (RecyclerView) v
                .findViewById(R.id.posts_recycle_view);
        mRecyclerView.setLayoutManager(mGridLayoutManager = new GridLayoutManager
                (getActivity(), 3));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                visibleItemCount = mGridLayoutManager.getChildCount();
                totalItemCount = mGridLayoutManager.getItemCount();
                pastVisiblesItems = mGridLayoutManager.findFirstVisibleItemPosition();
                if (visibleItemCount + pastVisiblesItems >= totalItemCount && !mIsLoading) {
                f(offset+20);
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
                final int COLUMN_MIN_SIZE = 120;
                int columns = dpWidth / COLUMN_MIN_SIZE;
                mGridLayoutManager.setSpanCount(columns);

                mProcessed = true;
            }
        });
        f(offset);
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
                        Log.d("TAG", "QueryTextSubmit: " + s);
                        QueryPreferences.setStoredQuery(getActivity(), s);
                        hideKeyboard(getActivity());
                        updateItems();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        Log.d("TAG", "QueryTextChange: " + s);
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


    void f(int offset) {
        App.getApi().getDefault(API_KEY, offset).enqueue(new Callback<NewsArr>() {
            @Override
            public void onResponse(Call<NewsArr> call, Response<NewsArr> response) {
                news.addAll(response.body().getResults());
                adapter = new Adapter(news);
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<NewsArr> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(), "An error occurred during networking", Toast.LENGTH_SHORT).show();
            }
        });
    }
}