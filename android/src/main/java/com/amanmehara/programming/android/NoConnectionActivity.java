package com.amanmehara.programming.android;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class NoConnectionActivity extends AppCompatActivity {

    private Context context;
    private Bundle bundle;
    private ActivitiesAsEnum activitiesAsEnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_connection);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bundle = getIntent().getExtras();
        activitiesAsEnum = (ActivitiesAsEnum) bundle.getSerializable("activityInfo");
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
            Intent intent;
            switch (activitiesAsEnum) {
                case LANGUAGE_ACTIVITY:
                    intent = new Intent(this, LanguageActivity.class);
                    startActivity(intent);
                    break;
                case PROGRAMS_ACTIVITY:
                    intent = new Intent(this, ProgramsActivity.class);
                    intent.putExtra("language", bundle.getString("language"));
                    startActivity(intent);
                    break;
            }
        } else {
            Intent intent = new Intent(this, NoConnectionActivity.class);
            switch (activitiesAsEnum) {
                case LANGUAGE_ACTIVITY:
                    intent.putExtra("activityInfo", ActivitiesAsEnum.LANGUAGE_ACTIVITY);
                    break;
                case PROGRAMS_ACTIVITY:
                    intent.putExtra("activityInfo", ActivitiesAsEnum.PROGRAMS_ACTIVITY);
                    intent.putExtra("language", bundle.getString("language"));
                    break;
            }
            startActivity(intent);
        }

    }
}
