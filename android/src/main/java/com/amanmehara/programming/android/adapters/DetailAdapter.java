/*

 Copyright (C) 2015 - 2017 Aman Mehara

 This file is part of Programming!

 Programming! is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Programming! is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Programming!. If not, see <http://www.gnu.org/licenses/>.

 */

package com.amanmehara.programming.android.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.amanmehara.programming.android.R;
import com.amanmehara.programming.android.common.Language;
import com.amanmehara.programming.android.rest.GithubAPIClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.amanmehara.programming.android.common.Type.FILE;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {

    private static final String TAG = DetailAdapter.class.getSimpleName();
    private final String accessToken;
    private final Activity activity;
    private final String languageName;
    private final JSONArray programContents;
    private final SharedPreferences sharedPreferences;

    public DetailAdapter(
            String accessToken,
            Activity activity,
            String languageName,
            JSONArray programContents,
            SharedPreferences sharedPreferences
    ) {
        this.accessToken = accessToken;
        this.activity = activity;
        this.languageName = languageName;
        this.programContents = programContents;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public DetailAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.files_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        WebSettings webSettings = viewHolder.fileContentView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        try {

            JSONObject jsonObject = programContents.getJSONObject(i);

            viewHolder.fileNameView.setText(jsonObject.getString("name"));

            String url = jsonObject.getString("url");
            String response = sharedPreferences.getString(url, null);
            if (response != null) {
                getContentResponseCallback(url, true, viewHolder).accept(response);
            } else {
                new GithubAPIClient(activity, getContentResponseCallback(url, false, viewHolder))
                        .execute(withAccessToken(url));
            }

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return programContents.length();
    }

    private String decodeContent(String content) {
        return new String(Base64.decode(content, Base64.DEFAULT));
    }

    private String generateHtml(String content) {
        return "<html>"
                + "<link href='file:///android_asset/prism.css' rel='stylesheet'/>"
                + "<script src='file:///android_asset/prism.js'></script>"
                + "<body style='margin:0px; padding:0px;'>"
                + "<pre class='line-numbers'><code class='language-"
                + reverseMapLanguageName(languageName)
                + "'>"
                + Html.escapeHtml(content)
                + "</code></pre>"
                + "</body>"
                + "</html>";
    }

    private GithubAPIClient.Consumer<String> getContentResponseCallback(String url, boolean cacheHit, ViewHolder viewHolder) {
        return response -> {
            try {
                if (!cacheHit) {
                    sharedPreferences.edit().putString(url, response).apply();
                }
                JSONObject resolvedContent = new JSONObject(response);
                if (resolvedContent.getString("type").equals(FILE.getValue())) {
                    String html = generateHtml(decodeContent(resolvedContent.getString("content")));
                    viewHolder.fileContentView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "UTF-8", null);
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
                if (cacheHit) {
                    sharedPreferences.edit().remove(url).apply();
                }
            }
        };
    }

    private String reverseMapLanguageName(String name) {
        for (Language language : Language.values()) {
            if (language.getDisplay().matches(name)) {
                return language.name().toLowerCase();
            }
        }
        return name;
    }

    private String withAccessToken(String url) {
        return url + "&access_token=" + accessToken;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView fileNameView;
        private WebView fileContentView;

        private ViewHolder(View v) {
            super(v);
            fileNameView = (TextView) v.findViewById(R.id.file_name);
            fileContentView = (WebView) v.findViewById(R.id.file_content);
        }
    }

}