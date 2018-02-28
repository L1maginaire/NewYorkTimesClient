package com.example.guest.newyorktimesclient.mvp.view;

import com.example.guest.newyorktimesclient.base.BaseView;
import com.example.guest.newyorktimesclient.mvp.model.Article;

import java.util.List;

/**
 * Created by l1maginaire on 2/28/18.
 */

public interface MainView extends BaseView {
    void onEmpsLoaded(List<Article> cakes);
    void onShowToast(String message);
    void onClearItems();
}
