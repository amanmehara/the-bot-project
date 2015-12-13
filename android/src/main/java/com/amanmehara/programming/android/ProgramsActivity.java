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
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


public class ProgramsActivity extends Activity implements ProgramsAdapter.ListClickListener {

    private Context context;
    private Bundle bundle;

    private RecyclerView programsRecyclerView;
    private RecyclerView.Adapter programsAdapter;
    private RecyclerView.LayoutManager programsLayoutManager;

    private JSONArray programs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programs);

        bundle = getIntent().getExtras();
        String language = bundle.getString("language");

        getActionBar().setDisplayHomeAsUpEnabled(true);

        String jsonPrograms = null;
        WebServiceClient webServiceClient = new WebServiceClient();

        try {
            jsonPrograms = webServiceClient
                    .execute("http://programmingwebapp.azurewebsites.net/api/programs/language/" + language)
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        programs = null;

        try {
            programs = new JSONArray(jsonPrograms);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        programsRecyclerView = (RecyclerView) findViewById(R.id.programs_recycler_view);
        programsRecyclerView.setHasFixedSize(true);

        programsLayoutManager = new LinearLayoutManager(this);
        programsRecyclerView.setLayoutManager(programsLayoutManager);

        programsAdapter = new ProgramsAdapter(programs);
        ((ProgramsAdapter) programsAdapter).setListClickListener(this);

        programsRecyclerView.setAdapter(programsAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_programs, menu);
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

        if (id == android.R.id.home) {

            context = this.getApplicationContext();
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            boolean isConnected = activeNetworkInfo != null &&
                    activeNetworkInfo.isConnectedOrConnecting();

            if (isConnected) {
                Intent intent = new Intent(this, LanguageActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(context, "No Internet, No Languages!", Toast.LENGTH_LONG).show();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void listItemClicked(View view, int position) {
        Intent intent = new Intent(ProgramsActivity.this, DetailActivity.class);

        try {
            JSONObject jsonObject = programs.getJSONObject(position);
            intent.putExtra("programDetails", jsonObject.toString());
            intent.putExtra("language", bundle.getString("language"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        startActivity(intent);
    }
}
