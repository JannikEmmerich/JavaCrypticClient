package com.github.jannikemmerich.javacrypticclient.terminal.commands;

import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import com.github.jannikemmerich.javacrypticclient.terminal.Terminal;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

public class RegisterCommand implements Command {

    @Override
    public void execute(String[] args) {
        if (Terminal.getInstance().getClient().isLoggedIn()) {
            System.out.println("You can not register right now!");
            return;
        }

        if (args.length != 0) {
            System.out.println("Usage: register");
            return;
        }

        String username = getString("Please enter your username: ");
        String mail = getString("Please enter you mail address: ");
        String password = getPassword("Please enter your password: ");
        if(!password.equals(getPassword("Please enter your password again: "))) {
            System.out.println("The second password does not match the first password");
            return;
        }

        if (Terminal.getInstance().getClient().register(username, password, mail)) {
            System.out.println("Successfully registered and logged in");
            Terminal.getInstance().loggedIn(username);

            DiscordRichPresence presence = new DiscordRichPresence();
            presence.startTimestamp = System.currentTimeMillis() / 1000;
            presence.state = "Logged In: " + username;
            presence.details = "JavaCrypticClient";
            presence.largeImageKey = "cryptic";
            presence.largeImageText = "Cryptic";

            DiscordRPC.INSTANCE.Discord_UpdatePresence(presence);

            new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(20000);
                        Terminal.getInstance().getClient().info().subscribe((data) -> {});
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }

    @Override
    public String getHelp() {
        return "\tRegister a new account";
    }

    private static String getString(String prompt) {
        String password = "";
        System.out.print(prompt);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            password = in.readLine();
        }
        catch (IOException e){
            System.out.println("Error trying to read your password!");
            System.exit(1);
        }

        return password;
    }

    private static String getPassword(String prompt) {

        Console console = System.console();
        String password = "";

        if (console != null) {
            password = new String(console.readPassword(prompt));
        } else {
            System.out.print(prompt);
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            try {
                password = in.readLine();
            }
            catch (IOException e){
                System.out.println("Error trying to read your password!");
                System.exit(1);
            }
        }

        return password;
    }
}
