package com.github.jannikemmerich.javacrypticclient;

import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import com.github.jannikemmerich.javacrypticclient.terminal.Terminal;

public class Main {

    public static final String URL = System.getProperty("url", "wss://ws.cryptic-game.net:443/");

    public static void main(String[] args) throws Exception {
        setupDiscordPresence();
        new Terminal();
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
