package com.yura.yelptestexample.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.yura.yelptestexample.api.response.answer.SearchAnswer;

import java.util.List;

public interface MapView extends MvpView {

    void showBusinesses(List<SearchAnswer.Businesses> businesses);
}
