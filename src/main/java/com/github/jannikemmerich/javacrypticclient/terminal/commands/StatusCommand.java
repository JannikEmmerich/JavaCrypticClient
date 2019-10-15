package com.github.jannikemmerich.javacrypticclient.terminal.commands;

import com.github.jannikemmerich.javacrypticclient.client.WebSocketClient;
import com.github.jannikemmerich.javacrypticclient.util.JSONBuilder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.json.simple.JSONObject;

public class StatusCommand implements Command {

    @Override
    public String executeCommand(String[] args) {
        JSONObject status = JSONBuilder.newJSONObject().add("action", "info").build();
        WebSocketClient.getInstance().getChannel().writeAndFlush(new TextWebSocketFrame(status.toJSONString()));

        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if(!WebSocketClient.getInstance().unhandledPackets.containsKey("info"))
                    WebSocketClient.getInstance().unhandledPackets.put("info", JSONBuilder.newJSONObject().add("error", "timeout").build());
            }
        }).start();

        while(!WebSocketClient.getInstance().unhandledPackets.containsKey("info")) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        JSONObject statusData = WebSocketClient.getInstance().unhandledPackets.remove("info");

        if(statusData.containsKey("online")) {
            return "Online members: " + statusData.get("online");
        }

        return "Online members: 0";
    }

    @Override
    public String getHelpMessage() {
        return "Get number of online members";
    }
}
