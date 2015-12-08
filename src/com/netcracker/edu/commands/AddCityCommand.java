package com.netcracker.edu.commands;

import com.netcracker.edu.bobjects.City;
import com.netcracker.edu.bobjects.User;
import com.netcracker.edu.dao.DAObject;
import com.netcracker.edu.dao.DAObjectFromSerializedStorage;
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

    public AddCityCommand() {
        super(User.Roles.ADMIN);
    }

    @Override
    public String getName() {
        return "add_city";
    }

    @Override
    protected int execute(String[] parameters) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        DAObject dao = DAObjectFromSerializedStorage.getInstance();
        City city;
        String cityName;
        if (parameters != null && !parameters[0].isEmpty() && parameters.length == 1) {
            cityName = parameters[0];
        } else {
            logger.info("Enter name of City:");
            cityName = br.readLine();
        }

        city = dao.findCityByName(cityName);
        if (city != null) {
            logger.info("City already exist");
            return 1;
        }

        city = new City(cityName);
        dao.addCity(city);
        logger.trace("city added");
        return 0;
    }

    @Override
    public String getHelp() {
        return "AddCityCommand usage: " + "\"" + getName() + "\"" + "or " + "\"" + getName() + "CityName" + "\"";
    }
}
