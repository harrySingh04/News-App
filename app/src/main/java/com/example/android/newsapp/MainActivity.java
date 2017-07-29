package com.example.android.newsapp;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.newsapp.Data.NewsDbHelper;
import com.example.android.newsapp.Data.NewsItemContract;
import com.example.android.newsapp.Utilities.JsonDataExtract;
import com.example.android.newsapp.Utilities.KeyClass;
import com.example.android.newsapp.Utilities.NetworkUtlis;
import com.example.android.newsapp.Utilities.NewsItemModel;
import com.example.android.newsapp.Utilities.NewsJobSchedule;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.net.URL;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.author;
import static android.R.attr.data;
import static android.R.attr.description;
import static android.R.attr.name;
import static android.os.Build.VERSION_CODES.N;
import static com.example.android.newsapp.Utilities.JsonDataExtract.jsonParseNews;
// Implementing Loader CallBacks to implement Async Task Loader
public class MainActivity extends AppCompatActivity implements NewsAdapter.NewsListData ,
        LoaderManager.LoaderCallbacks<JSONArray>{

        //private TextView newsTextView;
        //private TextView newsDesView;
        //private TextView newsAutView;
        //private TextView newsPubView;
        private ProgressBar progressBar;

        private NewsAdapter newsAdapter;
        private RecyclerView recyclerView;
    // Declaring constant to store the value for the Loader Id

    private static final int LOADER_NEWS_ID=18;
    //Constant to save the value for the url into the bundle to pass to the Aysnc Loader
    private final String NEWS_APP_QUERY_SEARCH="query";

    //Constant to save the apikey in the bundle
    private final String NEWS_API_KEY="apikey";

    //Varibale for the SQL lite Database
    SQLiteDatabase db;

    //Cursor for the database
    Cursor cursor;


    //Shared Preferences Variable to check if app is installed first time or not.
    SharedPreferences ref = null;

       List<NewsItemModel> dataArray = new ArrayList<NewsItemModel>() ;
         String[] title;
         String[] desc;
         String[] time;

        Toast mToast;

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

        NewsDbHelper dbHelper = new NewsDbHelper(this);
        db = dbHelper.getWritableDatabase();

        //Check if app is installed before or it is installed first time
        ref = getSharedPreferences("com.example.android.newsapp", MODE_PRIVATE);

        if (ref.getBoolean("firstrun", true)) {

            ref.edit().putBoolean("firstrun", false).commit();
           // Toast.makeText(MainActivity.this,"first time",Toast.LENGTH_SHORT).show();
            loadNewsData();
            //Initializing  the loader with LOADER_NEWS_ID as the ID, null for the bundle, and this for the context
            getSupportLoaderManager().initLoader(LOADER_NEWS_ID, null, this);
        }
        else {

            //Variable for the Db helper class NewsDbHelper

          //  Toast.makeText(MainActivity.this,"Hello i retrieve from database",Toast.LENGTH_SHORT).show();



           /* //Getting all the News from the database.
            cursor  = getAllNews();

            if(cursor!=null)
                progressBar.setVisibility(View.GONE);
            cursor.moveToFirst();
            do {

                dataArray.add(new NewsItemModel(cursor.getString(1),cursor.getString(2),
                        cursor.getString(3),cursor.getString(4),cursor.getString(5)));

            }while(cursor.moveToNext());
            cursor.close();
            newsAdapter.swapCursor(getAllNews());
            newsAdapter.setDataArray(dataArray);
            recyclerView.setAdapter(newsAdapter);*/

           retrieveDb(db);


        }

        NewsJobSchedule.scheduleRefresh(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.newsmenu, menu);


        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button_brown, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.refresh:
                retrieveDb(db);
                 newsAdapter.swapCursor(getAllNews());


                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }
    public  void loadNewsData()
    {
        String searchString = "the-next-web";

        String apiKey = KeyClass.KEYAPI;
        //Making the url to retrievt the data for the news app;
        URL url = NetworkUtlis.makeUrl(searchString,"latest",apiKey);

        //Creating a bundle to put the value of the url string into the bundle to be retrieved by the
        // Async Task loader during background
        Bundle bundle  = new Bundle();
        bundle.putString(NEWS_APP_QUERY_SEARCH,url.toString());
      //  bundle.putString(NEWS_API_KEY,apiKey);

        //Loader Manager for the Async Task loader
        LoaderManager loaderManager = getSupportLoaderManager();

        //Retrieving the loader for the loader id defined for this app
        Loader<JSONArray> gitHubLoader = loaderManager.getLoader(LOADER_NEWS_ID);
        //if no loader found then start the loader or restart the loader.
        if(gitHubLoader==null)
        {
            loaderManager.initLoader(LOADER_NEWS_ID,bundle,this);

        }
        else
        {
            loaderManager.restartLoader(LOADER_NEWS_ID,bundle,this);
        }


    }
