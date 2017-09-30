package com.amanmehara.programming.android.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.amanmehara.programming.android.R;

public class MainActivity extends BaseActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("Programming", MODE_PRIVATE);

        setContentView(R.layout.activity_main);
        setActionBar(R.id.toolbar, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_invalidate_cache) {
            sharedPreferences.edit().clear().apply();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getStarted(View view) {
        startActivity(GithubOAuthActivity.class);
    }

}