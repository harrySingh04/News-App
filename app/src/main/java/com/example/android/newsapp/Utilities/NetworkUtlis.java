package com.example.android.newsapp.Utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by ishpr on 6/21/2017.
 */

public class NetworkUtlis {

    public final static String NEWS_BASE_URL = "https://newsapi.org/v1/articles";
    public final static String PAR_QUERY = "source";
    public final static String SORT_STRING = "sortBy";
    public final static String API_KEY_STRING = "apiKey";

    public static URL makeUrl(String parQuery, String sortQuery,String apiKey)
    {
        Uri buildUri = Uri.parse(NEWS_BASE_URL).buildUpon().appendQueryParameter(PAR_QUERY,parQuery).appendQueryParameter(SORT_STRING,sortQuery)
            .appendQueryParameter(API_KEY_STRING,apiKey)
                .build();

        URL url = null;

        try
        {
            url = new URL(buildUri.toString());

        }
        catch(Exception e)
        {
            e.printStackTrace();

        }

       return url;

    }
    public static String getStaticResponseFromHttpUrl(URL url) throws IOException

    {
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
        try
        {

            InputStream in = urlConnection.getInputStream();
            Scanner input = new Scanner(in);
            input.useDelimiter("\\A");
            return(input.hasNext())? input.next():null;


        }finally {
            urlConnection.disconnect();
        }




    }
}
