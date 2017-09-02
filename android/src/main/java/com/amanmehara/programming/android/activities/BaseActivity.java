package com.amanmehara.programming.android.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toolbar;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Aman on 02-09-2017.
 */

public abstract class BaseActivity extends Activity {

    public void setActionBar(int id, boolean homeAsUp) {
        setActionBar((Toolbar)findViewById(id));
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(homeAsUp);
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return Objects.nonNull(activeNetworkInfo) && activeNetworkInfo.isConnectedOrConnecting();
    }

    public void startActivity(Class<? extends BaseActivity> clazz) {
        startActivity(clazz, Collections.emptyMap());
    }

    public void startActivity(Class<? extends BaseActivity> clazz, Map<String,Serializable> extrasMap) {
        Intent intent = new Intent(this,clazz);
        extrasMap.forEach(intent::putExtra);
        startActivity(intent);
    }

}
