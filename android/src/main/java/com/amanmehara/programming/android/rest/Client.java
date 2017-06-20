package com.amanmehara.programming.android.rest;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import com.amanmehara.programming.android.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by @amanmehara on 20-06-2017.
 */
public class Client extends AsyncTask<String,Integer,String> {

    private static final String TAG = Client.class.getSimpleName();
    private final Consumer<String> callback;
    private final ProgressBar progressBar;
    private String response;

    public Client(
            Activity activity,
            Consumer<String> callback
    ) {
        this.callback = callback;
        this.progressBar = (ProgressBar) activity.findViewById(R.id.my_progressbar);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            try (
                    InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader)
            ) {
                response = bufferedReader.lines()
                        .collect(Collectors.joining(System.getProperty("line.separator")));
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return response;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressBar.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        progressBar.setVisibility(View.GONE);
        callback.accept(response);
    }
}
