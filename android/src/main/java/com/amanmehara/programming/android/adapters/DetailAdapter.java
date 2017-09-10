package com.amanmehara.programming.android.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import com.amanmehara.programming.android.R;
import com.amanmehara.programming.android.common.Language;
import com.amanmehara.programming.android.rest.GithubAPIClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.stream.Stream;

import static com.amanmehara.programming.android.common.Type.FILE;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {

    private static final String TAG = DetailAdapter.class.getSimpleName();
    private static final SparseArray<JSONObject> resolvedContents = new SparseArray<>();
    private final String accessToken;
    private final Activity activity;
    private final String languageName;
    private final JSONArray programContents;


    public DetailAdapter(
            String accessToken,
            Activity activity,
            String languageName,
            JSONArray programContents
    ) {
        this.accessToken = accessToken;
        this.activity = activity;
        this.languageName = languageName;
        this.programContents = programContents;
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
            JSONObject jsonObject = programContents.getJSONObject(i);
            String url = jsonObject.getString("url") + "&access_token=" + accessToken;
            resolveContent(viewHolder,url,i);
            viewHolder.mTextView.setText(jsonObject.getString("name"));
        } catch (JSONException e) {
            Log.e(TAG,e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return programContents.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;
        private WebView mWebView;

        private ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.file_name);
            mWebView = (WebView) v.findViewById(R.id.file_content);
        }
    }

    private String decodeContent(String content) {
        return new String(Base64.decode(content,Base64.DEFAULT));
    }

    private String generateHtml(String content) {
        return "<html>"
                + "<link href='https://fonts.googleapis.com/css?family=Roboto+Mono:400' rel='stylesheet' type='text/css'>"
                + "<link href='file:///android_asset/prism.css' rel='stylesheet'/>"
                + "<script src='file:///android_asset/prism.js'></script>"
                + "<body style='margin:0px; padding:0px;'>"
                + "<pre class='line-numbers'><code class='language-"
                + reverseMapLanguageName(languageName)
                + "'>"
                + Html.escapeHtml(content)
                + "</code></pre>"
                + "</body>"
                + "</html>";
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void resolveContent(ViewHolder viewHolder, String url, int position) {
        new GithubAPIClient(activity, response -> {
            try {
                resolvedContents.append(position,new JSONObject(response));
                if(resolvedContents.get(position).getString("type").equals(FILE.getValue())) {
                    String html = generateHtml(decodeContent(resolvedContents.get(position).getString("content")));
                    viewHolder.mWebView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "UTF-8", null);
                }
                WebSettings webSettings = viewHolder.mWebView.getSettings();
                webSettings.setJavaScriptEnabled(true);
            } catch (JSONException e) {
                Log.e(TAG,e.getMessage());
            }
        }).execute(url);
    }

    private String reverseMapLanguageName(String name) {
        return Stream.of(Language.values())
                .filter(language -> language.getDisplay().matches(name))
                .findAny()
                .map(Enum::name)
                .map(String::toLowerCase)
                .orElse(name);
    }

}
