package com.example.android.newsapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.newsapp.Utilities.NewsItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ishpr on 6/29/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {
    private List<NewsItemModel> dataArray = new ArrayList<NewsItemModel>();

    NewsListData newsClicked;

    public NewsAdapter(NewsListData listener)
    {
        newsClicked = listener;

    }

    public interface  NewsListData{

        void onClickItemListener(int clickedIndex);
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

        holder.newsArticle.setText(dataArray.get(position).getTitle());
        holder.newsDes.setText(dataArray.get(position).getDesc());
        holder.newsTime.setText(dataArray.get(position).getPublishedAt());
    }

    @Override
    public int getItemCount() {
        if (dataArray==null )

        return 0;
        else
            return dataArray.size();
    }

    public void setDataArray(List<NewsItemModel> jsonData)
    {
        dataArray = jsonData;
        notifyDataSetChanged();

    }


    public class NewsAdapterViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
            final public TextView newsArticle;
            final public TextView newsDes;
            final public TextView newsTime;

        public NewsAdapterViewHolder(View view)
        {
            super(view);
            newsArticle = (TextView) view.findViewById(R.id.text_news_title);
            newsDes = (TextView) view.findViewById(R.id.text_news_des);
            newsTime = (TextView) view.findViewById(R.id.text_news_time);

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int clickPos = getAdapterPosition();
            newsClicked.onClickItemListener(clickPos);
        }
    }


}
