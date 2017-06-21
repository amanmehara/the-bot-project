package com.amanmehara.programming.android.activities;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;
import com.amanmehara.programming.android.R;
import com.amanmehara.programming.android.common.AppActivity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.amanmehara.programming.android.util.ActivityUtils.START_ACTIVITY;


public class NoConnectionActivity extends Activity {

    private Context context;
    private Bundle bundle;
    private AppActivity appActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_connection);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setActionBar(myToolbar);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        bundle = getIntent().getExtras();
        appActivity = (AppActivity) bundle.getSerializable("activityInfo");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_no_connection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tryAgain(View view) {

        context = this.getApplicationContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetworkInfo != null &&
                activeNetworkInfo.isConnectedOrConnecting();

        if (isConnected) {
            Map<String,Serializable> extrasMap = new HashMap<>();
            switch (appActivity) {
                case LANGUAGE:
                    START_ACTIVITY
                            .apply(this,LanguageActivity.class)
                            .accept(extrasMap);
                    break;
                case PROGRAM:
                    extrasMap.put("language",bundle.getString("language"));
                    START_ACTIVITY
                            .apply(this,ProgramsActivity.class)
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
                case PROGRAM:
                    extrasMap.put("activityInfo", AppActivity.PROGRAM);
                    extrasMap.put("language", bundle.getString("language"));
                    START_ACTIVITY
                            .apply(this,NoConnectionActivity.class)
                            .accept(extrasMap);
                    break;
                default:
            }
        }

    }
}
