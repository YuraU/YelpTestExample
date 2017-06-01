package com.yura.yelptestexample.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yura.yelptestexample.R;
import com.yura.yelptestexample.api.response.answer.ReviewsAnswer;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.DateAdapterViewHolder>{

    private static Context context;
    private List<ReviewsAnswer.Review> mTasks = new ArrayList<>();;

    public ReviewAdapter(Context context) {
        this.context = context;
    }


    @Override
    public DateAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new DateAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DateAdapterViewHolder holder, int position) {
        holder.bind(mTasks.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mTasks == null ? 0 : mTasks.size();
    }


    public void setupItems(List<ReviewsAnswer.Review> tasks) {
        this.mTasks.clear();
        this.mTasks.addAll(tasks);
        notifyDataSetChanged();
    }

    public void clear(){
        this.mTasks.clear();
        notifyDataSetChanged();
    }

    public List<ReviewsAnswer.Review> getReviews() {
        return mTasks;
    }

    public class DateAdapterViewHolder extends RecyclerView.ViewHolder{
        public ImageView photo;
        public TextView name;
        public RatingBar rating;
        public TextView tvReview;

        public DateAdapterViewHolder(View view){
            super(view);
            this.photo = (ImageView) view.findViewById(R.id.ivPhoto);
            this.name = (TextView) view.findViewById(R.id.tvName);
            this.rating = (RatingBar) view.findViewById(R.id.rating);
            this.tvReview = (TextView) view.findViewById(R.id.tvReview);
        }

        public void bind(final ReviewsAnswer.Review review, final int position) {
            name.setText(review.user.get("name"));
            rating.setRating(review.rating);
            tvReview.setText(review.text);

            Picasso.with(context)
                    .load(review.user.get("image_url"))
                    .into(photo);


        }
    }
}