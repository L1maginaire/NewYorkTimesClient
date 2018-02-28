package com.example.guest.newyorktimesclient.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guest.newyorktimesclient.R;
import com.example.guest.newyorktimesclient.mvp.model.LatestModel.News;
import com.example.guest.newyorktimesclient.ui.activities.BrowserActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by l1maginaire on 1/25/18.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{
    private List<News> news;
    private Context context;

    public NewsAdapter(List<News> news, Context context) {
        this.news = news;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final News item = news.get(position);
        Picasso.with(context).load(item.getThumbnailStandard()).into(holder.imageView);
        holder.title.setText(item.getTitle());
        holder.summary.setText(item.getAbstract());
        holder.published.setText(item.getPublishedDate().substring(0, 10));
        holder.itemView.setOnClickListener(v -> {
            Intent i = BrowserActivity.newIntent(context, Uri.parse(item.getUrl()));
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        if (news == null)
            return 0;
        return news.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // todo: Why ButterKnife cause NPE?
        private ImageView imageView;
        private TextView title;
        private TextView summary;
        private TextView published;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.urlToImage);
            title = (TextView) itemView.findViewById(R.id.news_title);
            summary = (TextView) itemView.findViewById(R.id.news_summary);
            published = (TextView) itemView.findViewById(R.id.publishedAt);
        }
    }
}
