package com.yura.yelptestexample.event.httpEvent;


import com.yura.yelptestexample.api.response.Response;

public class BaseHttpEvent extends HttpEvent {

    public final static int GET_TOKEN = 0;
    public final static int SEARCH    = 1;
    public final static int REVIEWS   = 2;

    public int type;

    public BaseHttpEvent(Response data, int type) {
        super(data);

        this.type = type;
    }
}
