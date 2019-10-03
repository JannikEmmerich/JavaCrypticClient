package com.github.jannikemmerich.javacrypticclient.terminal.commands;

public interface Command {

    String executeCommand(String[] args);
    String getHelpMessage();
}
