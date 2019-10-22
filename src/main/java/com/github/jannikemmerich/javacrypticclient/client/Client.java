package com.github.jannikemmerich.javacrypticclient.client;

import com.github.jannikemmerich.javacrypticclient.util.JSONBuilder;

import java.util.UUID;

public class Client {

    private WebSocketClient webSocketClient;

    private boolean connected = false;
    private boolean loggedIn = false;

    private UUID session;

    public Client(String url) {
        webSocketClient = new WebSocketClient(url);
    }

    public boolean login(String username, String password) {
        if(!connected) {
            return false;
        }

        if(loggedIn) {
            return true;
        }

        new Request(JSONBuilder.newJSONObject().add("action", "login").add("username", username).add("password", password).build()).subscribe((data -> {
            if(data.containsKey("token")) {
                session = UUID.fromString((String) data.get("session"));
                loggedIn = true;
            }
        }));

        return loggedIn;
    }

    public boolean register(String username, String password, String mail) {
        if(!connected) {
            return false;
        }

        if(loggedIn) {
            return true;
        }

        new Request(JSONBuilder.newJSONObject().add("action", "register").add("username", username).add("password", password).add("mail", mail).build()).subscribe((data -> {
            if(data.containsKey("token")) {
                session = UUID.fromString((String) data.get("session"));
                loggedIn = true;
            }
        }));

        return loggedIn;
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
}
