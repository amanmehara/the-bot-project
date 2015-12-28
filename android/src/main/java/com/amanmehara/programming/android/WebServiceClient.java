package com.amanmehara.programming.android;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebServiceClient extends AsyncTask<String, Integer, String> {

    public AsyncResponse asyncResponse;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;
    private Activity mActivity;
    private ProgressBar progressBar;
    private String response;

    WebServiceClient(Activity activity, AsyncResponse asyncResponse) {
        mActivity = activity;
        this.asyncResponse = asyncResponse;
        progressBar = (ProgressBar) mActivity.findViewById(R.id.my_progressbar);
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

            inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            bufferedReader = new BufferedReader(inputStreamReader);

            response = this.bufferedReaderToString(bufferedReader);

            bufferedReader.close();
            inputStreamReader.close();

        } catch (IOException e) {
//            e.printStackTrace();
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

        asyncResponse.getResponse(response);

//        mActivity.getResultPost
    }

    private String bufferedReaderToString(BufferedReader bufferedReader) {

        StringBuilder stringBuilder = new StringBuilder();
        String line;

        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(System.getProperty("line.separator"));
            }
        } catch (IOException e) {
//            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    public interface AsyncResponse {
        void getResponse(String response);
    }
}
