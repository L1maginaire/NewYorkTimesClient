package com.example.guest.newyorktimesclient.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.guest.newyorktimesclient.R;
import com.example.guest.newyorktimesclient.mvp.model.Article;
import com.example.guest.newyorktimesclient.mvp.model.LatestModel.News;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Created by l1maginaire on 1/25/18.
 **/


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{
    private List<Article> articles = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;

    public NewsAdapter(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.news_item, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    public void addNews(List<Article> news) {
        articles.addAll(news);
        notifyDataSetChanged();
    }

    public void clearNews() {
        articles.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Article article = articles.get(position);
        Glide.with(context).load(article.getPicUrl())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.pic);
        holder.title.setText(article.getTitle());
        holder.summary.setText(article.getSummary());
        holder.published.setText(article.getDate().substring(0, 10));
        holder.itemView.setOnClickListener(v -> {
//            Intent i = BrowserActivity.newIntent(context, Uri.parse(item.getUrl()));
//            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        if (articles == null)
            return 0;
        return articles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.urlToImage) ImageView pic;
        @BindView(R.id.news_title) TextView title;
        @BindView(R.id.news_summary) TextView summary;
        @BindView(R.id.publishedAt) TextView published;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }
    }
}
