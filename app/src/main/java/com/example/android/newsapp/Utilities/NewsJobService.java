package com.example.android.newsapp.Utilities;

import android.os.AsyncTask;
import android.widget.Toast;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import org.json.JSONException;

/**
 * Created by ishpr on 7/28/2017.
 */

public class NewsJobService extends JobService{
    AsyncTask newsBackgroundTask;
    @Override
    public boolean onStartJob(final JobParameters params) {

        newsBackgroundTask = new AsyncTask() {
            @Override
            protected void onPreExecute() {
                //Toast.makeText(NewsJobService.this, "News refreshed. Please click refresh button to see the latest news", Toast.LENGTH_SHORT).show();
                super.onPreExecute();
            }

            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    NewsDataRefresh.refreshArticles(NewsJobService.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(params, false);
                super.onPostExecute(o);

            }
        };


        newsBackgroundTask.execute();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {


        if (newsBackgroundTask != null) newsBackgroundTask.cancel(false);

        return true;

    }
}
