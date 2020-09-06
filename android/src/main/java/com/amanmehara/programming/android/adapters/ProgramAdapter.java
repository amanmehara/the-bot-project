/*

 Copyright (C) 2015 - 2017 Aman Mehara

 This file is part of Programming!

 Programming! is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Programming! is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Programming!. If not, see <http://www.gnu.org/licenses/>.

 */

package com.amanmehara.programming.android.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amanmehara.programming.android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.function.Consumer;

public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ViewHolder> {

    private static final String TAG = ProgramAdapter.class.getSimpleName();
    private final JSONArray programs;
    private final Consumer<JSONObject> onClickCallback;

    public ProgramAdapter(
            JSONArray programs,
            Consumer<JSONObject> onClickCallback
    ) {
        this.programs = programs;
        this.onClickCallback = onClickCallback;
    }

    @NonNull
    @Override
    public ProgramAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.programs_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgramAdapter.ViewHolder viewHolder, int i) {
        try {
            viewHolder.mTextView.setText(programs.getJSONObject(i).getString("name"));
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
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
            mTextView = v.findViewById(R.id.programs_name);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try {
                onClickCallback.accept(programs.getJSONObject(getLayoutPosition()));
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }
}
