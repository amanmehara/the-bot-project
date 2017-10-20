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

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amanmehara.programming.android.R;
import com.amanmehara.programming.android.common.Language;
import com.amanmehara.programming.android.rest.GithubAPIClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.amanmehara.programming.android.common.Type.DIRECTORY;
import static com.amanmehara.programming.android.common.Type.FILE;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {

    private static final String TAG = LanguageAdapter.class.getSimpleName();
    private static final Map<String, byte[]> logos = new ConcurrentHashMap<>();
    private final String accessToken;
    private final Activity activity;
    private final JSONArray languages;
    private final SharedPreferences sharedPreferences;
    private final BiFunction<String, byte[], Consumer<JSONArray>> onClickCallback;

    public LanguageAdapter(
            String accessToken,
            Activity activity,
            JSONArray languages,
            SharedPreferences sharedPreferences,
            BiFunction<String, byte[], Consumer<JSONArray>> onClickCallback) {
        this.accessToken = accessToken;
        this.activity = activity;
        this.languages = languages;
        this.sharedPreferences = sharedPreferences;
        this.onClickCallback = onClickCallback;
    }

    @Override
    public LanguageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.language_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LanguageAdapter.ViewHolder viewHolder, int i) {
        try {

            JSONObject language = languages.getJSONObject(i);

            String languageName = mapLanguageName(language.getString("name"));
            viewHolder.languageNameView.setText(languageName);

            String url = language.getString("url");
            String response = sharedPreferences.getString(url, null);
            if (response != null) {
                getProgramsResponseCallback(url, true, languageName, viewHolder).accept(response);
            } else {
                new GithubAPIClient(activity, getProgramsResponseCallback(url, false, languageName, viewHolder))
                        .execute(withAccessToken(url));
            }

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return languages.length();
    }

    private GithubAPIClient.Consumer<String> getLogoResponseCallback(String url, boolean cacheHit, String languageName, ViewHolder viewHolder) {
        return response -> {
            try {
                if (!cacheHit) {
                    sharedPreferences.edit().putString(url, response).apply();
                }
                JSONObject icon = new JSONObject(response);
                byte[] imageBlob = Base64.decode(icon.getString("content"), Base64.DEFAULT);
                int imageBlobLength = imageBlob.length;
                Bitmap logo = BitmapFactory.decodeByteArray(imageBlob, 0, imageBlobLength);
                logos.put(languageName, imageBlob);
                viewHolder.languageImageView.setImageBitmap(logo);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
                if (cacheHit) {
                    sharedPreferences.edit().remove(url).apply();
                }
                viewHolder.languageImageView.setImageResource(R.drawable.ic_circle_logo);
            }
        };
    }

    private int getProgramCount(JSONArray programs) {
        int count = 0;
        for (int i = 0; i < programs.length(); i++) {
            try {
                if (programs.getJSONObject(i).get("type").equals(DIRECTORY.getValue())) {
                    count++;
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return count;
    }

    private GithubAPIClient.Consumer<String> getProgramsResponseCallback(String url, boolean cacheHit, String languageName, ViewHolder viewHolder) {
        return response -> {
            try {
                if (!cacheHit) {
                    sharedPreferences.edit().putString(url, response).apply();
                }
                JSONArray programs = new JSONArray(response);
                Integer programCount = getProgramCount(programs);
                viewHolder.languageCountView.setText(String.valueOf(programCount));
                setLanguageLogo(viewHolder, programs, languageName);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
                if (cacheHit) {
                    sharedPreferences.edit().remove(url).apply();
                }
            }
        };
    }

    private String mapLanguageName(String name) {
        for (Language language : Language.values()) {
            if (language.name().matches(name.toUpperCase())) {
                return language.getDisplay();
            }
        }
        return name;
    }

    private void setLanguageLogo(ViewHolder viewHolder, JSONArray programs, String languageName) {
        for (int i = 0; i < programs.length(); i++) {
            try {
                JSONObject jsonObject = programs.getJSONObject(i);
                if (jsonObject.getString("name").equals("icon.png") && jsonObject.getString("type").equals(FILE.getValue())) {
                    String url = jsonObject.getString("url");
                    String response = sharedPreferences.getString(url, null);
                    if (response != null) {
                        getLogoResponseCallback(url, true, languageName, viewHolder).accept(response);
                    } else {
                        new GithubAPIClient(activity, getLogoResponseCallback(url, false, languageName, viewHolder))
                                .execute(withAccessToken(url));
                    }
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
                viewHolder.languageImageView.setImageResource(R.drawable.ic_circle_logo);
            }
        }
    }

    private String withAccessToken(String url) {
        return url + "&access_token=" + accessToken;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView languageNameView;
        private ImageView languageImageView;
        private TextView languageCountView;

        private ViewHolder(View v) {
            super(v);
            languageNameView = (TextView) v.findViewById(R.id.language_name);
            languageImageView = (ImageView) v.findViewById(R.id.language_image);
            languageCountView = (TextView) v.findViewById(R.id.language_count);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int layoutPosition = getLayoutPosition();
            try {
                JSONObject language = languages.getJSONObject(layoutPosition);
                String languageName = mapLanguageName(language.getString("name"));
                String url = language.getString("url");
                byte[] logoBlob = logos.get(languageName);
                onClickCallback.apply(languageName, logoBlob).accept(new JSONArray(sharedPreferences.getString(url, null)));
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

    }

    public interface BiFunction<T, U, R> {
        R apply(T t, U u);
    }

    public interface Consumer<T> {
        void accept(T t);
    }

}