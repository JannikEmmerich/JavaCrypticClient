package com.github.jannikemmerich.javacrypticclient.client;

import com.github.jannikemmerich.javacrypticclient.util.JSONBuilder;

import java.util.UUID;

public class Client {

    private WebSocketClient webSocketClient;

    private boolean connected = false;
    private boolean loggedIn = false;

    private String url;

    private UUID session;

    public Client(String url) {
        this.url = url;
    }

    public boolean login(String username, String password) {
        if(loggedIn) {
            return true;
        }

        if(!connected) {
            webSocketClient = new WebSocketClient(url);
        }

        new Request(JSONBuilder.newJSONObject().add("action", "login").add("name", username).add("password", password).build()).subscribe((data -> {
            if(data.containsKey("token")) {
                session = UUID.fromString((String) data.get("token"));
                loggedIn = true;
            }
        }));

        return loggedIn;
    }

    public Request info() {
        return new Request(JSONBuilder.newJSONObject().add("action", "status").build());
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
                session = UUID.fromString((String) data.get("token"));
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

    public WebSocketClient getWebSocketClient() {
        return webSocketClient;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
