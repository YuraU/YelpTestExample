package com.yura.yelptestexample.api.response.answer;


import java.util.List;
import java.util.Map;

public class ReviewsAnswer {
    public Integer total;
    public List<Review> reviews;

    public class Review {
        public Integer rating;
        public Map<String, String> user;
        public String text;
        public String time_created;
        public String url;
    }
}
