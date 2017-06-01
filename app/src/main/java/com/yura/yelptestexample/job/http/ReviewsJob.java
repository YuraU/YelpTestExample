package com.yura.yelptestexample.job.http;


import com.birbit.android.jobqueue.Params;
import com.yura.yelptestexample.api.API;
import com.yura.yelptestexample.api.response.Response;
import com.yura.yelptestexample.api.response.answer.ReviewsAnswer;
import com.yura.yelptestexample.api.response.answer.SearchAnswer;
import com.yura.yelptestexample.event.httpEvent.BaseHttpEvent;
import com.yura.yelptestexample.event.httpEvent.HttpEvent;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class ReviewsJob extends BaseHttpJob{

    public static final int PRIORITY = 1;
    private String id;

    public ReviewsJob(String id) {
        super(new Params(PRIORITY));
        this.id = id;
    }

    @Override
    protected Call<ResponseBody> apiCall() {
        return service.reviews(API.token, id);
    }

    @Override
    protected HttpEvent getHttpEvent(Response response) {
        return new BaseHttpEvent(response, BaseHttpEvent.REVIEWS);
    }

    @Override
    protected Class getAnswerClass() {
        return ReviewsAnswer.class;
    }
}
