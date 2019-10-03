package com.github.jannikemmerich.javacrypticclient.terminal;

import com.github.jannikemmerich.javacrypticclient.terminal.commands.Command;
import com.github.jannikemmerich.javacrypticclient.terminal.commands.StatusCommand;
import com.github.jannikemmerich.javacrypticclient.util.JSONBuilder;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Terminal {

    private HashMap<String, Command> commands;

    public Terminal(Channel channel) throws IOException {

        addCommands();

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

    private void addCommands() {
        commands = new HashMap<>();

        commands.put("status", new StatusCommand());
    }
}
