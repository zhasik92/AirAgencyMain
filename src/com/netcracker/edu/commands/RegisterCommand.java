package com.netcracker.edu.commands;

import com.netcracker.edu.bobjects.User;
import com.netcracker.edu.dao.DAObject;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Zhassulan on 19.11.2015.
 */
public class RegisterCommand extends AbstractCommand {
    private static final Logger logger = LogManager.getLogger(RegisterCommand.class);

    public RegisterCommand() {
        super(User.Roles.USER);
    }

    @Override
    public String getName() {
        return "register";
    }

    @Override
    public int execute(String[] parameters, User user) throws IOException {
        return execute(parameters);
    }

    @Override
    protected int execute(String[] parameters) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        DAObject dao = DAObject.getInstance();

        logger.info("Enter login: ");
        String login = br.readLine();
        User user = dao.findUserByLogin(login);
        if (user != null) {
            logger.warn("login already registered");
            return 0;
        }

        logger.info("enter password: ");
        char[] password = br.readLine().toCharArray();

        user = createUser(login, password);
        dao.addUser(user);
        logger.info("successfully registered");
        return 0;
    }

    public User createUser(String login, char[] password) {
        User user = new User(login, password);
        return user;
    }

    @Override
    public String getHelp() {
        return getName() + " login password";
    }
}
