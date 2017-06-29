package com.amanmehara.programming.android.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.amanmehara.programming.android.adapters.ProgramAdapter;
import com.amanmehara.programming.android.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.amanmehara.programming.android.util.ActivityUtils.SET_ACTION_BAR;
import static com.amanmehara.programming.android.util.ActivityUtils.START_ACTIVITY;


public class ProgramActivity extends Activity {

    private static final String TAG = ProgramActivity.class.getSimpleName();

    private final Function<Context,BiConsumer<String,JSONObject>> ON_CLICK_CALLBACK
            = context -> (language, programs) -> {
        Map<String,Serializable> extrasMap = new HashMap<>();
        extrasMap.put("language",language);
        extrasMap.put("programs",programs.toString());
        START_ACTIVITY.apply(context,DetailActivity.class).accept(extrasMap);
    };

    private final BiFunction<Activity,String,BiConsumer<RecyclerView,JSONArray>> SET_ADAPTER
            = (activity,language) -> (recyclerView, jsonArray) -> {
        ProgramAdapter programsAdapter = new ProgramAdapter(activity,language,jsonArray,ON_CLICK_CALLBACK);
        recyclerView.setAdapter(programsAdapter);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programs);
        SET_ACTION_BAR.apply(this,R.id.my_toolbar).accept(true);

        Bundle bundle = getIntent().getExtras();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.programs_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager programsLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(programsLayoutManager);

        try {
            SET_ADAPTER.apply(this,bundle.getString("language")).accept(recyclerView,new JSONArray(bundle.getString("programs")));
        } catch (JSONException e) {
            Log.e(TAG,e.getMessage());
            SET_ADAPTER.apply(this,bundle.getString("language")).accept(recyclerView,new JSONArray());
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

}
