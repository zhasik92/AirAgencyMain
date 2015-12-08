package com.netcracker.edu.commands;

import com.netcracker.edu.bobjects.Flight;
import com.netcracker.edu.bobjects.User;
import com.netcracker.edu.dao.DAObject;
import com.netcracker.edu.dao.DAObjectFromSerializedStorage;
import com.netcracker.edu.util.IdGenerator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Time;

/**
 * Created by Zhassulan on 03.11.2015.
 */
public class AddFlightCommand extends AbstractCommand {
    private static final Logger logger = LogManager.getLogger(AddFlightCommand.class);

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
        DAObject dao = DAObjectFromSerializedStorage.getInstance();

        logger.info("From:");
        String from = br.readLine();
        if (dao.findCityByName(from) == null) {
            logger.warn("city is not found");
            return 1;
        }

        logger.info("to:");
        String to = br.readLine();
        if (dao.findCityByName(to) == null) {
            logger.warn("city is not found");
            return 1;
        }
        logger.info("Departure time(hh:mm:ss):");
        Time departureTime = Time.valueOf(br.readLine());

        logger.info("Arrival time(hh:mm:ss):");
        Time arrivalTime = Time.valueOf(br.readLine());
        logger.info("Write Airplane type:");
        String airplane = br.readLine();
        if (dao.findAirplaneByName(airplane) == null) {
            logger.warn("No such airplane type");
            return 1;
        }

        logger.info("Set price:");
        double price = Double.parseDouble(br.readLine());

        Flight flight = new Flight(IdGenerator.getInstance().getId(), from, to, departureTime, arrivalTime, airplane, price);
        dao.addFlight(flight);
        logger.trace("flight added, id = " + flight.getId().toString());
        return 0;
    }

    @Override
    public String getHelp() {
        return "AddFlightCommand usage: " + getName();
    }
}
