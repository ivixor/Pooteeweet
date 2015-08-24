package com.ivixor.pooteeweet.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ivixor on 24.08.2015.
 */
public class AccessToken {

    @SerializedName("oauth_token")
    private String token;
    @SerializedName("oauth_token_secret")
    private String secret;

    public AccessToken(String token, String secret) {
        this.token = token;
        this.secret = secret;
    }

    public String getToken() {
        return token;
    }

    public String getSecret() {
        return secret;
    }
}
