package com.github.jannikemmerich.javacrypticclient.util;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JSONBuilder {

    public static JSONBuilder newJSONObject() {
        return new JSONBuilder();
    }

    private Map<String, Object> keySet;

    public JSONBuilder() {
        keySet = new HashMap<>();
    }

    public JSONBuilder add(String key, Object value) {
        keySet.put(key, value);
        return this;
    }

    public JSONObject build() {
        return new JSONObject(keySet);
    }
}
