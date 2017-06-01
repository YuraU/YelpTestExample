package com.yura.yelptestexample.api.response.answer;


import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.util.List;
import java.util.Map;

public class SearchAnswer {

    public List<Businesses> businesses;
    public Integer total;
    public Map<String, Object> region;

    public class Businesses implements ClusterItem{
        public String id;
        public String name;
        public String image_url;
        public Boolean is_closed;
        public String url;
        public Integer review_count;
        public Double rating;
        public LatLng coordinates;
        public String price;
        public Map<String, Object> location;
        public String phone;
        public String display_phone;
        public Double distance;
        public List<Map<String, String>> categories;

        @Override
        public LatLng getPosition() {
            return coordinates;
        }

        public String getCategories(){
            String sCategories = "";

            for(Map<String, String> map : categories){
                sCategories = sCategories + map.get("title") + " ";
            }

            return sCategories;
        }
    }
}
