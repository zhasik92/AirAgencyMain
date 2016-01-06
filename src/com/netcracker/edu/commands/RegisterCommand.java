package com.netcracker.edu.commands;

import com.netcracker.edu.bobjects.User;
import com.netcracker.edu.dao.DAOFactory;
import com.netcracker.edu.dao.DAObject;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

/**
 * Command
 * Created by Zhassulan on 19.11.2015.
 */
public class RegisterCommand extends AbstractCommand {
    private static final Logger logger = LogManager.getLogger(RegisterCommand.class);
    private static DAObject dao = DAOFactory.getDAObject();

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
        String login;
        char[] password;
        if (parameters == null || parameters.length < 1) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            logger.info("Enter login: ");
            login = br.readLine();
            logger.info("enter password: ");
            password = br.readLine().toCharArray();
        } else {
            if (parameters.length != 2) {
                throw new IllegalArgumentException("required 2 parameters");
            }
            login = parameters[0];
            password = parameters[1].toCharArray();
        }
        try {
            User user = dao.findUserByLogin(login);
            if (user != null) {
                logger.warn("login already registered");
                return 1;
            }

            user = createUser(login, password);
            dao.addUser(user);
            logger.info("successfully registered");
            return 0;
        } catch (SQLException sqle) {
            logger.error(sqle);
            return -1;
        }
    }

    public User createUser(String login, char[] password) {
        return new User(login, password);
    }

    @Override
    public String getHelp() {
        return "\"" + getName() + "\"" + " or " + "\"" + getName() + " login password\"";
    }
}
