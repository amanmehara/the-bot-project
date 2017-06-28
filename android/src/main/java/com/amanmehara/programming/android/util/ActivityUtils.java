package com.amanmehara.programming.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toolbar;

import java.io.Serializable;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created by @amanmehara on 20-06-2017.
 */
public class ActivityUtils {

    public static final BiFunction<Activity,Integer,Consumer<Boolean>> SET_ACTION_BAR
            = (activity, id) -> homeAsUp -> {
        activity.setActionBar((Toolbar) activity.findViewById(id));
        activity.getActionBar().setDisplayHomeAsUpEnabled(homeAsUp);
    };

    public static final Predicate<Context> IS_CONNECTED = context -> {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    };

    public static final BiFunction<Context,Class<?>,Consumer<Map<String,Serializable>>> START_ACTIVITY
            = (context, clazz) -> extrasMap -> {
        Intent intent = new Intent(context,clazz);
        extrasMap.forEach(intent::putExtra);
        context.startActivity(intent);
    };

}
