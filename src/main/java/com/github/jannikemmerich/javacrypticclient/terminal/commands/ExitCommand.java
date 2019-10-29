package com.github.jannikemmerich.javacrypticclient.terminal.commands;

import com.github.jannikemmerich.javacrypticclient.terminal.Terminal;

public class ExitCommand implements Command {

    @Override
    public void execute(String[] args) {
        if (Terminal.getInstance().getClient().isLoggedIn()) {
            Terminal.getInstance().getClient().logout();
        }

        System.exit(0);
    }

    @Override
    public String getHelp() {
        return "\t\tExit the client";
    }
}
