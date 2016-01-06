package com.netcracker.edu.commands;


import com.netcracker.edu.bobjects.User;
import com.netcracker.edu.dao.DAOFactory;
import com.netcracker.edu.dao.DAObject;
import com.netcracker.edu.session.SecurityContextHolder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;


/**
 * Created by Zhassulan on 24.10.2015.
 */

//!!! This class is only for testing, i'll remove it later
public class ViewCommand extends AbstractCommand {
    private static final Logger logger = LogManager.getLogger(ViewCommand.class);
    private static DAObject dao = DAOFactory.getDAObject();

    public ViewCommand() {
        super(User.Roles.USER);
    }

    @Override
    public String getName() {
        return "view";
    }

    @Override
    protected int execute(String[] parameters) throws IOException {
        logger.trace("ViewCommand.execute() called");
        //try {
            /*switch (parameters[0].toLowerCase()) {
                case "users":
                    dao.getAllUsers().forEach(logger::info);
                    break;
                case "cities":
                    dao.getAllCities().forEach(logger::info);
                    break;
                case "passengers":
                    dao.getAllPassengers().forEach(logger::info);
                    break;
                case "flights":
                    dao.getAllFlights().forEach(logger::info);
                    break;
                case "airplanes":
                    dao.getAllAirplanes().forEach(logger::info);
                    break;*/
            /*case "tickets":
                for (Flight it : dao.getAllFlights()) {
                    logger.info("if flight " + it.getId() +
                            " " + it.getDepartureAirportName() +
                            " " + it.getArrivalAirportName() + " number of sold tickets= " + dao.getNumberOfSoldTicketsInFlight(it.getId(),).size()+
                    ", number of returned tickets= "+dao.getAllCanceledTicketsInFlight(it.getId()).size());
                }*/
        // }

        /*}catch (SQLException sqle){
            logger.error(sqle);
            return -1;
        }*/
        SecurityContextHolder.getLoggedHolder().getTickets().forEach(logger::info);
        return 0;
    }

    @Override
    public String getHelp() {
        return "ViewCommand usage: " + getName();
    }
}
