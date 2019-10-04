package com.github.jannikemmerich.javacrypticclient.client;

import com.github.jannikemmerich.javacrypticclient.util.JSONBuilder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.json.simple.JSONObject;

import java.util.*;

public class Request {

    public static Map<UUID, JSONObject> requests = new HashMap<>();

    private UUID tag;

    private Request(UUID tag, String microService, List<String> endpoint, JSONObject data) {
        this.tag = tag;

        JSONBuilder request = JSONBuilder.newJSONObject()
                .add("tag", tag.toString())
                .add("ms", microService)
                .add("endpoint", endpoint)
                .add("data", data);

        requests.put(tag, null);
        WebSocketClient.getInstance().getChannel().writeAndFlush(new TextWebSocketFrame(request.build().toJSONString()));
    }

    public static Request create(String microService, List<String> endpoint, JSONObject data) {
        return new Request(UUID.randomUUID(), microService, endpoint, data);
    }

    public JSONObject waitForAnswer() {
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {

            }

            if(requests.get(tag) == null) {
                requests.remove(tag);
                requests.put(tag, JSONBuilder.newJSONObject().add("error", "timeout").build());
            }
        });

        t.start();

        while(requests.get(tag) == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        t.interrupt();
        return requests.remove(tag);
    }
}
