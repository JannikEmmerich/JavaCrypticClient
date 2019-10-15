package com.github.jannikemmerich.javacrypticclient.terminal.commands;

import club.minnced.discord.rpc.DiscordRPC;

public class ExitCommand implements Command {

    @Override
    public String executeCommand(String[] args) {

        DiscordRPC.INSTANCE.Discord_ClearPresence();

        System.exit(0);

        return null;
    }

    @Override
    public String getHelpMessage() {
        return "Terminates the program";
    }
}
