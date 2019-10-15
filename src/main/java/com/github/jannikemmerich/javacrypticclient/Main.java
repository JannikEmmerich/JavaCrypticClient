package com.github.jannikemmerich.javacrypticclient;

import com.github.jannikemmerich.javacrypticclient.client.WebSocketClient;
import com.github.jannikemmerich.javacrypticclient.terminal.Terminal;

import java.net.URI;
import java.util.Scanner;

public class Main {

    private static final String URL = System.getProperty("url", "wss://ws.test.cryptic-game.net:443/");

    public static void main(String[] args) throws Exception {
        /*Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter your username: ");
        String username = scanner.nextLine();

        String password;
        if (System.console() != null) {
            password = new String(System.console().readPassword("Please enter your password:"));
        } else {
            System.out.print("Please enter your password: ");
            password = scanner.nextLine();
        }

        URI uri = new URI(URL);
        new Main(username, password, uri);*/

        new Terminal();
    }

    Main(String username, String password, URI uri) throws Exception {
        new WebSocketClient(username, password, uri);
    }
}
