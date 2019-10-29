package com.github.jannikemmerich.javacrypticclient.terminal.commands;

import com.github.jannikemmerich.javacrypticclient.terminal.Terminal;

public class LogoutCommand implements Command {

    @Override
    public void execute(String[] args) {
        if (!Terminal.getInstance().getClient().isLoggedIn()) {
            System.out.println("You can not log out right now!");
            return;
        }

        Terminal.getInstance().getClient().logout();
        System.out.println("Logged out");
        Terminal.getInstance().startPrefix();
    }

    @Override
    public String getHelp() {
        return "\t\tLog out from the game";
    }
}
