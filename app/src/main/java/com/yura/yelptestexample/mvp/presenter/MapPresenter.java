package com.yura.yelptestexample.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.birbit.android.jobqueue.JobManager;
import com.yura.yelptestexample.YelpApp;
import com.yura.yelptestexample.api.API;
import com.yura.yelptestexample.api.response.answer.MapAnswer;
import com.yura.yelptestexample.api.response.answer.SearchAnswer;
import com.yura.yelptestexample.event.httpEvent.BaseHttpEvent;
import com.yura.yelptestexample.job.http.GetTokenJob;
import com.yura.yelptestexample.job.http.SearchJob;
import com.yura.yelptestexample.mvp.view.MapView;

import java.util.Map;

@InjectViewState
public class MapPresenter extends BasePresenter<MapView>{

    public void getToken(){
        JobManager jobManager = YelpApp.getInstance().getJobManager();
        jobManager.addJobInBackground(new GetTokenJob());
    }

    public void searchRestaurants(Double lat, Double lon){
        JobManager jobManager = YelpApp.getInstance().getJobManager();
        jobManager.addJobInBackground(new SearchJob(lat, lon));
    }


    public void messageEvent(BaseHttpEvent event){
        if (event.type == BaseHttpEvent.GET_TOKEN) {
            Map map = (Map) event.getResult().getTypedAnswer();
            API.token = map.get("token_type") + " " + map.get("access_token");
        } else if (event.type == BaseHttpEvent.SEARCH) {
            SearchAnswer answer = (SearchAnswer) event.getResult().getTypedAnswer();
            getViewState().showBusinesses(answer.businesses);
        }
    }
}
