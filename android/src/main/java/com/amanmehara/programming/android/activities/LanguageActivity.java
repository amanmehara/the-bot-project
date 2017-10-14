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
 along with Foobar. If not, see <http://www.gnu.org/licenses/>.

 */

package com.amanmehara.programming.android.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.amanmehara.programming.android.activities.enumeration.Activity;
import com.amanmehara.programming.android.adapters.LanguageAdapter;
import com.amanmehara.programming.android.R;
import com.amanmehara.programming.android.common.Constants;
import com.amanmehara.programming.android.common.Type;
import com.amanmehara.programming.android.rest.GithubAPIClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class LanguageActivity extends BaseActivity {

    private static final String TAG = LanguageActivity.class.getSimpleName();
    private static final String LANGUAGES_PATH = "contents?ref=master";
    private SharedPreferences sharedPreferences;
    private String accessToken;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("Programming", MODE_PRIVATE);

        setContentView(R.layout.activity_language);
        setActionBar(R.id.toolbar, true);
        recyclerView = setRecyclerView(R.id.language_recycler_view);

        Bundle bundle = getIntent().getExtras();
        accessToken = bundle.getString("accessToken");
        String url = Constants.ENDPOINT + LANGUAGES_PATH;

        if (isConnected()) {
            String response = sharedPreferences.getString(url, null);
            if (Objects.nonNull(response)) {
                getLanguagesResponseCallback(url, true).accept(response);
            } else {
                new GithubAPIClient(this, getLanguagesResponseCallback(url, false))
                        .execute(withAccessToken(url));
            }
        } else {
            setAdapter();
            Map<String, Serializable> extrasMap = new HashMap<>();
            extrasMap.put("enumeration.Activity", Activity.LANGUAGE);
            extrasMap.put("accessToken", accessToken);
            startActivity(ConnectionActivity.class, extrasMap);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_language, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_rate: {
                rate();
                return true;
            }
            case android.R.id.home: {
                startActivity(MainActivity.class);
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private JSONArray filterLanguages(JSONArray languages) {
        JSONArray filtered = new JSONArray();
        for (int i = 0; i < languages.length(); i++) {
            JSONObject language = languages.optJSONObject(i);
            if (Objects.nonNull(language)) {
                String type = language.optString("type");
                if (type.equals(Type.DIRECTORY.getValue())) {
                    filtered.put(language);
                }
            }
        }
        return filtered;
    }

    private Consumer<String> getLanguagesResponseCallback(String url, boolean cacheHit) {
        return response -> {
            try {
                if (!cacheHit) {
                    sharedPreferences.edit().putString(url, response).apply();
                }
                setAdapter(new JSONArray(response));
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
                if (cacheHit) {
                    sharedPreferences.edit().remove(url).apply();
                }
                setAdapter();
            }
        };
    }

    private BiFunction<String, byte[], Consumer<JSONArray>> getOnClickCallback() {
        return (languageName, logoBlob) -> (programs) -> {
            Map<String, Serializable> extrasMap = new HashMap<>();
            extrasMap.put("accessToken", accessToken);
            extrasMap.put("languageName", languageName);
            extrasMap.put("programs", programs.toString());
            extrasMap.put("logoBlob", logoBlob);
            startActivity(ProgramActivity.class, extrasMap);
        };
    }

    private void setAdapter() {
        setAdapter(new JSONArray());
    }

    private void setAdapter(JSONArray languages) {
        LanguageAdapter languageAdapter = new LanguageAdapter(
                accessToken,
                this,
                filterLanguages(languages),
                sharedPreferences,
                getOnClickCallback()
        );
        recyclerView.setAdapter(languageAdapter);
    }

    private String withAccessToken(String url) {
        return url + "&access_token=" + accessToken;
    }

}