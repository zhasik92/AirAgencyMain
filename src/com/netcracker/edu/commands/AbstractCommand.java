package com.netcracker.edu.commands;

import com.netcracker.edu.bobjects.User;

import java.io.IOException;
import java.security.AccessControlException;

/**
 * Created by Zhassulan on 23.10.2015.
 */
public abstract class AbstractCommand {
    protected final User.Roles role;

    public abstract String getName();

    public AbstractCommand(User.Roles role) {
        this.role = role;
    }

    public int execute(String[] parameters, User user) throws IOException {
        if (user == null) {
            throw new AccessControlException("User can't be null, sign in first");
        }
        checkAccess(user.role());
        return execute(parameters);
    }

    protected abstract int execute(String[] parameters) throws IOException;

    public abstract String getHelp();

    protected void checkAccess(User.Roles role) {
        if (this.role.compareTo(role) > 0) {
            throw new AccessControlException("acces denied");
        }
    }
}
