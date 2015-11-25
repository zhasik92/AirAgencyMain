package com.netcracker.edu.commands;

import com.netcracker.edu.bobjects.User;
import com.netcracker.edu.dao.DAObject;
import com.netcracker.edu.session.SecurityContextHolder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Created by Zhassulan on 19.11.2015.
 */
public class SignInCommand extends AbstractCommand {
    private static final Logger logger = LogManager.getLogger(SignInCommand.class);
    private DAObject dao = DAObject.getInstance();

    public SignInCommand() {
        super(User.Roles.USER);
    }

    @Override
    public String getName() {
        return "sign_in";
    }

    @Override
    public int execute(String[] parameters, User user) throws IOException {
        return execute(parameters);
    }

    @Override
    protected int execute(String[] parameters) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        logger.info("enter login");
        String login = br.readLine();
        User user = dao.findUserByLogin(login);
        logger.info("enter password");
        char[] password = br.readLine().toCharArray();
        if (user == null || !Arrays.equals(user.getPassword(), password)) {
            logger.warn("Login and password are incorrect");
            return 1;
        }
        SecurityContextHolder.setLoggedUser(user);
        logger.info("logged in");
        return 0;
    }

    @Override
    public String getHelp() {
        return getName();
    }
}
