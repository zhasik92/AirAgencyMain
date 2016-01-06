package com.netcracker.edu.commands;

import com.netcracker.edu.bobjects.User;
import com.netcracker.edu.dao.DAOFactory;
import com.netcracker.edu.dao.DAObject;
import com.netcracker.edu.session.SecurityContextHolder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.AccessControlException;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Created by Zhassulan on 19.11.2015.
 */
public class SignInCommand extends AbstractCommand {
    private static final Logger logger = LogManager.getLogger(SignInCommand.class);
    private DAObject dao = DAOFactory.getDAObject();

    public SignInCommand() {
        super(User.Roles.USER);
    }

    @Override
    public String getName() {
        return "sign_in";
    }

    @Override
    public int execute(String[] parameters, User user) throws IOException {
        if (user != null) {
            throw new AccessControlException("quit first");
        }
        return execute(parameters);
    }

    @Override
    protected int execute(String[] parameters) throws IOException {
        User user;
        String login;
        char[] password;
        if (parameters == null || parameters.length < 1) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            logger.info("enter login");
            login = br.readLine();
            logger.info("enter password");
            password = br.readLine().toCharArray();
        } else {
            if (parameters.length != 2) {
                throw new IllegalArgumentException("required 2 parameters");
            }
            login = parameters[0];
            password = parameters[1].toCharArray();
        }
        try {
            user = dao.findUserByLogin(login);
            if (user == null || !Arrays.equals(user.getPassword(), password)) {
                logger.warn("Login and password are incorrect");
                return 1;
            }
            SecurityContextHolder.setLoggedUser(user);
            logger.info("signed in");
            return 0;
        } catch (SQLException sqle) {
            logger.error(sqle);
            return -1;
        }
    }

    @Override
    public String getHelp() {
        return "SignInCommand usage:" + "\"" + getName() + "\"" + " or \"" + getName() + "login password\"";
    }
}
