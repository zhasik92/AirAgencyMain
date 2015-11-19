package com.netcracker.edu.commands;

import com.netcracker.edu.bobjects.City;
import com.netcracker.edu.dao.DAObject;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Zhassulan on 03.11.2015.
 */
public class AddCityCommand extends AbstractCommand {
    private static final Logger logger = LogManager.getLogger(AddCityCommand.class);

    @Override
    public String getName() {
        return "add_city";
    }

    @Override
    public int execute(String[] parameters) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            logger.info("Enter name of City:");
            String cityName = br.readLine();

            DAObject dao = DAObject.getInstance();
            City city = dao.findCityByName(cityName);
            if (city != null) {
                logger.info("City already exist");
                return 0;
            }

            city = new City(cityName);
            dao.addCity(city);
            logger.trace("city added");
            return 0;
        } catch (IOException ioe) {
            logger.error(ioe);
            throw ioe;
        }
    }

    @Override
    public String getHelp() {
        return "AddCityCommand usage: " + getName();
    }
}
