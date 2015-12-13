package com.amanmehara.programming.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.ExecutionException;


public class LanguageActivity extends Activity implements LanguageAdapter.ListClickListener {

    private Context context;

    private RecyclerView languageRecyclerView;
    private RecyclerView.Adapter languageAdapter;
    private RecyclerView.LayoutManager languageLayoutManager;

    private JSONArray languages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        context = this.getApplicationContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetworkInfo != null &&
                activeNetworkInfo.isConnectedOrConnecting();

        if (isConnected == true) {
            String jsonLanguage = null;
            WebServiceClient webServiceClient = new WebServiceClient();
            try {
                jsonLanguage = webServiceClient
                        .execute("http://programmingwebapp.azurewebsites.net/api/languages")
                        .get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            languages = null;

            try {
                languages = new JSONArray(jsonLanguage);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            languageRecyclerView = (RecyclerView) findViewById(R.id.language_recycler_view);
            languageRecyclerView.setHasFixedSize(true);

            languageLayoutManager = new LinearLayoutManager(this);
            languageRecyclerView.setLayoutManager(languageLayoutManager);

            languageAdapter = new LanguageAdapter(languages);
            ((LanguageAdapter) languageAdapter).setListClickListener(this);

            //languageAdapter = new LanguageAdapter(new String[]{hello, "Hello","Bye", "Aman", "Today", "Hi", String.valueOf(new WebServiceClient().execute("http://programmingwebapp.azurewebsites.net/api/languages"))});
            languageRecyclerView.setAdapter(languageAdapter);
        } else {
            Toast.makeText(context, "No Internet, No Languages!", Toast.LENGTH_LONG).show();

            languageRecyclerView = (RecyclerView) findViewById(R.id.language_recycler_view);
            languageRecyclerView.setHasFixedSize(true);

            languageLayoutManager = new LinearLayoutManager(this);
            languageRecyclerView.setLayoutManager(languageLayoutManager);

            languageAdapter = new LanguageAdapter(new JSONArray());
            languageRecyclerView.setAdapter(languageAdapter);
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
            intent.putExtra("language", languages.getString(position));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        startActivity(intent);

    }
}
