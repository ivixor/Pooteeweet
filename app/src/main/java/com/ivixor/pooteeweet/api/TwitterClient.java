package com.ivixor.pooteeweet.api;

import android.text.TextUtils;
import android.util.Log;

import com.ivixor.pooteeweet.App;
import com.ivixor.pooteeweet.AuthUtils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.client.Client;
import retrofit.client.Header;
import retrofit.client.OkClient;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedOutput;

/**
 * Created by ivixor on 24.08.2015.
 */
public class TwitterClient implements Client {

    public static final String OAUTH_CALLBACK = "http://pooteeweet.ua";
    public static final String TWITTER_AUTH_URL_BASE = "https://api.twitter.com/oauth/authorize?oauth_token=";

    private static final int CONNECT_TIMEOUT_MILLIS = 15000;
    private static final int READ_TIMEOUT_MILLIS = 20000;

    private final Client client;
    private final String token;
    private final String tokenSecret;

    public TwitterClient(String token, String tokenSecret) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        client.setReadTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        client.setProtocols(Arrays.asList(Protocol.HTTP_1_1, Protocol.HTTP_2));
        this.client = new OkClient(client);
        this.token = token;
        this.tokenSecret = tokenSecret;
    }

    @Override
    public Response execute(Request request) throws IOException {
        Request signedRequest = sign(request);
        return client.execute(signedRequest);
    }

    private Request sign(Request request) {
        final HashMap<String, String> params = extractParams(request);

        List<Header> headers = request.getHeaders();
        ArrayList<Header> newHeaders = new ArrayList<>();
        newHeaders.addAll(headers);
        newHeaders.add(new Header("Authorization",
                AuthUtils.generateAuthorizationHeader(
                        params,
                        TwitterConsumerKeys.getConsumerKey(),
                        TwitterConsumerKeys.getConsumerSecret(),
                        token,
                        tokenSecret,
                        OAUTH_CALLBACK,
                        request.getMethod(),
                        request.getUrl(),
                        AuthUtils.generateNonce(),
                        AuthUtils.generateTimestamp()
                )));

        return new Request(request.getMethod(), request.getUrl(),
                newHeaders, request.getBody());
    }

    private HashMap<String, String> extractParams(Request request) {
        HashMap<String, String> params = new HashMap<>();

        try {
            URL url = new URL(request.getUrl());
            insertKeyValuePairs(params, url.getQuery());

            TypedOutput body = request.getBody();
            if (body != null && body.length() > 0) {
                Log.i("TEST", "Mime type: " + body.mimeType());
                insertKeyValuePairs(params, "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return params;
    }

    private void insertKeyValuePairs(HashMap<String, String> params, String data) {
        if (!TextUtils.isEmpty(data)) {
            String[] querySets = data.split("&");
            if (querySets.length > 0) {
                for (String query : querySets) {
                    String[] keyValuePair = query.split("=");
                    if (keyValuePair.length == 2) {
                        params.put(keyValuePair[0], keyValuePair[1]);
                    }
                }
            }
        }
    }
}
