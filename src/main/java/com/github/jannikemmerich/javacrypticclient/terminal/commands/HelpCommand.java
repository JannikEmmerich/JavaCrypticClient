package com.github.jannikemmerich.javacrypticclient.terminal.commands;

import com.github.jannikemmerich.javacrypticclient.terminal.Terminal;

public class HelpCommand implements Command {

    @Override
    public void execute(String[] args) {
        StringBuilder helpMessage = new StringBuilder();

        for(String commands : Terminal.getInstance().getCommands().keySet()) {
            helpMessage.append(commands).append(Terminal.getInstance().getCommands().get(commands).getHelp()).append("\n");
        }

        System.out.println(helpMessage.toString());
    }

    @Override
    public String getHelp() {
        return "\t\tPrints all available commands";
    }
}