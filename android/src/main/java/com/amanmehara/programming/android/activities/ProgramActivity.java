/*

 Copyright (C) 2015 - 2017 Aman Mehara

 This file is part of Programming!

 Programming! is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Programming! is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Programming!. If not, see <http://www.gnu.org/licenses/>.

 */

package com.amanmehara.programming.android.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.amanmehara.programming.android.adapters.ProgramAdapter;
import com.amanmehara.programming.android.R;
import com.amanmehara.programming.android.common.Type;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ProgramActivity extends BaseActivity {

    private static final String TAG = ProgramActivity.class.getSimpleName();
    private Bundle bundle;
    private String languageName;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program);
        recyclerView = setRecyclerView(R.id.programs_recycler_view);

        bundle = getIntent().getExtras();
        languageName = bundle.getString("languageName");
        byte[] logoBlob = bundle.getByteArray("logoBlob");
        String programsJson = bundle.getString("programs");

        String title = String.format("%s {programming}", languageName);

        setActionBar(R.id.toolbar, true, title);

        setLanguageDatails(logoBlob);

        try {
            setAdapter(new JSONArray(programsJson));
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
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
        switch (id) {
            case R.id.action_rate: {
                rate();
                return true;
            }
            case android.R.id.home: {
                startActivity(LanguageActivity.class, bundle);
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private boolean exclusion(String name) {
        return name.equals("icon.png");
    }

    private JSONArray filterPrograms(JSONArray programs) {
        JSONArray filtered = new JSONArray();
        for (int i = 0; i < programs.length(); i++) {
            JSONObject program = programs.optJSONObject(i);
            if (program != null) {
                String name = program.optString("name");
                String type = program.optString("type");
                if (type.equals(Type.DIRECTORY.getValue()) && !exclusion(name)) {
                    filtered.put(program);
                }
            }
        }
        return filtered;
    }

    private ProgramAdapter.Consumer<JSONObject> getOnClickCallback() {
        return program -> {
            Map<String, Serializable> extrasMap = new HashMap<>();
            extrasMap.put("program", program.toString());
            startActivity(DetailActivity.class, bundle, extrasMap);
        };
    }

    private void setAdapter() {
        setAdapter(new JSONArray());
    }

    private void setAdapter(JSONArray programs) {
        ProgramAdapter programAdapter = new ProgramAdapter(this, filterPrograms(programs), getOnClickCallback());
        recyclerView.setAdapter(programAdapter);
    }

    private void setLanguageDatails(byte[] logoBlob) {
        TextView name = findViewById(R.id.language_name);
        name.setText(languageName);
        ImageView image = findViewById(R.id.language_image);
        if (logoBlob != null) {
            int imageBlobLength = logoBlob.length;
            Bitmap logo = BitmapFactory.decodeByteArray(logoBlob, 0, imageBlobLength);
            image.setImageBitmap(logo);
        } else {
            image.setImageResource(R.drawable.ic_circle_logo);
        }
    }

}