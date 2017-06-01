package com.yura.yelptestexample.job.http;

import com.birbit.android.jobqueue.Params;
import com.yura.yelptestexample.api.API;
import com.yura.yelptestexample.api.response.Response;
import com.yura.yelptestexample.api.response.answer.MapAnswer;
import com.yura.yelptestexample.event.httpEvent.BaseHttpEvent;
import com.yura.yelptestexample.event.httpEvent.HttpEvent;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class GetTokenJob extends BaseHttpJob {
    public static final int PRIORITY = 1;

    public GetTokenJob() {
        super(new Params(PRIORITY));
    }

    @Override
    protected Call<ResponseBody> apiCall() {
        return service.getToken("client_credentials", API.client_id, API.client_secret);
    }

    @Override
    protected HttpEvent getHttpEvent(Response response) {
        return new BaseHttpEvent(response, BaseHttpEvent.GET_TOKEN);
    }

    @Override
    protected Class getAnswerClass() {
        return MapAnswer.class;
    }
}
