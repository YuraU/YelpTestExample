package com.yura.yelptestexample.job.http;


import com.birbit.android.jobqueue.Params;
import com.yura.yelptestexample.api.API;
import com.yura.yelptestexample.api.response.Response;
import com.yura.yelptestexample.api.response.answer.SearchAnswer;
import com.yura.yelptestexample.event.httpEvent.BaseHttpEvent;
import com.yura.yelptestexample.event.httpEvent.HttpEvent;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class SearchJob extends BaseHttpJob {
    public static final int PRIORITY = 1;
    private double lat, lon;

    public SearchJob(double lat, double lon) {
        super(new Params(PRIORITY));
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    protected Call<ResponseBody> apiCall() {
        return service.search(API.token, lat, lon);
    }

    @Override
    protected HttpEvent getHttpEvent(Response response) {
        return new BaseHttpEvent(response, BaseHttpEvent.SEARCH);
    }

    @Override
    protected Class getAnswerClass() {
        return SearchAnswer.class;
    }
}
