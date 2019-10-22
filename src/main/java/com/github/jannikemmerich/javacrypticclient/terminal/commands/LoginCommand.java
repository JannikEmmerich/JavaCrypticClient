package com.github.jannikemmerich.javacrypticclient.terminal.commands;

import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import com.github.jannikemmerich.javacrypticclient.client.Request;
import com.github.jannikemmerich.javacrypticclient.terminal.Terminal;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoginCommand implements Command {

    @Override
    public void execute(String[] args) {
        if(Terminal.getInstance().getClient().isConnected() && !Terminal.getInstance().getClient().isLoggedIn()) {
            System.out.println("You can not log in right now!");
            return;
        }

        if(args.length != 1) {
            System.out.println("Usage: login <username>");
            return;
        }
        String password = getPassword("Please enter your password: ");
        if(Terminal.getInstance().getClient().login(args[0], password)) {
            System.out.println("Successfully logged in");

            DiscordRichPresence presence = new DiscordRichPresence();
            presence.startTimestamp = System.currentTimeMillis() / 1000;
            presence.state = "Logged In: " + args[0];
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

        } else {
            System.out.println("Error while logging in");
        }
    }

    @Override
    public String getHelp() {
        return "Send login request to the server";
    }

    private static String getPassword(String prompt) {

        Console console = System.console();
        String password = "";

        if(console != null) {
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
