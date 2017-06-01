package com.yura.yelptestexample.core;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.birbit.android.jobqueue.JobManager;
import com.yura.yelptestexample.YelpApp;
import org.greenrobot.eventbus.EventBus;


public abstract class BaseActivity extends MvpAppCompatActivity {

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}
