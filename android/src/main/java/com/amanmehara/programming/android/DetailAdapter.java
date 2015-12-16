package com.amanmehara.programming.android;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {

    private JSONArray mDataset;
    private String language;

    public DetailAdapter(JSONArray myDataset, String language) {
        mDataset = myDataset;
        this.language = language;
    }

    @Override
    public DetailAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.files_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DetailAdapter.ViewHolder viewHolder, int i) {
        try {
            JSONObject jsonObject = mDataset.getJSONObject(i);
            viewHolder.mTextView.setText(jsonObject.getString("FileName"));

            String html = "<html>"
                    + "<link href='https://fonts.googleapis.com/css?family=Roboto+Mono:400' rel='stylesheet' type='text/css'>"
                    + "<link href='file:///android_asset/prism.css' rel='stylesheet'/>"
                    + "<script src='file:///android_asset/prism.js'></script>"
                    + "<body style='margin:0px; padding:0px;'>"
                    + "<pre class='line-numbers'><code class='language-"
                    + language
                    + "'>"
                    + Html.escapeHtml(jsonObject.getString("FileContent"))
                    + "</code></pre>"
                    + "</body>"
                    + "</html>";

            viewHolder.mWebView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "UTF-8", null);

            WebSettings webSettings = viewHolder.mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public WebView mWebView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.file_name);
            mWebView = (WebView) v.findViewById(R.id.file_content);
        }
    }
}
