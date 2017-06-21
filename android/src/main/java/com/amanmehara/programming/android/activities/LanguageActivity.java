package com.amanmehara.programming.android.activities;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;
import com.amanmehara.programming.android.adapters.LanguageAdapter;
import com.amanmehara.programming.android.R;
import com.amanmehara.programming.android.common.AppActivity;
import com.amanmehara.programming.android.rest.Client;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.amanmehara.programming.android.util.ActivityUtils.START_ACTIVITY;


public class LanguageActivity extends Activity implements LanguageAdapter.ListClickListener {

    private Context context;

    private RecyclerView languageRecyclerView;
    private RecyclerView.Adapter languageAdapter;
    private RecyclerView.LayoutManager languageLayoutManager;

    private JSONArray languages;
    private String jsonLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setActionBar(myToolbar);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        languageRecyclerView = (RecyclerView) findViewById(R.id.language_recycler_view);
        languageRecyclerView.setHasFixedSize(true);

        languageLayoutManager = new LinearLayoutManager(this);
        languageRecyclerView.setLayoutManager(languageLayoutManager);

        context = this.getApplicationContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetworkInfo != null &&
                activeNetworkInfo.isConnectedOrConnecting();

        if (isConnected) {

            new Client(
                    LanguageActivity.this,
                    response -> {
                        jsonLanguage = response;

                        languages = null;

                        try {
                            languages = new JSONArray(jsonLanguage);
                        } catch (JSONException e) {
//                e.printStackTrace();
                        }

                        languageAdapter = new LanguageAdapter(languages);
                        ((LanguageAdapter) languageAdapter).setListClickListener(LanguageActivity.this);

                        languageRecyclerView.setAdapter(languageAdapter);
                    }).execute("http://programmingwebapp.azurewebsites.net/api/languages/count");

        } else {

            languageAdapter = new LanguageAdapter(new JSONArray());
            ((LanguageAdapter) languageAdapter).setListClickListener(this);

            languageRecyclerView.setAdapter(languageAdapter);

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void listItemClicked(View view, int position) {
        try {
            Map<String,Serializable> extrasMap = new HashMap<>();
            extrasMap.put("language",languages.getJSONObject(position).getString("LanguageName"));
            START_ACTIVITY
                    .apply(this,ProgramsActivity.class)
                    .accept(extrasMap);
        } catch (JSONException e) {
//            e.printStackTrace();
        }
    }
}