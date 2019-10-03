package com.github.jannikemmerich.javacrypticclient;

import com.github.jannikemmerich.javacrypticclient.util.JSONBuilder;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Terminal {

    public Terminal(Channel channel) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while(true) {
            String msg = reader.readLine();
            if(msg == null) {
                break;
            }

            if("info".equalsIgnoreCase(msg)) {
                channel.writeAndFlush(new TextWebSocketFrame(JSONBuilder.newJSONObject().add("action", "info").build().toJSONString()));
            }
        }
    }
}
