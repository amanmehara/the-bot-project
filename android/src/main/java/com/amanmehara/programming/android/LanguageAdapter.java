package com.amanmehara.programming.android;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {

    private String language;

    private String programCount;

    private JSONArray mDataset;

    private ListClickListener listClickListener;

    public LanguageAdapter(JSONArray myDataset) {
        mDataset = myDataset;
    }

    public ListClickListener getListClickListener() {
        return listClickListener;
    }

    public void setListClickListener(ListClickListener listClickListener) {
        this.listClickListener = listClickListener;
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
            JSONObject jsonObject = mDataset.getJSONObject(i);
            language = jsonObject.getString("LanguageName");
            programCount = jsonObject.getString("LanguageCount");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        switch (language) {
            case "c":
                viewHolder.mLanguageImage.setImageResource(R.drawable.c);
                viewHolder.mLanguageName.setText("C");
                break;
            case "cpp":
                viewHolder.mLanguageImage.setImageResource(R.drawable.cpp);
                viewHolder.mLanguageName.setText("C++");
                break;
            case "csharp":
                viewHolder.mLanguageImage.setImageResource(R.drawable.csharp);
                viewHolder.mLanguageName.setText("C#");
                break;
            case "fsharp":
                viewHolder.mLanguageImage.setImageResource(R.drawable.fsharp);
                viewHolder.mLanguageName.setText("F#");
                break;
            case "groovy":
                viewHolder.mLanguageImage.setImageResource(R.drawable.groovy);
                viewHolder.mLanguageName.setText("Groovy");
                break;
            case "java":
                viewHolder.mLanguageImage.setImageResource(R.drawable.java);
                viewHolder.mLanguageName.setText("Java");
                break;
            case "javascript":
                viewHolder.mLanguageImage.setImageResource(R.drawable.javascript);
                viewHolder.mLanguageName.setText("JavaScript");
                break;
            case "php":
                viewHolder.mLanguageImage.setImageResource(R.drawable.php);
                viewHolder.mLanguageName.setText("PHP");
                break;
            case "python":
                viewHolder.mLanguageImage.setImageResource(R.drawable.python);
                viewHolder.mLanguageName.setText("Python");
                break;
            case "scala":
                viewHolder.mLanguageImage.setImageResource(R.drawable.scala);
                viewHolder.mLanguageName.setText("Scala");
                break;
            case "sql":
                viewHolder.mLanguageImage.setImageResource(R.drawable.sql);
                viewHolder.mLanguageName.setText("SQL");
                break;
            default:
                viewHolder.mLanguageImage.setImageResource(R.drawable.ic_circle_logo);
                viewHolder.mLanguageName.setText(language);
        }

        viewHolder.mLanguageCount.setText(programCount);

    }

    @Override
    public int getItemCount() {
        return mDataset.length();
    }

    public interface ListClickListener {
        void listItemClicked(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mLanguageName;
        public ImageView mLanguageImage;
        public TextView mLanguageCount;

        public ViewHolder(View v) {
            super(v);
            mLanguageName = (TextView) v.findViewById(R.id.language_name);
            mLanguageImage = (ImageView) v.findViewById(R.id.language_image);
            mLanguageCount = (TextView) v.findViewById(R.id.language_count);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (getListClickListener() != null) {
                listClickListener.listItemClicked(v, getPosition());
            }
        }
    }
}
