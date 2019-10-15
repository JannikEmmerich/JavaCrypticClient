package com.github.jannikemmerich.javacrypticclient.terminal.commands.start;

import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import com.github.jannikemmerich.javacrypticclient.Main;
import com.github.jannikemmerich.javacrypticclient.client.WebSocketClient;
import com.github.jannikemmerich.javacrypticclient.terminal.Terminal;
import com.github.jannikemmerich.javacrypticclient.terminal.commands.Command;
import com.github.jannikemmerich.javacrypticclient.util.JSONBuilder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoginStartCommand implements Command {

    @Override
    public String executeCommand(String[] args) {
        if(args.length != 1) {
            return "Usage: login <username>";
        }
        String password = getPassword("Please enter your password: ");
        try {
            new WebSocketClient(Main.URL);
        } catch (Exception e) {
            return "Could not connect to the websocket";
        }
        if(login(args[0], password)) {
            Terminal.getInstance().postLogin(args[0]);
            return "Successfully logged in";
        } else {
            return "Error while logging in";
        }
    }

    private boolean login(String username, String password) {
        JSONObject login = JSONBuilder.newJSONObject().add("action", "login").add("name", username).add("password", password).build();
        WebSocketClient.getInstance().getChannel().writeAndFlush(new TextWebSocketFrame(login.toJSONString()));

        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if(WebSocketClient.getInstance().status.equals("waiting")) {
                    WebSocketClient.getInstance().status = "failed";
                }
            }
        }).start();

        while(WebSocketClient.getInstance().status.equals("waiting")) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(WebSocketClient.getInstance().status.equals("success")) {
            setupDiscordRichPresence(username);
            return true;
        }

        return false;
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

    @Override
    public String getHelpMessage() {
        return "Log in to a Cryptic server";
    }

    private void setupDiscordRichPresence(String username) {
        DiscordRichPresence presence = new DiscordRichPresence();
        presence.startTimestamp = System.currentTimeMillis() / 1000;
        presence.state = "Logged in: " + username + "@cryptic-game.net";
        presence.details = "JavaCrypticClient";
        presence.largeImageKey = "cryptic";
        presence.largeImageText = "Cryptic";
        DiscordRPC.INSTANCE.Discord_UpdatePresence(presence);
    }
}
