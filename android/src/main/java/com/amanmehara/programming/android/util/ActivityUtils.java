package com.amanmehara.programming.android.util;

import android.content.Context;
import android.content.Intent;

import java.io.Serializable;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Created by @amanmehara on 20-06-2017.
 */
public class ActivityUtils {

    public static final BiFunction<Context,Class<?>,Consumer<Map<String,Serializable>>> START_ACTIVITY
            = (context, clazz) -> extrasMap -> {
        Intent intent = new Intent(context,clazz);
        extrasMap.forEach(intent::putExtra);
        context.startActivity(intent);
    };

}
