package com.amanmehara.programming.android.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.amanmehara.programming.android.R;
import com.amanmehara.programming.android.common.Language;
import com.amanmehara.programming.android.rest.RestClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {

    private static final String TAG = LanguageAdapter.class.getSimpleName();
    private static final SparseArray<JSONArray> programs = new SparseArray<>();
    private final Activity activity;
    private final JSONArray languages;
    private final Function<Context,BiConsumer<String,JSONArray>> onClickCallback;

    public LanguageAdapter(
            Activity activity,
            JSONArray languages,
            Function<Context,BiConsumer<String,JSONArray>> onClickCallback) {
        this.activity = activity;
        this.languages = languages;
        this.onClickCallback = onClickCallback;
    }

    private static final Function<Activity,BiConsumer<String,Integer>> FETCH_PROGRAMS
            = activity -> (url,position) -> {
        new RestClient(activity, response -> {
            try {
                programs.append(position,new JSONArray(response));
            } catch (JSONException e) {
                Log.e(TAG,e.getMessage());
                programs.append(position,new JSONArray());
            }
        }).execute(url);
    };

    private static final Function<String,String> OBTAIN_LANGUAGE_NAME = (name) -> {
        Optional<Language> optLanguage = Stream
                .of(Language.values())
                .filter(language -> language.name().matches(name.toUpperCase()))
                .findAny();
        return optLanguage.map(Language::getDisplay).orElse(name);
    };

    private static final Function<JSONArray,Integer> OBTAIN_PROGRAM_COUNT = programs -> {
        int count = 0;
        for(int i=0; i<programs.length(); i++) {
            try {
                if(programs.getJSONObject(i).get("type").equals("dir")) {
                    count++;
                }
            } catch (JSONException e) {
                Log.e(TAG,e.getMessage());
            }
        }
        return count;
    };

    private static final Function<Activity,BiConsumer<ViewHolder,JSONArray>> SET_LANGUAGE_LOGO
            = activity -> (viewHolder,programs) -> {
        for(int i=0; i<programs.length(); i++) {
            try {
                JSONObject jsonObject = programs.getJSONObject(i);
                if(jsonObject.getString("name").equals("icon.png") && jsonObject.getString("type").equals("file")) {
                    new RestClient(activity, response -> {
                        try {
                            JSONObject icon = new JSONObject(response);
                            Bitmap logo = BitmapFactory
                                    .decodeByteArray(
                                            Base64.decode(icon.getString("content"),Base64.DEFAULT),
                                            0,
                                            Base64.decode(icon.getString("content"),Base64.DEFAULT).length
                                    );
                            viewHolder.mLanguageImage.setImageBitmap(logo);
                        } catch (JSONException e) {
                            Log.e(TAG,e.getMessage());
                            viewHolder.mLanguageImage.setImageResource(R.drawable.ic_circle_logo);
                        }
                    }).execute(jsonObject.getString("url"));
                }
            } catch (JSONException e) {
                Log.e(TAG,e.getMessage());
                viewHolder.mLanguageImage.setImageResource(R.drawable.ic_circle_logo);
            }
        }
    };

    @Override
    public LanguageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.language_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LanguageAdapter.ViewHolder viewHolder, int i) {
        try {
            JSONObject jsonObject = languages.getJSONObject(i);
            FETCH_PROGRAMS.apply(activity).accept(jsonObject.getString("url"),i);
            viewHolder.mLanguageName.setText(OBTAIN_LANGUAGE_NAME.apply(jsonObject.getString("name")));
            viewHolder.mLanguageCount.setText(OBTAIN_PROGRAM_COUNT.apply(programs.get(i)));
            SET_LANGUAGE_LOGO.apply(activity).accept(viewHolder,programs.get(i));
        } catch (JSONException e) {
            Log.e(TAG,e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return languages.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mLanguageName;
        private ImageView mLanguageImage;
        private TextView mLanguageCount;

        private ViewHolder(View v) {
            super(v);
            mLanguageName = (TextView) v.findViewById(R.id.language_name);
            mLanguageImage = (ImageView) v.findViewById(R.id.language_image);
            mLanguageCount = (TextView) v.findViewById(R.id.language_count);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int layoutPosition = getLayoutPosition();
            try {
                onClickCallback
                        .apply(activity.getApplicationContext())
                        .accept(
                                OBTAIN_LANGUAGE_NAME.apply(languages.getJSONObject(layoutPosition).getString("name")),
                                programs.get(layoutPosition)
                        );
            } catch (Exception e) {
                Log.e(TAG,e.getMessage());
            }
        }

    }
}