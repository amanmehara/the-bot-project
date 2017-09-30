package com.amanmehara.programming.android.activities;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.amanmehara.programming.android.R;
import com.amanmehara.programming.android.activities.enumeration.Activity;
import com.amanmehara.programming.android.rest.GithubOAuthClient;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Created by @amanmehara on 03-09-2017.
 */

public class GithubOAuthActivity extends BaseActivity {

    private static final String TAG = GithubOAuthActivity.class.getSimpleName();
    private static final String CLIENT_ID = "##";
    private static final String CLIENT_SECRET = "##";
    private static final String REDIRECT_URI = "##";
    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_githuboauth);
        webView = (WebView) findViewById(R.id.authentication);
        webView.getSettings().setJavaScriptEnabled(true);

        if (isConnected()) {
            overrideUrlLoading();
            authenticate();
        } else {
            Map<String, Serializable> extrasMap = new HashMap<>();
            extrasMap.put("enumeration.Activity", Activity.GITHUB);
            startActivity(ConnectionActivity.class, extrasMap);
        }

    }

    private void overrideUrlLoading() {
        android.app.Activity activity = this;
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                if (uri.toString().startsWith(REDIRECT_URI)) {
                    String code = uri.getQueryParameter("code");
                    String state = uri.getQueryParameter("state");
                    String urlEncodedFormData = String.format("client_id=%s&client_secret=%s&code=%s", CLIENT_ID, CLIENT_SECRET, code);
                    new GithubOAuthClient(activity, getResponseCallback()).execute(urlEncodedFormData);
                }
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
    }

    private void authenticate() {
        String state = UUID.randomUUID().toString();
        webView.loadUrl(constructUrl(state));
    }

    private String constructUrl(String state) {
        return "http://github.com/login/oauth/authorize"
                + "?"
                + String.format("client_id=%s", CLIENT_ID)
                + "&"
                + String.format("redirect_uri=%s", REDIRECT_URI)
                + "&"
                + String.format("state=%s", state);
    }

    private Consumer<String> getResponseCallback() {
        return response -> {
            String accessToken = response.substring("access_token=".length(), response.indexOf("&"));
            Map<String, Serializable> extrasMap = new HashMap<>();
            extrasMap.put("accessToken", accessToken);
            startActivity(LanguageActivity.class, extrasMap);
        };
    }

}