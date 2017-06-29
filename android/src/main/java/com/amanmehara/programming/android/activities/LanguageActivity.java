package com.amanmehara.programming.android.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.amanmehara.programming.android.adapters.LanguageAdapter;
import com.amanmehara.programming.android.R;
import com.amanmehara.programming.android.common.AppActivity;
import com.amanmehara.programming.android.common.Constants;
import com.amanmehara.programming.android.rest.RestClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static com.amanmehara.programming.android.util.ActivityUtils.IS_CONNECTED;
import static com.amanmehara.programming.android.util.ActivityUtils.SET_ACTION_BAR;
import static com.amanmehara.programming.android.util.ActivityUtils.START_ACTIVITY;

public class LanguageActivity extends Activity {

    private static final String TAG = LanguageActivity.class.getSimpleName();
    private static final String LANGUAGES_PATH = "content?ref=master";

    private static final Function<Context,BiConsumer<String,JSONArray>> ON_CLICK_CALLBACK
            = context -> (language, programs) -> {
        Map<String,Serializable> extrasMap = new HashMap<>();
        extrasMap.put("language",language);
        extrasMap.put("programs",programs.toString());
        START_ACTIVITY.apply(context,ProgramActivity.class).accept(extrasMap);
    };

    private static final Function<Activity,BiConsumer<RecyclerView,JSONArray>> SET_ADAPTER
            = activity -> (recyclerView, jsonArray) -> {
        LanguageAdapter languageAdapter = new LanguageAdapter(activity,jsonArray,ON_CLICK_CALLBACK);
        recyclerView.setAdapter(languageAdapter);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        SET_ACTION_BAR.apply(this,R.id.my_toolbar).accept(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.language_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager languageLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(languageLayoutManager);

        if (IS_CONNECTED.test(getApplicationContext())) {
            new RestClient(
                    LanguageActivity.this,
                    response -> {
                        try {
                            SET_ADAPTER.apply(this).accept(recyclerView,new JSONArray(response));
                        } catch (JSONException e) {
                            Log.e(TAG,e.getMessage());
                            SET_ADAPTER.apply(this).accept(recyclerView,new JSONArray());
                        }
                    }).execute(Constants.ENDPOINT + LANGUAGES_PATH);
        } else {
            SET_ADAPTER.apply(this).accept(recyclerView,new JSONArray());
            Map<String,Serializable> extrasMap = new HashMap<>();
            extrasMap.put("activityInfo",AppActivity.LANGUAGE);
            START_ACTIVITY
                    .apply(this,NoConnectionActivity.class)
                    .accept(extrasMap);
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

}