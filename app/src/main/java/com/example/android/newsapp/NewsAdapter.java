package com.example.android.newsapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.newsapp.Data.NewsItemContract;
import com.example.android.newsapp.Utilities.NewsItemModel;

import java.util.ArrayList;
import java.util.List;
import com.squareup.picasso.Picasso;

/**
 * Created by ishpr on 6/29/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {
    private List<NewsItemModel> dataArray = new ArrayList<NewsItemModel>();

    NewsListData newsClicked;

   //Cursor for the Database record
    Cursor nCursor;

    Context context;
    public NewsAdapter(NewsListData listener,Cursor cursor)
    {
        newsClicked = listener;
        nCursor = cursor;

    }

    public interface  NewsListData{

        void onClickItemListener(int clickedIndex);
    }
    @Override
    public NewsAdapter.NewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         context = parent.getContext();
        LayoutInflater layoutIdList = LayoutInflater.from(context);
        int layoutIdForList = R.layout.news_layout;
        boolean shouldAttachImmediately = false;

        View view = layoutIdList.inflate(layoutIdForList,parent,shouldAttachImmediately);
        NewsAdapterViewHolder viewHolder = new NewsAdapterViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(NewsAdapter.NewsAdapterViewHolder holder, int position) {


        if (!nCursor.moveToPosition(position))
            return;

        // Update the view holder with the information needed to display
        String title = nCursor.getString(nCursor.getColumnIndex(NewsItemContract.NewsItemEntry.COLUMN_TITLE));
        String desc = nCursor.getString(nCursor.getColumnIndex(NewsItemContract.NewsItemEntry.COLUMN_DESC));
        String publishedAt = nCursor.getString(nCursor.getColumnIndex(NewsItemContract.NewsItemEntry.COLUMN_PUBLISHED_AT));
        String urlTo = nCursor.getString(nCursor.getColumnIndex(NewsItemContract.NewsItemEntry.COLUMN__URL_TO));
        String urlToImage = nCursor.getString(nCursor.getColumnIndex(NewsItemContract.NewsItemEntry.COLUMN_URL_TO_IMAGE));

        holder.newsArticle.setText(title);
        holder.newsDes.setText(desc);
        holder.newsTime.setText(publishedAt);

        if(urlToImage!=null)
        {
            Picasso.with(context)
                    .load(urlToImage)
                    .into(holder.imageView);

        }

    }

    @Override
    public int getItemCount() {
        if (dataArray==null )

        return 0;
        else
            return dataArray.size();
    }

    //  A new function called swapCursor that takes the new cursor and returns void
    public void swapCursor(Cursor cursor) {
        // Check if the current cursor is not null, and close it if so
        if(nCursor!=null)
            nCursor.close();
        nCursor = cursor;
        if(nCursor!=null)
            this.notifyDataSetChanged();
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
            final public ImageView imageView;

        public NewsAdapterViewHolder(View view)
        {
            super(view);
            newsArticle = (TextView) view.findViewById(R.id.text_news_title);
            newsDes = (TextView) view.findViewById(R.id.text_news_des);
            newsTime = (TextView) view.findViewById(R.id.text_news_time);
            imageView=(ImageView) view.findViewById(R.id.news_image);

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int clickPos = getAdapterPosition();
            newsClicked.onClickItemListener(clickPos);
        }
    }


}
