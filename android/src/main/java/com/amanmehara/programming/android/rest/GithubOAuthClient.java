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
import java.io.OutputStream;
import java.net.URL;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by @amanmehara on 03-09-2017.
 */

public class GithubOAuthClient extends AsyncTask<String,Integer,String> {

    private static final String TAG = GithubOAuthClient.class.getSimpleName();
    private static final String URL = "https://github.com/login/oauth/access_token";
    private Consumer<String> callback;
    private final ProgressBar progressBar;
    private String response;

    public GithubOAuthClient(Activity activity, Consumer<String> callback) {
        this.callback = callback;
        this.progressBar = (ProgressBar) activity.findViewById(R.id.progressbar);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(URL);
            String urlEncodedFormData = params[0];
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpsURLConnection.setUseCaches(false);
            httpsURLConnection.setDoOutput(true);
            OutputStream outputStream = httpsURLConnection.getOutputStream();
            outputStream.write(urlEncodedFormData.getBytes());
            outputStream.flush();
            outputStream.close();
            try (
                    InputStreamReader inputStreamReader = new InputStreamReader(httpsURLConnection.getInputStream());
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
