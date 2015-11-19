package com.netcracker.edu.commands;


import com.netcracker.edu.dao.DAObject;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;


/**
 * Created by Zhassulan on 24.10.2015.
 */

//!!! This class is only for testing, i'll remove it later
public class ViewCommand extends AbstractCommand {
    private static final Logger logger = LogManager.getLogger(ViewCommand.class);
    @Override
    public String getName() {
        return "view";
    }

    @Override
    public int execute(String[] parameters) throws IOException {
        logger.trace("ViewCommand.execute() called");
        DAObject dao=DAObject.getInstance();
        dao.getAllPassengers().forEach(logger::info);
        dao.getAllCities().forEach(logger::info);
        dao.getAllFlights().forEach(logger::info);
        dao.getAllAirplanes().forEach(logger::info);
        return 0;
    }

    @Override
    public String getHelp() {
        return "ViewCommand usage: "+getName();
    }
}
