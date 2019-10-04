package com.github.jannikemmerich.javacrypticclient.terminal;

import com.github.jannikemmerich.javacrypticclient.terminal.commands.*;
import io.netty.channel.Channel;

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

            String[] split = msg.split(" ");
            String command = split[0];
            String[] args = new String[split.length - 1];

            for(int i = 1; i < split.length; i++) {
                args[i-1] = split[i];
            }

            if(!commands.containsKey(command)) {
                System.out.println("Command does not exist");
                continue;
            }

            System.out.println(commands.get(command).executeCommand(args));
        }
    }

    private void addCommands() {
        commands = new HashMap<>();

        commands.put("status", new StatusCommand());
        commands.put("devices", new DevicesCommand());
    }
}
