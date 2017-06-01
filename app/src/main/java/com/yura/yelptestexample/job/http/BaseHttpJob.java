package com.yura.yelptestexample.job.http;

import com.birbit.android.jobqueue.Params;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yura.yelptestexample.api.ApiFactory;
import com.yura.yelptestexample.api.ApiService;
import com.yura.yelptestexample.api.response.RequestResult;
import com.yura.yelptestexample.api.response.Response;
import com.yura.yelptestexample.api.response.answer.MapAnswer;
import com.yura.yelptestexample.event.httpEvent.HttpEvent;
import com.yura.yelptestexample.job.BaseJob;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.ResponseBody;
import retrofit2.Call;


public abstract class BaseHttpJob extends BaseJob {

    public final static String TAG = "HttpJob";

    protected Response response;

    private final int id;
    private static final AtomicInteger jobCounter = new AtomicInteger(0);
    protected ApiService service = ApiFactory.getApiService();
    protected String jsonString;
    protected Object answer;
    protected String url;

    protected static final String GROUP = "http";

    protected BaseHttpJob(Params params) {
        super(params.groupBy(GROUP).addTags(TAG));
        id = jobCounter.incrementAndGet();
    }

    @Override
    public void onRun() throws Throwable {
        try {
            response = new Response();
            Call<ResponseBody> call = apiCall();
            getUrl(call);

            response.setAnswer(getHttpAnswer(call));
            checkResponseAnswer(response);
            if (response.getRequestResult() == RequestResult.SUCCESS) {
                onSuccess();
            } else {
                onError();
            }
        } catch (IOException e) {
            onError();
        }

        EventBus.getDefault().post(getHttpEvent(response));
    }

    protected void onSuccess() {
        saveResponse();
    }

    protected void onError(){

    }

    protected void checkResponseAnswer(Response response){

    }

    protected abstract Call<ResponseBody> apiCall();

    protected abstract HttpEvent getHttpEvent(Response response);

    protected void parseResponse(retrofit2.Response<ResponseBody> retrofitResponse) throws IOException {
        jsonString = retrofitResponse.body().string();
    }

    protected void getUrl(Call call){
        url = call.request().url().toString();
    }

    protected Object getHttpAnswer(Call<ResponseBody> call) throws IOException {
        retrofit2.Response<ResponseBody> retrofitResponse = call.execute();
        parseResponse(retrofitResponse);

        answer = new Gson().fromJson(jsonString, getAnswerClass());

        return answer;
    }

    protected abstract Class getAnswerClass();


    protected void saveResponse(){
    }


}
