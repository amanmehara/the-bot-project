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


public class ProgramsActivity extends AppCompatActivity implements ProgramsAdapter.ListClickListener {

    private Context context;
    private Bundle bundle;

    private RecyclerView programsRecyclerView;
    private RecyclerView.Adapter programsAdapter;
    private RecyclerView.LayoutManager programsLayoutManager;

    private JSONArray programs;
    private String jsonPrograms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programs);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bundle = getIntent().getExtras();
        String language = bundle.getString("language");

        programsRecyclerView = (RecyclerView) findViewById(R.id.programs_recycler_view);
        programsRecyclerView.setHasFixedSize(true);

        programsLayoutManager = new LinearLayoutManager(this);
        programsRecyclerView.setLayoutManager(programsLayoutManager);

//        String jsonPrograms = null;

        context = this.getApplicationContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetworkInfo != null &&
                activeNetworkInfo.isConnectedOrConnecting();

        if (isConnected) {

            new WebServiceClient(ProgramsActivity.this,
                    new WebServiceClient.AsyncResponse() {
                        @Override
                        public void getResponse(String response) {
                            jsonPrograms = response;

                            programs = null;

                            try {
                                programs = new JSONArray(jsonPrograms);
                            } catch (JSONException e) {
//                e.printStackTrace();
                            }

                            programsAdapter = new ProgramsAdapter(programs);
                            ((ProgramsAdapter) programsAdapter).setListClickListener(ProgramsActivity.this);

                            programsRecyclerView.setAdapter(programsAdapter);
                        }
                    }).execute("http://programmingwebapp.azurewebsites.net/api/programs/language/"
                    + language);

        } else {
            programsAdapter = new ProgramsAdapter(new JSONArray());
            ((ProgramsAdapter) programsAdapter).setListClickListener(this);

            programsRecyclerView.setAdapter(programsAdapter);

            Intent intent = new Intent(ProgramsActivity.this, NoConnectionActivity.class);
            intent.putExtra("activityInfo", ActivitiesAsEnum.PROGRAMS_ACTIVITY);
            intent.putExtra("language", bundle.getString("language"));
            startActivity(intent);
        }

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
//            e.printStackTrace();
        }

        startActivity(intent);
    }
}
