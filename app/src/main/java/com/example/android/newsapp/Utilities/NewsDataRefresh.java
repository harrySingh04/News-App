package com.example.android.newsapp.Utilities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import com.example.android.newsapp.Data.NewsDbHelper;
import com.example.android.newsapp.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import static android.R.attr.data;
import static com.example.android.newsapp.R.id.progressBar;

/**
 * Created by ishpr on 7/28/2017.
 */

public class NewsDataRefresh {

    public static final String ACTION_REFRESH = "refresh";
    private static String TAG = "RefreshTasks";
    private static String result = null;
    private static JSONArray jsonArray;
    // SQLiteDatabase db;

    public static void refreshArticles(Context context) throws JSONException {
        // ArrayList<NewsDetails> newsDetailses =new ArrayList<NewsDetails> ;
        // URL url = NetworkUtils.makeURL();

        try {
            NetworkUtlis utils = new NetworkUtlis();
            URL url = utils.makeUrl("the-next-web", "latest", "17212abb471447c1bc7bcb493fd44d8c");
            result = NetworkUtlis.getStaticResponseFromHttpUrl(url);
            //Extracting the data for the url
            jsonArray =  JsonDataExtract.jsonParseNews(result);
           SQLiteDatabase db = new NewsDbHelper(context).getWritableDatabase();
            NewsDbHelper.deleteData(db);

            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {

                        JSONObject eachRowData = jsonArray.getJSONObject(i);
                        //Adding data into the db
                        NewsDbHelper.addNewsEntryDb(db,eachRowData.getString("title"), eachRowData.getString("description"),
                                eachRowData.getString("publishedAt"), eachRowData.getString("url"), eachRowData.getString("urlToImage"));
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            }

        }catch (Exception e)
        {
            e.printStackTrace();

        }



    }

}
