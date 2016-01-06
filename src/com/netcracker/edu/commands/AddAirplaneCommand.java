package com.netcracker.edu.commands;

import com.netcracker.edu.bobjects.Airplane;
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
 * Created by Zhassulan on 03.11.2015.
 */
public class AddAirplaneCommand extends AbstractCommand {
    private static final Logger logger = LogManager.getLogger(AddAirplaneCommand.class);
    private static DAObject dao = DAOFactory.getDAObject();

    public AddAirplaneCommand() {
        super(User.Roles.ADMIN);
    }

    @Override
    public String getName() {
        return "add_airplane";
    }

    @Override
    protected int execute(String[] parameters) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String airplaneName;
        Integer capacity;
        if (parameters == null || parameters.length < 1) {
            logger.info("Enter name of Airplane:");
            airplaneName = br.readLine();
            logger.info("write capacity");
            capacity = Integer.parseInt(br.readLine());
        } else {
            if (parameters.length != 2) {
                logger.error("illegal arguments");
                throw new IllegalArgumentException();
            }
            airplaneName = parameters[0];
            capacity = Integer.parseInt(parameters[1]);
        }
        try {
            Airplane airplane = dao.findAirplaneByName(airplaneName);
            if (airplane != null) {
                logger.warn("Airplane already exist");
                return 1;
            }

            airplane = new Airplane(airplaneName, capacity);
            dao.addAirplane(airplane);
            logger.trace("airplane added");
            return 0;
        } catch (SQLException sqle) {
            logger.error(sqle);
            return -1;
        }
    }

    @Override
    public String getHelp() {
        return "AddAirplaneCommand usage: " + getName();
    }
}
