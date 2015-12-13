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
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DetailActivity extends Activity {

    private Context context;

    private RecyclerView detailRecyclerView;
    private RecyclerView.Adapter detailAdapter;
    private RecyclerView.LayoutManager detailLayoutManager;

    private Bundle bundle;
    private JSONObject programDetails;
    private JSONArray files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        bundle = getIntent().getExtras();

        getActionBar().setDisplayHomeAsUpEnabled(true);

        programDetails = null;
        try {
            programDetails = new JSONObject(bundle.getString("programDetails"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        files = null;
        try {
            files = programDetails.getJSONArray("Files");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        detailRecyclerView = (RecyclerView) findViewById(R.id.files_recycler_view);
        detailRecyclerView.setHasFixedSize(true);

        detailLayoutManager = new LinearLayoutManager(this);
        detailRecyclerView.setLayoutManager(detailLayoutManager);

        detailAdapter = new DetailAdapter(files, bundle.getString("language"));
        detailRecyclerView.setAdapter(detailAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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
                Intent intent = new Intent(this, ProgramsActivity.class);
                intent.putExtra("language", bundle.getString("language"));
                startActivity(intent);
            } else {
                Toast.makeText(context, "No Internet, No Programs!", Toast.LENGTH_LONG).show();
            }

        }

        return super.onOptionsItemSelected(item);
    }
}
