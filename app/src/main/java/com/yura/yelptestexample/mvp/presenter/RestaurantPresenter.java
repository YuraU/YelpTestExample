package com.yura.yelptestexample.mvp.presenter;


import com.arellomobile.mvp.InjectViewState;
import com.yura.yelptestexample.YelpApp;
import com.yura.yelptestexample.api.response.answer.ReviewsAnswer;
import com.yura.yelptestexample.event.httpEvent.BaseHttpEvent;
import com.yura.yelptestexample.job.http.ReviewsJob;
import com.yura.yelptestexample.mvp.view.RestaurantView;

import java.util.HashMap;

@InjectViewState
public class RestaurantPresenter extends BasePresenter<RestaurantView>{

    HashMap<String, String> restaurantInfo;

    public void onCreate(HashMap<String, String> map){
        restaurantInfo = map;
        getViewState().fillRestaurantInfo(restaurantInfo);
        getViewState().showImage(restaurantInfo.get("image"));

        YelpApp.getInstance().getJobManager().addJobInBackground(new ReviewsJob(restaurantInfo.get("id")));
    }

    public void messageEvent(BaseHttpEvent event){
        if (event.type == BaseHttpEvent.REVIEWS) {
            ReviewsAnswer answer = (ReviewsAnswer)event.getResult().getTypedAnswer();

            getViewState().fillReviews(answer.reviews);
        }
    }

}
