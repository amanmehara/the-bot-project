package com.amanmehara.programming.android;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebServiceClient extends AsyncTask<String, String, String> {

    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;


    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        String responseEntity = null;
        try {
            URL url = new URL(params[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            bufferedReader = new BufferedReader(inputStreamReader);

            responseEntity = this.bufferedReaderToString(bufferedReader);


            bufferedReader.close();
            inputStreamReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseEntity;

    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
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
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }
}
