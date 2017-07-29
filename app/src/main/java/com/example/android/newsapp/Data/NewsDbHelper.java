package com.example.android.newsapp.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.newsapp.Utilities.NewsItemModel;

import java.util.ArrayList;

import static android.R.attr.version;
import static android.content.ContentValues.TAG;

/**
 * Created by ishpr on 7/26/2017.
 */
// Db helper class for the news item saving into the database
public class NewsDbHelper extends SQLiteOpenHelper {


//Version for the database
    private static final int DATABASE_VERSION=1;

//Database name where data will be stored in the database.

    private static final String DATABASE_NAME="newsItem.db";

    public NewsDbHelper(Context context) {
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        // Create a table to hold news Item  data
        final String SQL_CREATE_NEWS_DATA = "CREATE TABLE " + NewsItemContract.NewsItemEntry.TABLE_NAME + " (" +
                NewsItemContract.NewsItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NewsItemContract.NewsItemEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                NewsItemContract.NewsItemEntry.COLUMN_DESC + " TEXT NOT NULL, " +
                NewsItemContract.NewsItemEntry.COLUMN_PUBLISHED_AT + " TEXT NOT NULL," +
                NewsItemContract.NewsItemEntry.COLUMN__URL_TO + " TEXT NOT NULL," +
                NewsItemContract.NewsItemEntry.COLUMN_URL_TO_IMAGE + " TEXT NOT NULL" +
                "); ";

        db.execSQL(SQL_CREATE_NEWS_DATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // For now simply drop the table and create a new one. This means if you change the
        // DATABASE_VERSION the table will be dropped.
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.
        db.execSQL("DROP TABLE IF EXISTS " + NewsItemContract.NewsItemEntry.TABLE_NAME);
        onCreate(db);
    }

    public static void deleteData(SQLiteDatabase db) {

        db.beginTransaction();
        db.delete(NewsItemContract.NewsItemEntry.TABLE_NAME, null, null);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public static long addNewsEntryDb(SQLiteDatabase db, String title,String desc,String publishedAt,String urlTo,String urlImageTo )
    {
        ContentValues cv = new ContentValues();

        //  Put to insert the News title into the COLUMN_TITLE
        cv.put(NewsItemContract.NewsItemEntry.COLUMN_TITLE,title);
        //  Put to insert the  News Description into the COLUMN_DESC
        cv.put(NewsItemContract.NewsItemEntry.COLUMN_DESC,desc);
        //  Put to insert the News Published date into the COLUMN_PUBLISHED_AT
        cv.put(NewsItemContract.NewsItemEntry.COLUMN_PUBLISHED_AT,publishedAt);
        //  Put to insert the News Url  into the COLUMN_UL_TO
        cv.put(NewsItemContract.NewsItemEntry.COLUMN__URL_TO,urlTo);
        //  Put to insert the News Image url into the COLUMN_URL_TO_IMAGE
        cv.put(NewsItemContract.NewsItemEntry.COLUMN_URL_TO_IMAGE,urlImageTo);

        //  Insert to run an insert query on TABLE_NAME with the ContentValues created
        return db.insert(NewsItemContract.NewsItemEntry.TABLE_NAME,null,cv);


    }
}
