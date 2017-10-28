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

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.amanmehara.programming.android.R;

public class FileActivity extends BaseActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();
    private Bundle bundle;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_file);
        setActionBar(R.id.toolbar, true);

        bundle = getIntent().getExtras();
        String fileName = bundle.getString("fileName");
        String fileContent = bundle.getString("fileContent");

        TextView fileNameView = findViewById(R.id.file_name);
        fileNameView.setText(fileName);

        WebView fileContentView = findViewById(R.id.file_content);

        WebSettings webSettings = fileContentView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setTextZoom(75);

        fileContentView.loadDataWithBaseURL("file:///android_asset/", fileContent, "text/html", "UTF-8", null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file, menu);
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
                startActivity(DetailActivity.class, bundle);
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

}