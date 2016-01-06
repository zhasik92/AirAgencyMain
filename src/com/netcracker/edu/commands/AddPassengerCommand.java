package com.netcracker.edu.commands;

import com.netcracker.edu.bobjects.Passenger;
import com.netcracker.edu.bobjects.User;
import com.netcracker.edu.dao.DAOFactory;
import com.netcracker.edu.dao.DAObject;
import com.netcracker.edu.util.IdGenerator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.SQLException;

/**
 * Command
 * Created by Zhassulan on 23.10.2015.
 */
public class AddPassengerCommand extends AbstractCommand {
    private static final Logger logger = LogManager.getLogger(AddPassengerCommand.class);
    private DAObject dao = DAOFactory.getDAObject();

    public AddPassengerCommand() {
        super(User.Roles.USER);
    }

    @Override
    public String getName() {
        return "add_passenger";
    }

    @Override
    protected int execute(String[] parameters) throws IOException {
        if (createPassenger(parameters) == null) {
            return -1;
        }
        return 0;
    }

    public Passenger createPassenger(String[] parameters) throws IOException {
        String passportNumber;
        String citizenship;
        String firstName;
        String lastName;
        Date dateOfBirth;
        String email;
        Passenger passenger;
        try {
            if (parameters != null && parameters.length > 0) {
                if (parameters.length != 6) {
                    throw new IllegalArgumentException("six parameters required");
                }
                passportNumber = parameters[0];
                citizenship = parameters[1];
                firstName = parameters[2];
                lastName = parameters[3];
                dateOfBirth = Date.valueOf(parameters[4]);
                email = parameters[5];
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                logger.info("Enter name of City:");
                logger.info("Please enter passport number:");
                passportNumber = br.readLine();
                logger.info("Citizenship:");
                citizenship = br.readLine();
                passenger = dao.findPassengerByPassportNumberAndCitizenship(passportNumber, citizenship);
                if (passenger != null) {
                    logger.warn("passenger already exist");
                    return passenger;
                }
                logger.info("first name:");
                firstName = br.readLine();

                logger.info("last name:");
                lastName = br.readLine();

                logger.info("date of birth(yyyy-[m]m-[d]d):");
                dateOfBirth = Date.valueOf(br.readLine());

                logger.info("email:");
                email = br.readLine();
            }
            passenger = new Passenger(IdGenerator.getInstance().getId(), email, firstName, lastName, dateOfBirth, passportNumber, citizenship);
            dao.addPassenger(passenger);
            logger.info("passenger saved, id = " + passenger.getId().toString());
            return passenger;
        } catch (SQLException sqle) {
            logger.error(sqle);
            return null;
        }
    }

    @Override
    public String getHelp() {
        return "AddPassengerCommand usage: " + getName();
    }
}
