package com.example.android.newsapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ishpr on 6/29/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {
    private String[] titleData;
    private String[] desData;
    private String[] timeData;
    public NewsAdapter()
    {


    }

    @Override
    public NewsAdapter.NewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutIdList = LayoutInflater.from(context);
        int layoutIdForList = R.layout.news_layout;
        boolean shouldAttachImmediately = false;

        View view = layoutIdList.inflate(layoutIdForList,parent,shouldAttachImmediately);
        NewsAdapterViewHolder viewHolder = new NewsAdapterViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(NewsAdapter.NewsAdapterViewHolder holder, int position) {
        holder.newsArticle.setText(titleData[position]);
        holder.newsDes.setText(desData[position]);
        holder.newsTime.setText(timeData[position]);
    }

    @Override
    public int getItemCount() {
        if (titleData==null )

        return 0;
        else
            return titleData.length;
    }

    public void setTitle(String[] title)
    {
        titleData = title;
        notifyDataSetChanged();

    }
    public void setDesc(String[] desc)
    {
        desData = desc;
        notifyDataSetChanged();

    }
    public void setTime(String[] time)
    {
        timeData = time;
        notifyDataSetChanged();

    }

    public class NewsAdapterViewHolder extends RecyclerView.ViewHolder{
            final public TextView newsArticle;
            final public TextView newsDes;
            final public TextView newsTime;

        public NewsAdapterViewHolder(View view)
        {
            super(view);
            newsArticle = (TextView) view.findViewById(R.id.news_text_view);
            newsDes = (TextView) view.findViewById(R.id.text_news_des);
            newsTime = (TextView) view.findViewById(R.id.text_news_time);

        }

    }


}
