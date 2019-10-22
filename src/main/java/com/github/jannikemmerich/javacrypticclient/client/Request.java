package com.github.jannikemmerich.javacrypticclient.client;

import com.github.jannikemmerich.javacrypticclient.terminal.Terminal;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.json.simple.JSONObject;

public class Request {

    public static Request activeRequest;

    private JSONObject data;

    public Request(JSONObject data) {
        while (activeRequest != null) {
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Terminal.getInstance().getClient().getWebSocketClient().getChannel().writeAndFlush(new TextWebSocketFrame(data.toJSONString()));
    }

    public void handle(JSONObject json) {
        if(json.containsKey("tag")) {
            data = (JSONObject) json.get("data");
        } else {
            data = json;
        }
    }

    public void subscribe(RequestExecutor executor) {

        activeRequest = this;

        int k = 0;
        while(data == null && k < 40) {
            try {
                Thread.sleep(500);
                k++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        activeRequest = null;

        if(data == null) data = new JSONObject();

        executor.run(data);

    }

    public interface RequestExecutor {
        void run(JSONObject data);
    }
}
