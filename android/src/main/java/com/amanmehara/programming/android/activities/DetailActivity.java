package com.amanmehara.programming.android.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.amanmehara.programming.android.adapters.DetailAdapter;
import com.amanmehara.programming.android.R;
import com.amanmehara.programming.android.common.AppActivity;
import com.amanmehara.programming.android.rest.RestClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static com.amanmehara.programming.android.util.ActivityUtils.IS_CONNECTED;
import static com.amanmehara.programming.android.util.ActivityUtils.SET_ACTION_BAR;
import static com.amanmehara.programming.android.util.ActivityUtils.START_ACTIVITY;

public class DetailActivity extends Activity {

    private static final String TAG = DetailActivity.class.getSimpleName();
    private Bundle bundle;

    private static final Function<Activity,BiConsumer<RecyclerView,JSONArray>> SET_ADAPTER
            = activity -> (recyclerView, jsonArray) -> {
        DetailAdapter detailAdapter = new DetailAdapter(activity,jsonArray);
        recyclerView.setAdapter(detailAdapter);
    };

    private static final Function<String,Optional<JSONObject>> OBTAIN_JSONOBJECT = s -> {
        try {
            return Optional.of(new JSONObject(s));
        } catch (JSONException e) {
            Log.e(TAG,e.getMessage());
            return Optional.empty();
        }
    };

    private static final Function<Activity,BiConsumer<Integer,JSONObject>> SET_PROGRAM_NAME
            = activity -> (id, program) -> {
        TextView name = (TextView) activity.findViewById(id);
        try {
            name.setText(program.getString("name"));
        } catch (JSONException e) {
            Log.e(TAG,e.getMessage());
        }
    };

    private static final Function<JSONObject,Optional<String>> OBTAIN_PROGRAM_URL = program -> {
        try {
            return Optional.ofNullable(program.getString("url"));
        } catch (JSONException e) {
            Log.e(TAG,e.getMessage());
            return Optional.empty();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        SET_ACTION_BAR.apply(this,R.id.my_toolbar).accept(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.files_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager detailLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(detailLayoutManager);

        bundle = getIntent().getExtras();

        Optional<JSONObject> programOptional = OBTAIN_JSONOBJECT.apply(bundle.getString("program"));

        if(programOptional.isPresent()) {
            Optional<String> programUrlOptional = OBTAIN_PROGRAM_URL.apply(programOptional.get());
            if(IS_CONNECTED.test(getApplicationContext()) && programUrlOptional.isPresent()) {
                new RestClient(
                        DetailActivity.this,
                        response -> {
                            try {
                                SET_ADAPTER.apply(this).accept(recyclerView,new JSONArray(response));
                            } catch (JSONException e) {
                                Log.e(TAG,e.getMessage());
                                SET_ADAPTER.apply(this).accept(recyclerView,new JSONArray());
                            }
                        }
                ).execute(programUrlOptional.get());
            } else {
                SET_ADAPTER.apply(this).accept(recyclerView,new JSONArray());
                Map<String,Serializable> extrasMap = new HashMap<>();
                extrasMap.put("activityInfo", AppActivity.DETAIL);
                extrasMap.put("language",bundle.getString("language"));
                extrasMap.put("programs",bundle.getString("programs"));
                extrasMap.put("program",bundle.getString("program"));
                START_ACTIVITY
                        .apply(this,NoConnectionActivity.class)
                        .accept(extrasMap);
            }

            SET_PROGRAM_NAME.apply(this).accept(R.id.program_name,programOptional.get());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
            Map<String,Serializable> extrasMap = new HashMap<>();
            extrasMap.put("language",bundle.getString("language"));
            extrasMap.put("programs",bundle.getString("programs"));
            START_ACTIVITY
                    .apply(this,ProgramActivity.class)
                    .accept(extrasMap);
        }
        return super.onOptionsItemSelected(item);
    }
}
