package com.github.jannikemmerich.javacrypticclient;

import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import com.github.jannikemmerich.javacrypticclient.terminal.Terminal;

public class Main {

    private static final String URL_PROD = System.getProperty("url", "wss://ws.cryptic-game.net:443/");
    private static final String URL_TEST = System.getProperty("url", "wss://ws.test.cryptic-game.net:443/");

    public static void main(String[] args) throws Exception {

        if(args.length != 1) {
            System.out.println("Available arguments: prod / test / <websocket url>");
            System.exit(-1);
        }

        setupDiscordPresence();

        String url;

        switch (args[0]) {
            case "prod":
                url = URL_PROD;
                break;
            case "test":
                url = URL_TEST;
                break;
            default:
                url = args[0];
        }

        new Terminal(url);
    }

    private static void setupDiscordPresence() {
        DiscordRPC lib = DiscordRPC.INSTANCE;
        String appID = "596676243144048640";
        lib.Discord_Initialize(appID, null, true, null);
        DiscordRichPresence presence = new DiscordRichPresence();
        presence.startTimestamp = System.currentTimeMillis() / 1000;
        presence.state = "Connecting to Cryptic Game";
        presence.details = "JavaCrypticClient";
        presence.largeImageKey = "cryptic";
        presence.largeImageText = "Cryptic";
        lib.Discord_UpdatePresence(presence);
    }
}
