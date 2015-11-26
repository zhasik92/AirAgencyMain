package com.netcracker.edu.commands;

import com.netcracker.edu.bobjects.*;
import com.netcracker.edu.dao.DAObject;
import com.netcracker.edu.session.SecurityContextHolder;
import com.netcracker.edu.util.IdGenerator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * Created by Zhassulan on 30.10.2015.
 */
public class BuyTicketCommand extends AbstractCommand {
    private static final Logger logger = LogManager.getLogger(BuyTicketCommand.class);
    private static DAObject dao = DAObject.getInstance();

    public BuyTicketCommand() {
        super(User.Roles.USER);
    }

    @Override
    public String getName() {
        return "buy_ticket";
    }

    @Override
    protected int execute(String[] parameters) throws IOException {
        String passport;
        String citizenship;
        City from;
        City to;
        Passenger passenger;
        LinkedList<Ticket> tickets;
        if (parameters == null || parameters.length < 1) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            logger.info("From: ");
            String buf = br.readLine().toLowerCase();
            from = dao.findCityByName(buf);
            logger.info("to: ");
            buf = br.readLine().toLowerCase();
            to = dao.findCityByName(buf);
            logger.info("passport number:");
            passport = br.readLine();
            logger.info("citizenship:");
            citizenship = br.readLine();
        } else {
            if (parameters.length != 4) {
                throw new IllegalArgumentException("requared 4 parameters");
            }
            from = dao.findCityByName(parameters[0]);
            to = dao.findCityByName(parameters[1]);
            passport = parameters[2];
            citizenship = parameters[3];
        }
        passenger = dao.findPassengerByPassportNumberAndCitizenship(passport, citizenship);
        if (passenger == null) {
            throw new IllegalArgumentException("wrong passport number or citizenship or passenger haven't registered");
        }
        if (from == null || to == null) {
            logger.warn("illegal cities");
            return 1;
        }
        tickets = buyTicket(passenger, from, to);
        if (tickets == null) {
            logger.warn("Sorry, all tickets have been sold");
            return 1;
        }

        for (Ticket it : tickets) {
            logger.info(it.toString());
        }
        return 0;
    }

    //return LinkedList with bought tickets or null if no tickets available
    public LinkedList<Ticket> buyTicket(Passenger passenger, City from, City to) {
        if (passenger == null || from == null || to == null) {
            throw new IllegalArgumentException();
        }
        DAObject dao = DAObject.getInstance();
        FindRoutesCommand findRoutesCommand = (FindRoutesCommand) CommandsEngine.getInstance().getCommand("find_routes");
        LinkedList<Flight> path = findRoutesCommand.getPath(from.getName(), to.getName());
        for (Flight it : path) {
            int airplaneCapacity = dao.findAirplaneByName(it.getAirplaneName()).getCapacity();
            int numberOfSoldTickets = dao.getAllActualTicketsInFlight(it.getId()).size();
            if (airplaneCapacity - numberOfSoldTickets < 1) {
                logger.trace("No available tickets it flight, id = " + it.getId());
                return null;
            }
        }

        LinkedList<Ticket> currentTickets = new LinkedList<>();
        for (Flight it : path) {
            Ticket ticket = new Ticket(IdGenerator.getInstance().getId(), passenger.getId(), it.getId(), false);
            logger.trace("Ticket created,  id = " + ticket.getId());
            dao.addTicket(ticket);
            currentTickets.add(ticket);
            SecurityContextHolder.getLoggedHolder().addTicket(ticket.getId());
            logger.trace("ticket saved");
        }
        return currentTickets;
    }

    @Override
    public String getHelp() {
        return "BuyTicketCommand usage: " + "\"" + getName() + "\"" + " or " + "\"" + getName() + "From To passportNumber citizenship" + "\"";
    }
}
