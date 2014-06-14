package ru.msinchevskaya.collageapplication.app.auth;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ru.msinchevskaya.collageapplication.app.R;


public class AuthDialog extends Dialog {

    private final Context mContext;
    private final String url;
    private final AuthDialogListener mListener;

    public interface AuthDialogListener{
        public void onComplete(String token);
        public void onError(String error);
    }

    public AuthDialog(Context context, String url, AuthDialogListener listener) {
        super(context);
        if (listener == null) {
            throw new NullPointerException("AuthDialogListener cannot be null");
        }
        mContext = context;
        this.url = url;
        mListener = listener;

        CookieSyncManager.createInstance(getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_login);

        WebView webView = (WebView) findViewById(R.id.wvLogin);
        webView.setWebViewClient(new AuthWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }

    class AuthWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.startsWith(mContext.getString(R.string.redirect_url))) {
                System.out.println(url);
                String urls[] = url.split("=");
                mListener.onComplete(urls[1]);
                AuthDialog.this.dismiss();
                return true;
            }
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            mListener.onError(description);
        }
    }
}
