package com.netcracker.edu.commands;

import com.netcracker.edu.bobjects.City;
import com.netcracker.edu.bobjects.Flight;
import com.netcracker.edu.bobjects.Passenger;
import com.netcracker.edu.bobjects.Ticket;
import com.netcracker.edu.dao.DAObject;
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
    @Override
    public String getName() {
        return "buy_ticket";
    }

    @Override
    public int execute(String[] parameters) throws IOException {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {


            DAObject dao = DAObject.getInstance();
            AddPassengerCommand addPassengerCommand = (AddPassengerCommand) CommandsEngine.getInstance().getCommand("add_passenger");
            Passenger passenger = addPassengerCommand.createPassenger();

            logger.info("From: ");
            String buf = br.readLine().toLowerCase();
            City from = dao.findCityByName(buf);

            logger.info("to: ");
            buf = br.readLine().toLowerCase();
            City to = dao.findCityByName(buf);

            if (from == null || to == null) {
                logger.warn("illegal cities");
                return 1;
            }

            LinkedList<Ticket> tickets = buyTicket(passenger, from, to);
            if (tickets == null) {
                logger.warn("Sorry, all tickets have been sold");
                return 1;
            }
            for (Ticket it : tickets) {
                logger.info(it.toString());
            }
            return 0;
        }catch (IOException ioe){
            logger.error(ioe);
            throw ioe;
        }
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
                logger.trace("No available tickets it flight, id = "+it.getId());
                return null;
            }
        }

        LinkedList<Ticket> currentTickets = new LinkedList<>();
        for (Flight it : path) {
            Ticket ticket = new Ticket(IdGenerator.getInstance().getId(), passenger.getId(), it.getId(), false);
            logger.trace("Ticket created,  id = "+ticket.getId());
            dao.addTicket(ticket);
            currentTickets.add(ticket);
            logger.trace("ticket saved");
        }
        return currentTickets;
    }

    @Override
    public String getHelp() {
        return "BuyTicketCommand usage: " + getName();
    }
}
