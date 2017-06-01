package com.yura.yelptestexample.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yura.yelptestexample.R;
import com.yura.yelptestexample.adapter.ReviewAdapter;
import com.yura.yelptestexample.api.response.answer.ReviewsAnswer;
import com.yura.yelptestexample.core.BaseActivity;
import com.yura.yelptestexample.databinding.ActivityRestaurantBinding;
import com.yura.yelptestexample.event.httpEvent.BaseHttpEvent;
import com.yura.yelptestexample.mvp.presenter.RestaurantPresenter;
import com.yura.yelptestexample.mvp.view.RestaurantView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

public class RestaurantActivity extends BaseActivity implements RestaurantView {

    @InjectPresenter
    RestaurantPresenter restaurantPresenter;
    ReviewAdapter adapter;

    ActivityRestaurantBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_restaurant);

        Intent intent = getIntent();
        HashMap<String, String> map = (HashMap<String, String>)intent.getSerializableExtra("map");

        restaurantPresenter.onCreate(map);

        adapterSetup();
    }

    private void adapterSetup() {
        if (adapter == null) {
            adapter = new ReviewAdapter(this);
        }

        binding.reviews.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.reviews.setAdapter(adapter);
    }

    @Override
    public void fillRestaurantInfo(HashMap<String, String> map) {
        binding.tvName.setText(map.get("name"));
        binding.tvCategorys.setText(map.get("categories"));
        binding.tvPhone.setText(map.get("phone"));

        binding.ratingBar.setRating(Double.valueOf(map.get("rating")).floatValue());
    }

    @Override
    public void showImage(String url) {
        Picasso.with(getApplicationContext())
                .load(url)
                .into(binding.ivPhoto);
    }

    @Override
    public void fillReviews(List<ReviewsAnswer.Review> list) {
        adapter.setupItems(list);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BaseHttpEvent event) {
        restaurantPresenter.messageEvent(event);
    }
}
