package com.github.jannikemmerich.javacrypticclient.terminal.commands.start;

import com.github.jannikemmerich.javacrypticclient.terminal.commands.Command;

public class ExitStartCommand implements Command {

    @Override
    public String executeCommand(String[] args) {
        System.exit(0);
        return null;
    }

    @Override
    public String getHelpMessage() {
        return "Terminates the program";
    }
}
