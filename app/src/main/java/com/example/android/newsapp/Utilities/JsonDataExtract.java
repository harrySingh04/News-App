package com.example.android.newsapp.Utilities;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by ishpr on 6/21/2017.
 */

public class JsonDataExtract {
/**
 *  This class pareses the JSON taken from the web response and send the data to the
 *  Post execute method of the Async Task.We will be extracting the text,title,description
 * and the image from the news web response . The data then will be retrieved by the post execute method
 * and will be assigned to the view of the App.
 */

    private String title;
    private String descrption;
    private String publishedAt;
    private String urlToSite;
    private String imageContent;



    public static final String PARAM_STATUS = "status";
    public final static String PARAM_ARTICLES = "articles";
    public final static String PARAM_TITLE = "title";
    public final static String PARAM_DES = "description";
    public final static String PARAM_IMAGE = "urlToImage";

    public final static String ERROR_CODE = "error";
    public static JSONArray jsonParseNews(String jsonData)
    {

       try {


           JSONObject newsData = new JSONObject(jsonData);

              if(newsData.get(PARAM_STATUS).equals("ok")) {

                    JSONArray articlesData = newsData.getJSONArray(PARAM_ARTICLES);


           return articlesData;


        }

       }
       catch(Exception e)
       {
           e.printStackTrace();

       }

        return null;

    }


}
