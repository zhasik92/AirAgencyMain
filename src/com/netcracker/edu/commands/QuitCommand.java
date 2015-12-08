package com.netcracker.edu.commands;

import com.netcracker.edu.bobjects.User;
import com.netcracker.edu.session.SecurityContextHolder;

import java.io.IOException;

/**
 * Created by Zhassulan on 04.12.2015.
 */
public class QuitCommand extends AbstractCommand {
    public QuitCommand() {
        super(User.Roles.USER);
    }

    @Override
    public String getName() {
        return "quit";
    }

    @Override
    public int execute(String[] parameters, User user) throws IOException{
        if (user == null) {
            throw new IllegalArgumentException("User can't be null, sign in first");
        }
        execute(parameters);
        return 0;
    }
    @Override
    protected int execute(String[] parameters) throws IOException {
        SecurityContextHolder.removeUserFromSignedUsers();
        SecurityContextHolder.setLoggedUser(null);
        return 0;
    }

    @Override
    public String getHelp() {
        return "quit";
    }
}
