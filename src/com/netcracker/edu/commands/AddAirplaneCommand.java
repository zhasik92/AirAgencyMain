package com.netcracker.edu.commands;

import com.netcracker.edu.bobjects.Airplane;
import com.netcracker.edu.dao.DAObject;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Created by Zhassulan on 03.11.2015.
 */
public class AddAirplaneCommand extends AbstractCommand {
    private static final Logger logger = LogManager.getLogger(AddAirplaneCommand.class);

    @Override
    public String getName() {
        return "add_airplane";
    }

    @Override
    public int execute(String[] parameters) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            logger.info("Enter name of Airplane:");
            String airplaneName = br.readLine();

            DAObject dao = DAObject.getInstance();
            Airplane airplane = dao.findAirplaneByName(airplaneName);
            if (airplane != null) {
                logger.warn("Airplane already exist");
                return 0;
            }

            logger.info("write capacity");
            Integer capacity = Integer.parseInt(br.readLine());
            airplane = new Airplane(airplaneName, capacity);
            dao.addAirplane(airplane);
            logger.trace("airplane added");
            return 0;
        } catch (IOException ioe) {
            logger.error(ioe);
            throw ioe;
        }
    }

    @Override
    public String getHelp() {
        return "AddAirplaneCommand usage: " + getName();
    }
}
