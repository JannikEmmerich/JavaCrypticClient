package com.github.jannikemmerich.javacrypticclient.terminal.commands;

import com.github.jannikemmerich.javacrypticclient.terminal.Terminal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DeleteCommand implements Command {

    @Override
    public void execute(String[] args) {
        if (!Terminal.getInstance().getClient().isLoggedIn()) {
            System.out.println("You can not log out right now!");
            return;
        }

        if(!getString("Do you really want to delete your account? Type yes / no: ").equals("yes")) {
            System.out.println("Aborted");
            return;
        }

        Terminal.getInstance().getClient().delete();
        System.out.println("Successfully delete your account");
        Terminal.getInstance().startPrefix();
    }

    @Override
    public String getHelp() {
        return "\t\tDeletes your current user account";
    }

    private static String getString(String prompt) {
        String password = "";
        System.out.print(prompt);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            password = in.readLine();
        }
        catch (IOException e){
            System.out.println("Error trying to read your password!");
            System.exit(1);
        }

        return password;
    }
}
