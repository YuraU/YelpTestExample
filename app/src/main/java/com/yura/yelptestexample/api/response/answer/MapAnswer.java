package com.yura.yelptestexample.api.response.answer;

import java.util.HashMap;
import java.util.Map;


public class MapAnswer extends HashMap<String, Object> {

    public MapAnswer(){}

    public MapAnswer(Map<String, Object> answer) {
        super(answer);
    }

    @Override
    public Object get(Object key) {
        Object o = super.get(key);
        if(o == null)
            return "";

        return o;
    }
}
