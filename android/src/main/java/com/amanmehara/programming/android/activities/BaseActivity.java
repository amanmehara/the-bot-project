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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public abstract class BaseActivity extends Activity {

    boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return Objects.nonNull(activeNetworkInfo) && activeNetworkInfo.isConnectedOrConnecting();
    }

    void setActionBar(int id, boolean showHomeAsUp) {
        setActionBar(findViewById(id));
        ActionBar actionBar = getActionBar();
        //noinspection ConstantConditions
        actionBar.setDisplayHomeAsUpEnabled(showHomeAsUp);
    }

    RecyclerView setRecyclerView(int id) {
        RecyclerView recyclerView = findViewById(id);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        return recyclerView;
    }

    void startActivity(Class<? extends BaseActivity> clazz) {
        startActivity(clazz, Collections.emptyMap());
    }

    void startActivity(Class<? extends BaseActivity> clazz, Map<String, Serializable> extrasMap) {
        Intent intent = new Intent(this, clazz);
        extrasMap.forEach(intent::putExtra);
        startActivity(intent);
    }

    void rate() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=com.amanmehara.programming.android"));
        startActivity(intent);
    }

}
