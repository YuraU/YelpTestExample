package com.yura.yelptestexample.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiService {

    @POST(API.URL_TOKEN)
    Call<ResponseBody> getToken(@Query("grant_type") String grant_type, @Query("client_id") String client_id, @Query("client_secret") String client_secret);

    @GET(API.URL_SEARCH)
    Call<ResponseBody> search(@Header("Authorization") String authorization, @Query("latitude") double latitude, @Query("longitude") double longitude);

    @GET(API.URL_REVIEWS)
    Call<ResponseBody> reviews(@Header("Authorization") String authorization, @Path("id") String id);
}