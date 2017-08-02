package com.amanmehara.programming.android.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.amanmehara.programming.android.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ViewHolder> {

    private static final String TAG = ProgramAdapter.class.getSimpleName();
    private final Activity activity;
    private final String language;
    private final JSONArray programs;
    private final BiFunction<Context,JSONArray,BiConsumer<String,JSONObject>> onClickCallback;

    public ProgramAdapter(
            Activity activity,
            String language,
            JSONArray programs,
            BiFunction<Context,JSONArray,BiConsumer<String,JSONObject>> onClickCallback
    ) {
        this.activity = activity;
        this.language = language;
        this.programs = programs;
        this.onClickCallback = onClickCallback;
    }

    @Override
    public ProgramAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.programs_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProgramAdapter.ViewHolder viewHolder, int i) {
        try {
            viewHolder.mTextView.setText(programs.getJSONObject(i).getString("name"));
        } catch (JSONException e) {
            Log.e(TAG,e.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return programs.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTextView;

        private ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.programs_name);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try {
                onClickCallback
                        .apply(activity.getApplicationContext(),programs)
                        .accept(language,programs.getJSONObject(getLayoutPosition()));
            } catch (Exception e) {
                Log.e(TAG,e.getMessage());
            }
        }
    }
}