//Overriding the Loader method onCreateLoader to start the loader for the data retrieval.
    @Override
    public Loader<JSONArray> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<JSONArray>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(args==null)
                    return;
                forceLoad();

            }

            @Override
            public JSONArray loadInBackground() {
                // Retrieving the url string from the bundle
                String searchUrlString = args.getString(NEWS_APP_QUERY_SEARCH);
                //If string is empty then return and dont show anything
                if(searchUrlString==null)
                    return null;
                String result = null;
                JSONArray jsonArray =null;

                try
                {
                    //Convert the string to url
                    URL searchUrl = new URL(searchUrlString);
                    //Calling the Network utlis method the get the http url response for the url
                    result = NetworkUtlis.getStaticResponseFromHttpUrl(searchUrl);
                    //Extracting the data for the url
                    jsonArray =  JsonDataExtract.jsonParseNews(result);

                }
                catch(Exception e)
                {

                    e.printStackTrace();
                    return null;
                }

                return jsonArray;

            }
        };
    }

    @Override
    public void onLoadFinished(Loader<JSONArray> loader, JSONArray jsonArray) {
        //If data is retrieved from the url then proceed
       if(jsonArray!=null) {

           ArrayList<String> newsDetails = new ArrayList<>();


           if (jsonArray != null) {
               for (int i = 0; i < jsonArray.length(); i++) {

                   try {
                       //Invisible the progress bar as data has been retrieved and will be shown
                       progressBar.setVisibility(View.GONE);
                       JSONObject eachRowData = jsonArray.getJSONObject(i);

                      // dataArray.add(new NewsItemModel(eachRowData.getString("title"), eachRowData.getString("description"),
                            //   eachRowData.getString("publishedAt"), eachRowData.getString("url"), eachRowData.getString("urlToImage")));
                  //Adding data into the db
                       NewsDbHelper.addNewsEntryDb(db,eachRowData.getString("title"), eachRowData.getString("description"),
                               eachRowData.getString("publishedAt"), eachRowData.getString("url"), eachRowData.getString("urlToImage"));
                   } catch (Exception e) {
                       e.printStackTrace();

                   }
               }

               // Calling the method that is retrieving the data from the database and storing
               // in the recycler view and display
               retrieveDb(db);

           }
       }
    }

    @Override
    public void onLoaderReset(Loader<JSONArray> loader) {

    }

    @Override
    public void onClickItemListener(int clickedIndex) {


        Uri webpage = Uri.parse(dataArray.get(clickedIndex).getUrlTo());

        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    /*public long addNewsEntryDb(String title,String desc,String publishedAt,String urlTo,String urlImageTo)
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


    }*/

    // Cursor containing the list of guests
    private Cursor getAllNews() {
        return db.query(
                NewsItemContract.NewsItemEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
    //Method to retrieve data from the database and storing in the Recycler view
    public void retrieveDb(SQLiteDatabase db)
    {


        cursor = getAllNews();
        newsAdapter = new NewsAdapter(this,cursor);
        //newsAdapter.swapCursor(cursor);
        if(cursor!=null)
            progressBar.setVisibility(View.GONE);
        if(cursor.moveToFirst())
        {
            do {
                dataArray.add(new NewsItemModel(cursor.getString(cursor.getColumnIndex(NewsItemContract.NewsItemEntry.COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(NewsItemContract.NewsItemEntry.COLUMN_DESC)),
                        cursor.getString(cursor.getColumnIndex(NewsItemContract.NewsItemEntry.COLUMN_PUBLISHED_AT)),
                        cursor.getString(cursor.getColumnIndex(NewsItemContract.NewsItemEntry.COLUMN__URL_TO)),
                        cursor.getString(cursor.getColumnIndex(NewsItemContract.NewsItemEntry.COLUMN_URL_TO_IMAGE))));

            }while(cursor.moveToNext());

        }

        newsAdapter.setDataArray(dataArray);

        recyclerView.setAdapter(newsAdapter);

    }


}