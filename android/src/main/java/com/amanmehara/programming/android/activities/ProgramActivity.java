package com.amanmehara.programming.android.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.amanmehara.programming.android.adapters.ProgramsAdapter;
import com.amanmehara.programming.android.R;
import com.amanmehara.programming.android.common.AppActivity;
import com.amanmehara.programming.android.rest.RestClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.amanmehara.programming.android.util.ActivityUtils.IS_CONNECTED;
import static com.amanmehara.programming.android.util.ActivityUtils.SET_ACTION_BAR;
import static com.amanmehara.programming.android.util.ActivityUtils.START_ACTIVITY;


public class ProgramActivity extends Activity implements ProgramsAdapter.ListClickListener {

    private Bundle bundle;

    private RecyclerView programsRecyclerView;
    private RecyclerView.Adapter programsAdapter;

    private JSONArray programs;
    private String jsonPrograms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programs);

        SET_ACTION_BAR.apply(this,R.id.my_toolbar).accept(true);

        bundle = getIntent().getExtras();
        String language = bundle.getString("language");

        programsRecyclerView = (RecyclerView) findViewById(R.id.programs_recycler_view);
        programsRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager programsLayoutManager = new LinearLayoutManager(this);
        programsRecyclerView.setLayoutManager(programsLayoutManager);

//        String jsonPrograms = null;

        if (IS_CONNECTED.test(getApplicationContext())) {

            new RestClient(ProgramActivity.this,
                    response -> {
                        jsonPrograms = response;

                        programs = null;

                        try {
                            programs = new JSONArray(jsonPrograms);
                        } catch (JSONException e) {
//                e.printStackTrace();
                        }

                        programsAdapter = new ProgramsAdapter(programs);
                        ((ProgramsAdapter) programsAdapter).setListClickListener(ProgramActivity.this);

                        programsRecyclerView.setAdapter(programsAdapter);
                    }).execute("http://programmingwebapp.azurewebsites.net/api/programs/language/"
                    + language);

        } else {
            programsAdapter = new ProgramsAdapter(new JSONArray());
            ((ProgramsAdapter) programsAdapter).setListClickListener(this);

            programsRecyclerView.setAdapter(programsAdapter);

            Map<String,Serializable> extrasMap = new HashMap<>();
            extrasMap.put("activityInfo", AppActivity.PROGRAM);
            extrasMap.put("language", bundle.getString("language"));
            START_ACTIVITY
                    .apply(this,NoConnectionActivity.class)
                    .accept(extrasMap);
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

        try {

            Map<String,Serializable> extrasMap = new HashMap<>();
            extrasMap.put("programDetails",programs.getJSONObject(position).toString());
            extrasMap.put("language",bundle.getString("language"));
            START_ACTIVITY
                    .apply(this,DetailActivity.class)
                    .accept(extrasMap);

        } catch (JSONException e) {
//            e.printStackTrace();
        }
    }
}
