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
        if(loggedIn) {
            return true;
        }

        if(!connected) {
            webSocketClient = new WebSocketClient(url);
        }

        new Request(JSONBuilder.newJSONObject().add("action", "register").add("name", username).add("password", password).add("mail", mail).build()).subscribe((data -> {
            if (data.containsKey("token")) {
                session = UUID.fromString((String) data.get("token"));
                loggedIn = true;
            } else if (data.containsKey("error")) {
                loggedIn = false;
                switch ((String) (data.get("error"))) {
                    case "invalid password":
                        System.out.println("Your password is not valid");
                        break;
                    case "invalid email":
                        System.out.println("Your mail is not valid");
                        break;
                    case "username already exists":
                        System.out.println("Your username already exists");
                        break;
                    default:
                        System.out.println("Error while registering");
                }
            }
        }));

        return loggedIn;
    }

    public void logout() {
        new Request(JSONBuilder.newJSONObject().add("action", "logout").build()).subscribe(data -> {});
        loggedIn = false;
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
