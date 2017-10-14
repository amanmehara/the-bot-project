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
 along with Foobar. If not, see <http://www.gnu.org/licenses/>.

 */

package com.amanmehara.programming.android.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.amanmehara.programming.android.R;
import com.amanmehara.programming.android.activities.enumeration.Activity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ConnectionActivity extends BaseActivity {

    private Bundle bundle;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        setActionBar(R.id.toolbar, true);
        bundle = getIntent().getExtras();
        activity = (Activity) bundle.getSerializable("enumeration.Activity");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_connection, menu);
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
                startActivity(MainActivity.class);
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    public void tryAgain(View view) {

        if (isConnected()) {
            Map<String, Serializable> extrasMap = new HashMap<>();
            switch (activity) {
                case DETAIL:
                    extrasMap.put("accessToken", bundle.getString("accessToken"));
                    extrasMap.put("languageName", bundle.getString("languageName"));
                    extrasMap.put("programs", bundle.getString("programs"));
                    extrasMap.put("program", bundle.getString("program"));
                    startActivity(DetailActivity.class, extrasMap);
                    break;
                case GITHUB:
                    startActivity(GithubOAuthActivity.class);
                    break;
                case LANGUAGE:
                    extrasMap.put("accessToken", bundle.getString("accessToken"));
                    startActivity(LanguageActivity.class, extrasMap);
                    break;
                default:
            }
        } else {
            Map<String, Serializable> extrasMap = new HashMap<>();
            switch (activity) {
                case DETAIL:
                    extrasMap.put("enumeration.Activity", Activity.DETAIL);
                    extrasMap.put("accessToken", bundle.getString("accessToken"));
                    extrasMap.put("languageName", bundle.getString("languageName"));
                    extrasMap.put("programs", bundle.getString("programs"));
                    extrasMap.put("program", bundle.getString("program"));
                    startActivity(ConnectionActivity.class, extrasMap);
                    break;
                case GITHUB:
                    extrasMap.put("enumeration.Activity", Activity.GITHUB);
                    startActivity(ConnectionActivity.class, extrasMap);
                    break;
                case LANGUAGE:
                    extrasMap.put("enumeration.Activity", Activity.LANGUAGE);
                    extrasMap.put("accessToken", bundle.getString("accessToken"));
                    startActivity(ConnectionActivity.class, extrasMap);
                    break;
                default:
            }
        }

    }

}
