package com.github.jannikemmerich.javacrypticclient.terminal.commands.start;

import com.github.jannikemmerich.javacrypticclient.terminal.commands.Command;

public class LoginStartCommand implements Command {

    @Override
    public String executeCommand(String[] args) {
        return null;
    }

    @Override
    public String getHelpMessage() {
        return "Log in to a Cryptic server";
    }
}
