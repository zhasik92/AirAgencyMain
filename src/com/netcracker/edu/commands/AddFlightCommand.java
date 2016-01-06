package com.netcracker.edu.commands;

import com.netcracker.edu.bobjects.Flight;
import com.netcracker.edu.bobjects.User;
import com.netcracker.edu.dao.DAOFactory;
import com.netcracker.edu.dao.DAObject;
import com.netcracker.edu.util.IdGenerator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.sql.Time;

/**
 * Command
 * Created by Zhassulan on 03.11.2015.
 */
public class AddFlightCommand extends AbstractCommand {
    private static final Logger logger = LogManager.getLogger(AddFlightCommand.class);
    DAObject dao = DAOFactory.getDAObject();

    public AddFlightCommand() {
        super(User.Roles.ADMIN);
    }

    @Override
    public String getName() {
        return "add_flight";
    }

    @Override
    protected int execute(String[] parameters) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String from;
        String to;
        Time departureTime;
        Time arrivalTime;
        String airplane;
        double price;
        if (parameters == null || parameters.length == 0) {
            logger.info("From:");
            from = br.readLine();
            logger.info("to:");
            to = br.readLine();
            logger.info("Departure time(hh:mm:ss):");
            departureTime = Time.valueOf(br.readLine());
            logger.info("Arrival time(hh:mm:ss):");
            arrivalTime = Time.valueOf(br.readLine());
            logger.info("Write Airplane type:");
            airplane = br.readLine();
            logger.info("Set price:");
            price = Double.parseDouble(br.readLine());
        } else {
            if (parameters.length != 6) {
                throw new IllegalArgumentException("required 6 args");
            }
            from = parameters[0];
            to = parameters[1];
            departureTime = Time.valueOf(parameters[2]);
            arrivalTime = Time.valueOf(parameters[3]);
            airplane = parameters[4];
            price = Double.parseDouble(parameters[5]);
        }
        try {
            if (dao.findCityByName(from) == null) {
                logger.warn("city is not found");
                return 1;
            }
            if (dao.findCityByName(to) == null) {
                logger.warn("city is not found");
                return 1;
            }
            if (dao.findAirplaneByName(airplane) == null) {
                logger.warn("No such airplane type");
                return 1;
            }

            Flight flight = new Flight(IdGenerator.getInstance().getId(), from, to, departureTime, arrivalTime, airplane, price);
            dao.addFlight(flight);
            logger.trace("flight added, id = " + flight.getId().toString());
            return 0;
        } catch (SQLException sqle) {
            logger.error(sqle);
            return -1;
        }
    }

    @Override
    public String getHelp() {
        return "AddFlightCommand usage: " + getName();
    }
}
