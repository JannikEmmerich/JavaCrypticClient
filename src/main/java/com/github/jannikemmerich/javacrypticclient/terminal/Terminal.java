package com.github.jannikemmerich.javacrypticclient.terminal;

import com.github.jannikemmerich.javacrypticclient.client.Request;
import com.github.jannikemmerich.javacrypticclient.terminal.commands.Command;
import com.github.jannikemmerich.javacrypticclient.terminal.commands.ExitCommand;
import com.github.jannikemmerich.javacrypticclient.terminal.commands.StatusCommand;
import io.netty.channel.Channel;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Terminal {

    private HashMap<String, Command> commands;

    public Terminal(Channel channel, String username) throws IOException {

        addCommands();

        JSONObject devicesRequest = Request.create("device", Arrays.asList("device", "all"), new JSONObject()).waitForAnswer();
        List<JSONObject> devices = (List<JSONObject>) devicesRequest.get("devices");

        JSONObject currentDevice = devices.get(0);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while(true) {
            System.out.print(username + "@" + currentDevice.get("name") + " $ ");

            String msg = reader.readLine();
            if(msg == null) {
                break;
            }

            String[] split = msg.split(" ");
            String command = split[0];
            String[] args = new String[split.length - 1];

            for(int i = 1; i < split.length; i++) {
                args[i-1] = split[i];
            }

            if(!commands.containsKey(command)) {
                System.out.println("Command could not be found.\nType `help` for a list of commands.");
                continue;
            }

            System.out.println(commands.get(command).executeCommand(args));
        }
    }

    private void addCommands() {
        commands = new HashMap<>();

        commands.put("status", new StatusCommand());
        commands.put("exit", new ExitCommand());
    }
}
