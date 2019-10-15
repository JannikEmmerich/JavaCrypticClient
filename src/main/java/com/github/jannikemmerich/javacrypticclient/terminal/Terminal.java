package com.github.jannikemmerich.javacrypticclient.terminal;

import com.github.jannikemmerich.javacrypticclient.models.DeviceModel;
import com.github.jannikemmerich.javacrypticclient.terminal.commands.Command;
import com.github.jannikemmerich.javacrypticclient.terminal.commands.ExitCommand;
import com.github.jannikemmerich.javacrypticclient.terminal.commands.HelpCommand;
import com.github.jannikemmerich.javacrypticclient.terminal.commands.StatusCommand;
import com.github.jannikemmerich.javacrypticclient.terminal.commands.start.ExitStartCommand;
import com.github.jannikemmerich.javacrypticclient.terminal.commands.start.LoginStartCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

public class Terminal {

    private static Terminal instance;

    private HashMap<String, Command> commands;
    private String prefix;

    private boolean waitForCommand = true;

    private String username;
    private DeviceModel activeDevice;

    public Terminal() {
        instance = this;

        addStartCommands();

        prefix = getStartPrefix();

        initializeTerminal();
    }

    private void initializeTerminal() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (waitForCommand) {
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

    public static Terminal getInstance() {
        return instance;
    }

    public HashMap<String, Command> getCommands() {
        return commands;
    }

    private void addStartCommands() {
      commands = new HashMap<>();

      commands.put("exit", new ExitStartCommand());
      commands.put("login", new LoginStartCommand());
      commands.put("help", new HelpCommand());

    }

    private void addCommands() {
        commands = new HashMap<>();

        commands.put("help", new HelpCommand());
        commands.put("status", new StatusCommand());
        commands.put("exit", new ExitCommand());
    }

    private String getStartPrefix() {
        String username = System.getProperty("user.name");
        String hostname = "localhost";
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException ignored) {}

        return username + "@" + hostname + " $ ";
    }

    public String getUsername() {
        return username;
    }

    public DeviceModel getActiveDevice() {
        return activeDevice;
    }

    public void setActiveDevice(DeviceModel activeDevice) {
        this.activeDevice = activeDevice;
    }

    public void postLogin(String username) {
        addCommands();

        this.username = username;
        activeDevice = DeviceModel.getFirstDevice();

        prefix = username + "@" + activeDevice.getHostname() + " $ ";
    }
}
