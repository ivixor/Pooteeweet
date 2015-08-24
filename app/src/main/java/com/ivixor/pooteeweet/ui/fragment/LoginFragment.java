package com.ivixor.pooteeweet.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import com.ivixor.pooteeweet.AccessTokenStore;
import com.ivixor.pooteeweet.AuthUtils;
import com.ivixor.pooteeweet.R;
import com.ivixor.pooteeweet.api.TwitterClient;
import com.ivixor.pooteeweet.api.TwitterOAuthHelper;
import com.ivixor.pooteeweet.model.AccessToken;
import com.ivixor.pooteeweet.model.RequestToken;
import com.ivixor.pooteeweet.ui.activity.BaseActivity;
import com.ivixor.pooteeweet.ui.activity.LoginActivity;
import com.ivixor.pooteeweet.ui.activity.TimelineActivity;

import java.util.regex.Pattern;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ivixor on 24.08.2015.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener {

    public final static String TAG = LoginFragment.class.getSimpleName();
    public final static String TITLE = "Login";

    private Subscription subscription;
    private RequestToken requestToken = null;
    private TwitterOAuthHelper twitterOAuthHelper = new TwitterOAuthHelper();
    private AccessTokenStore accessTokenStore = new AccessTokenStore();

    private WebView webView;
    private ProgressBar progressBar;
    private View errorView;
    private Button loginButton;
    private Button retryButton;

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    @Override
    public String getFragmentTitle() {
        return TITLE;
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void prepareViews(View rootView) {
        webView = (WebView) rootView.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new TwitterWebViewClient());

        loginButton = (Button) rootView.findViewById(R.id.button_login);
        loginButton.setOnClickListener(this);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressbar);

        errorView = rootView.findViewById(R.id.view_error);
        retryButton = (Button) errorView.findViewById(R.id.button_retry);
        retryButton.setOnClickListener(this);
    }

    @Override
    protected void customizeToolbar(ActionBar actionBar) {
        if (actionBar != null) {
            actionBar.setTitle(getFragmentTitle());
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void customizeViews(View rootView) {


    }

    @Override
    public void onResume() {
        super.onResume();

        AccessToken accessToken = accessTokenStore.loadAccessToken(getActivity());
        if (accessToken != null) {
            startActivity(new Intent(getActivity(), TimelineActivity.class));
            getActivity().finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_login:
                loginButton.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                webView.setVisibility(View.VISIBLE);
                requestToken();
                return;
            case R.id.button_retry:
                errorView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
                requestToken();
                return;
        }
    }

    private void handleError(Throwable throwable) {
        throwable.printStackTrace();
        webView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        requestToken = null;
    }

    private void requestToken() {
        subscription = twitterOAuthHelper.getRequestToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TwitterRequestTokenObserver());
    }

    private class TwitterWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            progressBar.setVisibility(View.GONE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!TextUtils.isEmpty(url) && url.startsWith(TwitterClient.OAUTH_CALLBACK)) {
                String payload = url.substring(TwitterClient.OAUTH_CALLBACK.length() + 2);
                String params[] = payload.split(Pattern.quote("&"));
                if (params.length == 2) {
                    String oAuthToken = null;
                    if (!TextUtils.isEmpty(params[0]) && params[0].startsWith(AuthUtils.TOKEN + "=")) {
                        oAuthToken = params[0].substring(AuthUtils.TOKEN.length() + 1);
                    }

                    String oAuthVerifier = null;
                    if (!TextUtils.isEmpty(params[1]) && params[1].startsWith(AuthUtils.VERIFIER + "=")) {
                        oAuthVerifier = params[1].substring(AuthUtils.VERIFIER.length() + 1);
                    }

                    if (requestToken.getToken().equals(oAuthToken) && !TextUtils.isEmpty(oAuthVerifier)) {
                        subscription = twitterOAuthHelper.getAccessToken(oAuthToken, oAuthVerifier)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new TwitterAccessTokenObserver());
                    } else {
                        handleError(new Exception("Bad request token response"));
                    }
                }
            }

            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    private class TwitterRequestTokenObserver implements Observer<RequestToken> {

        @Override
        public void onCompleted() {
            if (subscription != null && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
        }

        @Override
        public void onError(Throwable e) {
            handleError(e);

            if (subscription != null && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
        }

        @Override
        public void onNext(RequestToken requestToken) {
            LoginFragment.this.requestToken = requestToken;
            webView.loadUrl(TwitterClient.TWITTER_AUTH_URL_BASE + requestToken.getToken());
        }
    }

    private class TwitterAccessTokenObserver implements Observer<AccessToken> {

        @Override
        public void onCompleted() {
            if (subscription != null && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
        }

        @Override
        public void onError(Throwable e) {
            handleError(e);

            if (subscription != null && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
        }

        @Override
        public void onNext(AccessToken accessToken) {
            progressBar.setVisibility(View.GONE);
            new AccessTokenStore().storeAccessToken(getActivity(), accessToken);
            startActivity(new Intent(getActivity(), TimelineActivity.class));
            getActivity().finish();
        }
    }


}
