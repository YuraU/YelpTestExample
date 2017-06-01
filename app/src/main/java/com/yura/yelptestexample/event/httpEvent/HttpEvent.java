package com.yura.yelptestexample.event.httpEvent;


import com.yura.yelptestexample.api.response.Response;

public abstract class HttpEvent {

    protected Response data;

    public HttpEvent(Response data) {
        this.data = data;
    }

    public Response getResult() {
        return data;
    }
}
