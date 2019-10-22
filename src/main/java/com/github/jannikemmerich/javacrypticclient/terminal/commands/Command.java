package com.github.jannikemmerich.javacrypticclient.terminal.commands;

public interface Command {

    void execute(String[] args);
    String getHelp();
}
