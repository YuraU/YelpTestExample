package com.yura.yelptestexample.api.response;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class Response<T> {

    @Nullable
    private T mAnswer;

    private RequestResult mRequestResult;

    public Response() {
        mRequestResult = RequestResult.ERROR;
    }

    @NonNull
    public RequestResult getRequestResult() {
        return mRequestResult;
    }

    public Response setRequestResult(@NonNull RequestResult requestResult) {
        mRequestResult = requestResult;
        return this;
    }

    @Nullable
    public <T> T getTypedAnswer() {
        if (mAnswer == null) {
            return null;
        }
        //noinspection unchecked
        return (T) mAnswer;
    }

    @NonNull
    public Response setAnswer(@Nullable T answer) {
        if(answer == null){
            mAnswer = null;
            return this;
        }

        mAnswer = answer;
        answer = null;
        return this;
    }

    public void save(@NonNull Context context, String url, String jsonString) {
    }
}

