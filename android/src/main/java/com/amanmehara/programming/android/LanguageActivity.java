package com.amanmehara.programming.android;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LanguageActivity extends AppCompatActivity implements LanguageAdapter.ListClickListener {

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
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

            new WebServiceClient(
                    LanguageActivity.this,
                    new WebServiceClient.AsyncResponse() {
                        @Override
                        public void getResponse(String response) {
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
                        }
                    }).execute("http://programmingwebapp.azurewebsites.net/api/languages/count");

        } else {

            languageAdapter = new LanguageAdapter(new JSONArray());
            ((LanguageAdapter) languageAdapter).setListClickListener(this);

            languageRecyclerView.setAdapter(languageAdapter);

            Intent intent = new Intent(this, NoConnectionActivity.class);
            intent.putExtra("activityInfo", ActivitiesAsEnum.LANGUAGE_ACTIVITY);
            startActivity(intent);
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

        Intent intent = new Intent(LanguageActivity.this, ProgramsActivity.class);

        try {
            JSONObject jsonObject = languages.getJSONObject(position);
            intent.putExtra("language", jsonObject.getString("LanguageName"));
        } catch (JSONException e) {
//            e.printStackTrace();
        }

        startActivity(intent);
    }
}