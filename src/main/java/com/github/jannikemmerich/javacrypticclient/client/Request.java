package com.github.jannikemmerich.javacrypticclient.client;

import org.json.simple.JSONObject;

import java.util.UUID;

public class Request {

    private UUID tag;

    public Request(JSONObject data) {

    }

    public void subscribe(RequestExecutor executor) {
        if(tag != null) {

        } else {

        }
    }

    public interface RequestExecutor {
        void run(JSONObject data);
    }
}
