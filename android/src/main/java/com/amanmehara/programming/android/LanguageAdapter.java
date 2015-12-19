package com.amanmehara.programming.android;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {

    private String language;

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
            language = mDataset.getString(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        switch (language) {
            case "c":
                viewHolder.mImageView.setImageResource(R.drawable.c);
                viewHolder.mTextView.setText("C");
                break;
            case "cpp":
                viewHolder.mImageView.setImageResource(R.drawable.cpp);
                viewHolder.mTextView.setText("C++");
                break;
            case "csharp":
                viewHolder.mImageView.setImageResource(R.drawable.csharp);
                viewHolder.mTextView.setText("C#");
                break;
            case "fsharp":
                viewHolder.mImageView.setImageResource(R.drawable.fsharp);
                viewHolder.mTextView.setText("F#");
                break;
            case "groovy":
                viewHolder.mImageView.setImageResource(R.drawable.groovy);
                viewHolder.mTextView.setText("Groovy");
                break;
            case "java":
                viewHolder.mImageView.setImageResource(R.drawable.java);
                viewHolder.mTextView.setText("Java");
                break;
            case "javascript":
                viewHolder.mImageView.setImageResource(R.drawable.javascript);
                viewHolder.mTextView.setText("JavaScript");
                break;
            case "php":
                viewHolder.mImageView.setImageResource(R.drawable.php);
                viewHolder.mTextView.setText("PHP");
                break;
            case "python":
                viewHolder.mImageView.setImageResource(R.drawable.python);
                viewHolder.mTextView.setText("Python");
                break;
            case "scala":
                viewHolder.mImageView.setImageResource(R.drawable.scala);
                viewHolder.mTextView.setText("Scala");
                break;
            case "sql":
                viewHolder.mImageView.setImageResource(R.drawable.sql);
                viewHolder.mTextView.setText("SQL");
                break;
            default:
                viewHolder.mImageView.setImageResource(R.drawable.ic_circle_logo);
                viewHolder.mTextView.setText(language);
        }


    }

    @Override
    public int getItemCount() {
        return mDataset.length();
    }

    public interface ListClickListener {
        void listItemClicked(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTextView;
        public ImageView mImageView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.language_name);
            mImageView = (ImageView) v.findViewById(R.id.language_image);
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
