package com.amanmehara.programming.android;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {

    //private String[] mDataset;

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

//    public LanguageAdapter(String[] myDataset) {
//        mDataset = myDataset;
//    }

    @Override
    public void onBindViewHolder(LanguageAdapter.ViewHolder viewHolder, int i) {
        //viewHolder.mTextView.setText(mDataset[i]);
        try {
            viewHolder.mTextView.setText(mDataset.getString(i));
        } catch (JSONException e) {
            e.printStackTrace();
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

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.language_name);
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
