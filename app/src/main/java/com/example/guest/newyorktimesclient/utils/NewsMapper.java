package com.example.guest.newyorktimesclient.utils;

import com.example.guest.newyorktimesclient.mvp.model.LatestModel.News;
import com.example.guest.newyorktimesclient.mvp.model.LatestModel.Response;
import com.example.guest.newyorktimesclient.mvp.model.Article;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by l1maginaire on 2/28/18.
 */

public class UsersMapper {

    @Inject
    public UsersMapper() {
    }

    public List<Article> mapNews(Response response) {
        List<Article> articles = new ArrayList<>();
        if (response != null) {
            List<News> news = response.getNews();
            if (news != null) {
                for (News single : news) {
                    Article article = new Article();

                    article.setName(nameRefactoring(emp.getFName()));
                    article.setSurname(nameRefactoring(emp.getLName()));
                    if (emp.getAvatrUrl() == null || emp.getAvatrUrl().isEmpty()) {
                        article.setAvatarUrl("http://wiseheartdesign.com/images/articles/default-avatar.png"); //default pic
                    } else {
                        article.setAvatarUrl(emp.getAvatrUrl());
                    }
                    dateRefactoring(emp.getBirthday(), article);
                    List<String> names = new ArrayList<>();
                    List<String> ids = new ArrayList<>();
                    for (int i = 0; i < emp.getSpecialty().size(); i++) {
                        names.add(emp.getSpecialty().get(i).getName());
                        ids.add(String.valueOf(emp.getSpecialty().get(i).getSpecialtyId()));
                    }
                    article.setSpecName(names);
                    article.setSpecId(ids);
                    articles.add(article);
                    storage.addEmployee(article);
                }
            }
        }
        return articles;
    }
}
