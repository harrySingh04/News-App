package com.example.android.newsapp.Utilities;

/**
 * Created by ishpr on 7/1/2017.
 */

public class NewsItemModel {

    private String title;
    private String desc;
    private String publishedAt;
    private String urlTo;
    private String urlToImage;

    public NewsItemModel(String title,String desc,String publishedAt,String urlTo,String urlToImage)
    {
        this.title = title;
        this.desc = desc;
        this.publishedAt = publishedAt;
        this.urlTo = urlTo;
        this.urlToImage = urlToImage;

    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getUrlTo() {
        return urlTo;
    }

    public void setUrlTo(String urlTo) {
        this.urlTo = urlTo;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }
}
