package com.amanmehara.programming.android;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProgramsAdapter extends RecyclerView.Adapter<ProgramsAdapter.ViewHolder> {

    private JSONArray mDataset;

    private ListClickListener listClickListener;

    public ProgramsAdapter(JSONArray myDataset) {
        mDataset = myDataset;
    }

    public ListClickListener getListClickListener() {
        return listClickListener;
    }

    public void setListClickListener(ListClickListener listClickListener) {
        this.listClickListener = listClickListener;
    }

    @Override
    public ProgramsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.programs_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProgramsAdapter.ViewHolder viewHolder, int i) {
        try {
            JSONObject jsonObject = mDataset.getJSONObject(i);
            viewHolder.mTextView.setText(jsonObject.getString("ProgramName"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        switch (i%4) {
//            case 0:
//                viewHolder.mTextView.setBackgroundColor(Color.RED);
//                break;
//            case 1:
//                viewHolder.mTextView.setBackgroundColor(Color.GREEN);
//                break;
//            case 2:
//                viewHolder.mTextView.setBackgroundColor(Color.BLUE);
//                break;
//            case 3:
//                viewHolder.mTextView.setBackgroundColor(Color.MAGENTA);
//                break;
//        }
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
            mTextView = (TextView) v.findViewById(R.id.programs_name);
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
