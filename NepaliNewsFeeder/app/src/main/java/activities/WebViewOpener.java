package activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import nepalinewsfeeder.sabinkharel.com.fragment.R;

public class WebViewOpener extends AppCompatActivity {

    private WebView webview;
    public static String intentUrl = null;
    private ProgressBar mProgressBar;
    private boolean isWebViewLoadingFirstPage = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_opener);
        Intent intent = getIntent();
        intentUrl = intent.getStringExtra(NewsApiRecycleViewAdapter.NEWSLINK);
        mProgressBar = findViewById(R.id.progressBars);
        webview = findViewById(R.id.webView);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webview.setWebViewClient(new MyWebView());
        webview.loadUrl(intentUrl);
        mProgressBar.setVisibility(View.VISIBLE);
        webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressBar.setVisibility(ProgressBar.VISIBLE);
                webview.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                mProgressBar.setVisibility(ProgressBar.GONE);
                webview.setVisibility(View.VISIBLE);
                isWebViewLoadingFirstPage = false;
            }
        });
    }

    private class MyWebView extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(intentUrl);
            return true;
        }
    }
}
