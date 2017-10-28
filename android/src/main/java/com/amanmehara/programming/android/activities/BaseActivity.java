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

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toolbar;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

public abstract class BaseActivity extends Activity {

    protected boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    protected void setActionBar(int id, boolean showHomeAsUp) {
        setActionBar(findViewById(id));
        ActionBar actionBar = getActionBar();
        //noinspection ConstantConditions
        actionBar.setDisplayHomeAsUpEnabled(showHomeAsUp);
    }

    protected RecyclerView setRecyclerView(int id) {
        RecyclerView recyclerView = findViewById(id);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        return recyclerView;
    }

    protected void startActivity(Class<? extends BaseActivity> clazz) {
        startActivity(clazz, Collections.emptyMap());
    }

    protected void startActivity(Class<? extends BaseActivity> clazz, Bundle bundle) {
        startActivity(clazz, bundle, Collections.emptyMap());
    }

    protected void startActivity(Class<? extends BaseActivity> clazz, Map<String, Serializable> extrasMap) {
        Intent intent = new Intent(this, clazz);
        for (Map.Entry<String, Serializable> entry : extrasMap.entrySet()) {
            intent.putExtra(entry.getKey(), entry.getValue());
        }
        startActivity(intent);
    }

    protected void startActivity(Class<? extends BaseActivity> clazz, Bundle bundle, Map<String, Serializable> extrasMap) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(bundle);
        for (Map.Entry<String, Serializable> entry : extrasMap.entrySet()) {
            intent.putExtra(entry.getKey(), entry.getValue());
        }
        startActivity(intent);
    }

    protected void rate() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=com.amanmehara.programming.android"));
        startActivity(intent);
    }

}