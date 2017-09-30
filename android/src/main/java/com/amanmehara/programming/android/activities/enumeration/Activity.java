package com.amanmehara.programming.android.activities.enumeration;

import com.amanmehara.programming.android.activities.BaseActivity;
import com.amanmehara.programming.android.activities.ConnectionActivity;
import com.amanmehara.programming.android.activities.DetailActivity;
import com.amanmehara.programming.android.activities.GithubOAuthActivity;
import com.amanmehara.programming.android.activities.LanguageActivity;
import com.amanmehara.programming.android.activities.MainActivity;
import com.amanmehara.programming.android.activities.ProgramActivity;

/**
 * Created by @amanmehara on 20-06-2017.
 */
public enum Activity {

    CONNECTION(ConnectionActivity.class),
    DETAIL(DetailActivity.class),
    GITHUB(GithubOAuthActivity.class),
    LANGUAGE(LanguageActivity.class),
    MAIN(MainActivity.class),
    PROGRAM(ProgramActivity.class);

    private final Class<? extends BaseActivity> clazz;

    Activity(Class<? extends BaseActivity> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends BaseActivity> getClazz() {
        return clazz;
    }

}
