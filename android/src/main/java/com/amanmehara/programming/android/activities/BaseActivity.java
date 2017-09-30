package com.amanmehara.programming.android.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toolbar;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Aman on 02-09-2017.
 */

public abstract class BaseActivity extends Activity {

    protected boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return Objects.nonNull(activeNetworkInfo) && activeNetworkInfo.isConnectedOrConnecting();
    }

    protected void setActionBar(int id) {
        setActionBar((Toolbar) findViewById(id));
    }

    protected RecyclerView setRecyclerView(int id) {
        RecyclerView recyclerView = (RecyclerView) findViewById(id);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        return recyclerView;
    }

    protected void startActivity(Class<? extends BaseActivity> clazz) {
        startActivity(clazz, Collections.emptyMap());
    }

    protected void startActivity(Class<? extends BaseActivity> clazz, Map<String, Serializable> extrasMap) {
        Intent intent = new Intent(this, clazz);
        extrasMap.forEach(intent::putExtra);
        startActivity(intent);
    }

}
