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
import java.util.function.BiConsumer;
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

        sharedPreferences = getPreferences(MODE_PRIVATE);

        setContentView(R.layout.activity_language);
        setActionBar(R.id.toolbar);
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
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    private BiConsumer<String, JSONArray> getOnClickCallback() {
        return (languageName, programs) -> {
            Map<String, Serializable> extrasMap = new HashMap<>();
            extrasMap.put("accessToken", accessToken);
            extrasMap.put("languageName", languageName);
            extrasMap.put("programs", programs.toString());
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