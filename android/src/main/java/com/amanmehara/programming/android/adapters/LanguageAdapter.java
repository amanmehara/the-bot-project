package com.amanmehara.programming.android.adapters;

import android.app.Activity;
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
import com.amanmehara.programming.android.rest.GithubAPIClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.function.BiConsumer;
import java.util.stream.Stream;

import static com.amanmehara.programming.android.common.Type.DIRECTORY;
import static com.amanmehara.programming.android.common.Type.FILE;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {

    private static final String TAG = LanguageAdapter.class.getSimpleName();
    private static final SparseArray<JSONArray> programs = new SparseArray<>();
    private final String accessToken;
    private final Activity activity;
    private final JSONArray languages;
    private final BiConsumer<String,JSONArray> onClickCallback;

    public LanguageAdapter(
            String accessToken,
            Activity activity,
            JSONArray languages,
            BiConsumer<String,JSONArray> onClickCallback) {
        this.accessToken = accessToken;
        this.activity = activity;
        this.languages = languages;
        this.onClickCallback = onClickCallback;
    }

    @Override
    public LanguageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.language_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LanguageAdapter.ViewHolder viewHolder, int i) {
        try {
            JSONObject programs = languages.getJSONObject(i);
            String url = programs.getString("url") + "&access_token=" + accessToken;
            fetchPrograms(viewHolder,url,i);
            viewHolder.languageNameView.setText(mapLanguageName(programs.getString("name")));
        } catch (JSONException e) {
            Log.e(TAG,e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return languages.length();
    }

    private void fetchPrograms(ViewHolder viewHolder, String url, int position) {
        new GithubAPIClient(activity, response -> {
            try {
                programs.append(position,new JSONArray(response));
            } catch (JSONException e) {
                Log.e(TAG,e.getMessage());
                programs.append(position,new JSONArray());
            }
            viewHolder.languageCountView.setText(String.valueOf(getProgramCount(programs.get(position))));
            setLanguageLogo(viewHolder,programs.get(position));
        }).execute(url);
    }

    private int getProgramCount(JSONArray programs) {
        int count = 0;
        for(int i=0; i<programs.length(); i++) {
            try {
                if(programs.getJSONObject(i).get("type").equals(DIRECTORY.getValue())) {
                    count++;
                }
            } catch (JSONException e) {
                Log.e(TAG,e.getMessage());
            }
        }
        return count;
    }

    private String mapLanguageName(String name) {
        return Stream.of(Language.values())
                .filter(language -> language.name().matches(name.toUpperCase()))
                .findAny()
                .map(Language::getDisplay)
                .orElse(name);
    }

    private void setLanguageLogo(ViewHolder viewHolder, JSONArray programs) {
        for(int i=0; i<programs.length(); i++) {
            try {
                JSONObject jsonObject = programs.getJSONObject(i);
                if(jsonObject.getString("name").equals("icon.png") && jsonObject.getString("type").equals(FILE.getValue())) {
                    String url = jsonObject.getString("url") + "&access_token=" + accessToken;
                    new GithubAPIClient(activity, response -> {
                        try {
                            JSONObject icon = new JSONObject(response);
                            byte[] imageBlob = Base64.decode(icon.getString("content"),Base64.DEFAULT);
                            int imageBlobLength = imageBlob.length;
                            Bitmap logo = BitmapFactory.decodeByteArray(imageBlob,0,imageBlobLength);
                            viewHolder.languageImageView.setImageBitmap(logo);
                        } catch (JSONException e) {
                            Log.e(TAG,e.getMessage());
                            viewHolder.languageImageView.setImageResource(R.drawable.ic_circle_logo);
                        }
                    }).execute(url);
                }
            } catch (JSONException e) {
                Log.e(TAG,e.getMessage());
                viewHolder.languageImageView.setImageResource(R.drawable.ic_circle_logo);
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView languageNameView;
        private ImageView languageImageView;
        private TextView languageCountView;

        private ViewHolder(View v) {
            super(v);
            languageNameView = (TextView) v.findViewById(R.id.language_name);
            languageImageView = (ImageView) v.findViewById(R.id.language_image);
            languageCountView = (TextView) v.findViewById(R.id.language_count);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int layoutPosition = getLayoutPosition();
            try {
                String languageName = mapLanguageName(languages.getJSONObject(layoutPosition).getString("name"));
                onClickCallback.accept(languageName,programs.get(layoutPosition));
            } catch (JSONException e) {
                Log.e(TAG,e.getMessage());
            }
        }

    }
}