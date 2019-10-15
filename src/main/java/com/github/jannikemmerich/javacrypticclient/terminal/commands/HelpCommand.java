package com.github.jannikemmerich.javacrypticclient.terminal.commands;

import com.github.jannikemmerich.javacrypticclient.terminal.Terminal;

public class HelpCommand implements Command {

    @Override
    public String executeCommand(String[] args) {
        StringBuilder helpMessage = new StringBuilder();

        for(String commands : Terminal.getInstance().getCommands().keySet()) {
            helpMessage.append(commands).append("\t\t").append(Terminal.getInstance().getCommands().get(commands).getHelpMessage()).append("\n");
        }

        return helpMessage.toString();
    }

    @Override
    public String getHelpMessage() {
        return "Prints all available commands";
    }
}
