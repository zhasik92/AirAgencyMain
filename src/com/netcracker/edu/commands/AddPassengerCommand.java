package com.netcracker.edu.commands;

import com.netcracker.edu.bobjects.Passenger;
import com.netcracker.edu.dao.DAObject;
import com.netcracker.edu.util.IdGenerator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;

/**
 * Created by Zhassulan on 23.10.2015.
 */
public class AddPassengerCommand extends AbstractCommand {
    private static final Logger logger = LogManager.getLogger(AddPassengerCommand.class);

    @Override
    public String getName() {
        return "add_passenger";
    }

    @Override
    public int execute(String[] parameters) throws IOException {
        DAObject.getInstance().addPassenger(createPassenger());
        logger.info("passenger added");
        return 0;
    }

    public Passenger createPassenger() throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            logger.info("Please enter passport number:");
            String passportNumber = br.readLine();

            logger.info("Citizenship:");
            String citizenship = br.readLine();

            DAObject dao = DAObject.getInstance();
            Passenger passenger = dao.findPassengerByPassportNumberAndCitizenship(passportNumber, citizenship);
            if (passenger != null) {
                logger.warn("passenger already exist");
                return passenger;
            }

            logger.info("first name:");
            String firstName = br.readLine();

            logger.info("last name:");
            String lastName = br.readLine();

            logger.info("date of birth(yyyy-[m]m-[d]d):");
            Date dateOfBirth = Date.valueOf(br.readLine());

            logger.info("email:");
            String email = br.readLine();

            passenger = new Passenger(IdGenerator.getInstance().getId(), email, firstName, lastName, dateOfBirth, passportNumber, citizenship);
            logger.info("passenger created, id = " + passenger.getId().toString());
            return passenger;
        } catch (IOException ioe) {
            logger.error(ioe);
            throw ioe;
        }
    }

    @Override
    public String getHelp() {
        return "AddPassengerCommand usage: " + getName();
    }
}
