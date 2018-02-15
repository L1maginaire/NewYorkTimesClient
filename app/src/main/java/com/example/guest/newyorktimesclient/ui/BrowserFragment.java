package com.example.guest.newyorktimesclient.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.guest.newyorktimesclient.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BrowserFragment extends Fragment {
    @BindView(R.id.webView) WebView webView;

    public static final String ARG_URI = "url";
    private Uri mUri;

    public static BrowserFragment newInstance(Uri uri) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);
        BrowserFragment fragment = new BrowserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUri = getArguments().getParcelable(ARG_URI);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.webview, container, false);
        ButterKnife.bind(this, view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                return false;
            }
        });
        webView.loadUrl(mUri.toString());
        return view;
    }
}
