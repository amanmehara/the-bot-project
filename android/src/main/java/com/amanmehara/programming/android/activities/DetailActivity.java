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
import java.util.Objects;
import java.util.function.Consumer;

public class DetailActivity extends BaseActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();
    private Bundle bundle;
    private SharedPreferences sharedPreferences;
    private String accessToken;
    private String languageName;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getPreferences(MODE_PRIVATE);

        setContentView(R.layout.activity_detail);
        setActionBar(R.id.toolbar);
        recyclerView = setRecyclerView(R.id.files_recycler_view);

        bundle = getIntent().getExtras();
        accessToken = bundle.getString("accessToken");
        languageName = bundle.getString("languageName");

        try {
            if (isConnected()) {
                JSONObject program = new JSONObject(bundle.getString("program"));
                setProgramName(R.id.program_name, program);
                String url = program.getString("url");
                String response = sharedPreferences.getString(url, null);
                if (Objects.nonNull(response)) {
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
                extrasMap.put("programs", bundle.getString("programs"));
                extrasMap.put("program", bundle.getString("program"));
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
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
            Map<String, Serializable> extrasMap = new HashMap<>();
            extrasMap.put("languageName", languageName);
            extrasMap.put("programs", bundle.getString("programs"));
            startActivity(ProgramActivity.class, extrasMap);
        }
        return super.onOptionsItemSelected(item);
    }

    private Consumer<String> getProgramResponseCallback(String url, boolean cacheHit) {
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
