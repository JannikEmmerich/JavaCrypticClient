package com.github.jannikemmerich.javacrypticclient.terminal;

import com.github.jannikemmerich.javacrypticclient.client.Client;
import com.github.jannikemmerich.javacrypticclient.terminal.commands.*;
import com.github.jannikemmerich.javacrypticclient.util.Device;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Terminal {

    private static Terminal instance;

    private Client client;
    private String prefix;
    private HashMap<String, Command> commands;

    private UUID user;
    private Device activeDevice;

    public Terminal(String url) {
        instance = this;

        startPrefix();
        addCommands();

        client = new Client(url);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.print(prefix);

            String msg = null;
            try {
                msg = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(msg == null) {
                break;
            }

            String[] split = msg.split(" ");
            String command = split[0];
            String[] args = new String[split.length - 1];

            System.arraycopy(split, 1, args, 0, split.length - 1);

            if(!commands.containsKey(command)) {
                System.out.println("Command could not be found.\nType `help` for a list of commands.");
                continue;
            }

            commands.get(command).execute(args);
        }
    }

    public static Terminal getInstance() {
        return instance;
    }

    public Client getClient() {
        return client;
    }

    private void addCommands() {
        commands = new HashMap<>();
        commands.put("help", new HelpCommand());
        commands.put("login", new LoginCommand());
        commands.put("register", new RegisterCommand());
        commands.put("logout", new LogoutCommand());
        commands.put("exit", new ExitCommand());
        commands.put("delete", new DeleteCommand());
    }

    public void startPrefix() {
        String username = System.getProperty("user.name");
        String hostname = "localhost";
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException ignored) {}

        prefix = username + "@" + hostname + " $ ";
    }

    public void loggedIn(String username) {
        client.info().subscribe((result) -> {
            user = UUID.fromString((String) result.get("uuid"));
        });

        List<Device> devices = Device.getDevices();
        if(devices.size() == 0) {
            devices.add(Device.getStarterDevice());
        }

        activeDevice = devices.get(0);

        prefix = username + "@" + activeDevice.getName() + " $ ";
    }

    public HashMap<String, Command> getCommands() {
        return commands;
    }
}
