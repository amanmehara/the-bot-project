package com.amanmehara.programming.android.activities;

import android.os.Bundle;
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
import java.util.function.Consumer;

public class ProgramActivity extends BaseActivity {

    private static final String TAG = ProgramActivity.class.getSimpleName();
    private String accessToken;
    private String languageName;
    private String programsJson;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_programs);
        setActionBar(R.id.my_toolbar,true);
        recyclerView = setRecyclerView(R.id.programs_recycler_view);

        Bundle bundle = getIntent().getExtras();
        accessToken = bundle.getString("accessToken");
        languageName = bundle.getString("languageName");
        programsJson = bundle.getString("programs");

        try {
            setAdapter(new JSONArray(programsJson));
        } catch (JSONException e) {
            Log.e(TAG,e.getMessage());
            setAdapter();
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
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Consumer<JSONObject> getOnClickCallback() {
        return program -> {
            Map<String,Serializable> extrasMap = new HashMap<>();
            extrasMap.put("accessToken",accessToken);
            extrasMap.put("languageName",languageName);
            extrasMap.put("programs",programsJson);
            extrasMap.put("program",program.toString());
            startActivity(DetailActivity.class,extrasMap);
        };
    }

    private void setAdapter() {
        setAdapter(new JSONArray());
    }

    private void setAdapter(JSONArray programs) {
        ProgramAdapter programAdapter = new ProgramAdapter(this,programs,getOnClickCallback());
        recyclerView.setAdapter(programAdapter);
    }

}
