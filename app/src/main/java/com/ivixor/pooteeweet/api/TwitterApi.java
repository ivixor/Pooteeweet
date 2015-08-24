package com.ivixor.pooteeweet.api;

import com.ivixor.pooteeweet.model.UserProfile;

import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by ivixor on 24.08.2015.
 */
public interface TwitterApi {

    String ENDPOINT = "https://api.twitter.com/";

    String VERIFIER = "oauth_verifier";

    @POST("/oauth/request_token")
    Observable<Response> getRequestToken();

    @FormUrlEncoded
    @POST("/oauth/access_token")
    Observable<Response> getAccessToken(@Field(VERIFIER) String verifier);

    @GET("/1.1/account/verify_credentials.json")
    Observable<UserProfile> getUserProfile();

}
