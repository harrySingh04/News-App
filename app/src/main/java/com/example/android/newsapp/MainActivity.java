package com.example.android.newsapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.newsapp.Utilities.JsonDataExtract;
import com.example.android.newsapp.Utilities.KeyClass;
import com.example.android.newsapp.Utilities.NetworkUtlis;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URL;
import java.security.Key;
import java.util.ArrayList;

import static android.R.attr.author;
import static android.R.attr.description;
import static com.example.android.newsapp.Utilities.JsonDataExtract.jsonParseNews;

public class MainActivity extends AppCompatActivity {

        //private TextView newsTextView;
        //private TextView newsDesView;
        //private TextView newsAutView;
        //private TextView newsPubView;
        private ProgressBar progressBar;

        private NewsAdapter newsAdapter;
        private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // newsTextView = (TextView) findViewById(R.id.news_text_view);
       /* newsDesView = (TextView) findViewById(R.id.news_des);
        newsAutView = (TextView) findViewById(R.id.news_author);
        newsPubView = (TextView) findViewById(R.id.news_pub);*/

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) findViewById(R.id.news_recycyler_view);

        progressBar.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        recyclerView.setVisibility(View.VISIBLE);

        newsAdapter = new NewsAdapter();

        loadNewsData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.newsmenu, menu);
        return true;

    }


    public  void loadNewsData()
    {
        String searchString = "the-next-web";

        String apiKey = KeyClass.KEYAPI;


        new NewsAysnTask().execute(searchString,apiKey);


    }

    class NewsAysnTask extends AsyncTask<String,Void,JSONArray>{


        @Override
        protected JSONArray doInBackground(String... params) {
            String searchString = params[0];
            String apiString = params[1];
            JSONObject jsonObject = null;
            URL url = NetworkUtlis.makeUrl(searchString,"latest",apiString);
            String result = null;
            JSONArray jsonArray =null;

            try
            {
                    result = NetworkUtlis.getStaticResponseFromHttpUrl(url);
                    jsonArray =  JsonDataExtract.jsonParseNews(result);

            }
            catch(Exception e)
            {

                e.printStackTrace();
            }

          return jsonArray;

        }

        @Override
    protected void onPostExecute(JSONArray jsonArray) {

            ArrayList<String> newsDetails = new ArrayList<>();
            String[] title = new String[jsonArray.length()];
            String[] desc=new String[jsonArray.length()];
            String[] time=new String[jsonArray.length()];
            if(jsonArray!=null) {
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        progressBar.setVisibility(View.GONE);
                        JSONObject eachRowData = jsonArray.getJSONObject(i);

                        title[i] = eachRowData.getString("title");
                        desc[i]=eachRowData.getString("description");
                        time[i] = eachRowData.getString("publishedAt")


                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }

                newsAdapter.setTitle(title);
                newsAdapter.setDesc(desc);
                newsAdapter.setTime(time);
                recyclerView.setAdapter(newsAdapter);
            }
        }
    }
}