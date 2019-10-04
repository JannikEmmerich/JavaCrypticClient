package com.github.jannikemmerich.javacrypticclient.terminal.commands;

import com.github.jannikemmerich.javacrypticclient.client.Request;
import org.json.simple.JSONObject;

import java.util.Arrays;

public class DevicesCommand implements Command {

    @Override
    public String executeCommand(String[] args) {

        return Request.create("device", Arrays.asList("device", "all"), new JSONObject()).waitForAnswer().toJSONString();
    }

    @Override
    public String getHelpMessage() {
        return null;
    }
}
