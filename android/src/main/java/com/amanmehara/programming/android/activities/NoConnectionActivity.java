package com.amanmehara.programming.android.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.amanmehara.programming.android.R;
import com.amanmehara.programming.android.common.AppActivity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.amanmehara.programming.android.util.ActivityUtils.IS_CONNECTED;
import static com.amanmehara.programming.android.util.ActivityUtils.SET_ACTION_BAR;
import static com.amanmehara.programming.android.util.ActivityUtils.START_ACTIVITY;

public class NoConnectionActivity extends Activity {

    private Bundle bundle;
    private AppActivity appActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_connection);

        SET_ACTION_BAR.apply(this,R.id.my_toolbar).accept(true);

        bundle = getIntent().getExtras();
        appActivity = (AppActivity) bundle.getSerializable("activityInfo");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_no_connection, menu);
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

    public void tryAgain(View view) {

        if (IS_CONNECTED.test(getApplicationContext())) {
            Map<String,Serializable> extrasMap = new HashMap<>();
            switch (appActivity) {
                case LANGUAGE:
                    START_ACTIVITY
                            .apply(this,LanguageActivity.class)
                            .accept(extrasMap);
                    break;
                case DETAIL:
                    extrasMap.put("language",bundle.getString("language"));
                    extrasMap.put("programs",bundle.getString("programs"));
                    extrasMap.put("program",bundle.getString("program"));
                    START_ACTIVITY
                            .apply(this,DetailActivity.class)
                            .accept(extrasMap);
                    break;
                default:
            }
        } else {
            Map<String,Serializable> extrasMap = new HashMap<>();
            switch (appActivity) {
                case LANGUAGE:
                    extrasMap.put("activityInfo",AppActivity.LANGUAGE);
                    START_ACTIVITY
                            .apply(this,NoConnectionActivity.class)
                            .accept(extrasMap);
                    break;
                case DETAIL:
                    extrasMap.put("activityInfo", AppActivity.DETAIL);
                    extrasMap.put("language",bundle.getString("language"));
                    extrasMap.put("programs",bundle.getString("programs"));
                    extrasMap.put("program",bundle.getString("program"));
                    START_ACTIVITY
                            .apply(this,NoConnectionActivity.class)
                            .accept(extrasMap);
                    break;
                default:
            }
        }

    }
}
