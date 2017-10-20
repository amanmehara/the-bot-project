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

package com.amanmehara.programming.android.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.amanmehara.programming.android.activities.enumeration.Activity;
import com.amanmehara.programming.android.adapters.DetailAdapter;
import com.amanmehara.programming.android.R;
import com.amanmehara.programming.android.rest.GithubAPIClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends BaseActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();
    private Bundle bundle;
    private SharedPreferences sharedPreferences;
    private String accessToken;
    private String languageName;
    private byte[] logoBlob;
    private String programJson;
    private String programsJson;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("Programming", MODE_PRIVATE);

        setContentView(R.layout.activity_detail);
        setActionBar(R.id.toolbar, true);
        recyclerView = setRecyclerView(R.id.files_recycler_view);

        bundle = getIntent().getExtras();
        accessToken = bundle.getString("accessToken");
        languageName = bundle.getString("languageName");
        logoBlob = bundle.getByteArray("logoBlob");
        programJson = bundle.getString("program");
        programsJson = bundle.getString("programs");

        try {
            if (isConnected()) {
                JSONObject program = new JSONObject(programJson);
                setProgramName(R.id.program_name, program);
                String url = program.getString("url");
                String response = sharedPreferences.getString(url, null);
                if (response != null) {
                    getProgramResponseCallback(url, true).accept(response);
                } else {
                    new GithubAPIClient(this, getProgramResponseCallback(url, false))
                            .execute(withAccessToken(url));

                }
            } else {
                setAdapter();
                Map<String, Serializable> extrasMap = new HashMap<>();
                extrasMap.put("enumeration.Activity", Activity.DETAIL);
                extrasMap.put("languageName", languageName);
                extrasMap.put("programs", programsJson);
                extrasMap.put("program", programJson);
                extrasMap.put("logoBlob", logoBlob);
                startActivity(ConnectionActivity.class, extrasMap);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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
                Map<String, Serializable> extrasMap = new HashMap<>();
                extrasMap.put("accessToken", accessToken);
                extrasMap.put("languageName", languageName);
                extrasMap.put("logoBlob", logoBlob);
                extrasMap.put("programs", programsJson);
                startActivity(ProgramActivity.class, extrasMap);
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private GithubAPIClient.Consumer<String> getProgramResponseCallback(String url, boolean cacheHit) {
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

    private void setAdapter() {
        setAdapter(new JSONArray());
    }

    private void setAdapter(JSONArray programContents) {
        DetailAdapter detailAdapter = new DetailAdapter(
                accessToken,
                this,
                languageName,
                programContents,
                sharedPreferences
        );
        recyclerView.setItemViewCacheSize(programContents.length());
        recyclerView.setAdapter(detailAdapter);
    }

    private void setProgramName(int id, JSONObject program) {
        TextView name = (TextView) findViewById(id);
        try {
            name.setText(program.getString("name"));
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private String withAccessToken(String url) {
        return url + "&access_token=" + accessToken;
    }

}