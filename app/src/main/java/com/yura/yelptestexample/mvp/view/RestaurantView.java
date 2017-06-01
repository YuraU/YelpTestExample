package com.yura.yelptestexample.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.yura.yelptestexample.api.response.answer.ReviewsAnswer;

import java.util.HashMap;
import java.util.List;

public interface RestaurantView extends MvpView {

    void fillRestaurantInfo(HashMap<String, String> map);
    void showImage(String url);
    void fillReviews(List<ReviewsAnswer.Review> list);
}
