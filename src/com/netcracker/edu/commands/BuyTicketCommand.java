package com.netcracker.edu.commands;

import com.netcracker.edu.bobjects.*;
import com.netcracker.edu.dao.DAOFactory;
import com.netcracker.edu.dao.DAObject;
import com.netcracker.edu.session.SecurityContextHolder;
import com.netcracker.edu.util.IdGenerator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Command
 * Created by Zhassulan on 30.10.2015.
 */
public class BuyTicketCommand extends AbstractCommand {
    private static final Logger logger = LogManager.getLogger(BuyTicketCommand.class);
    private static DAObject dao = DAOFactory.getDAObject();

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
        Calendar flightDate;
        Passenger passenger;
        LinkedList<Ticket> tickets;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (parameters == null || parameters.length < 1) {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                logger.info("From: ");
                String buf = br.readLine().toLowerCase();
                from = dao.findCityByName(buf);
                logger.info("to: ");
                buf = br.readLine().toLowerCase();
                to = dao.findCityByName(buf);
                logger.info("Flight date(yyyy-MM-dd):");
                buf = br.readLine();
                flightDate = Calendar.getInstance();
                flightDate.setTime(df.parse(buf));
                logger.info("passport number:");
                passport = br.readLine();
                logger.info("citizenship:");
                citizenship = br.readLine();
            } else {
                if (parameters.length != 5) {
                    throw new IllegalArgumentException("required 5 parameters");
                }
                from = dao.findCityByName(parameters[0]);
                to = dao.findCityByName(parameters[1]);
                flightDate = Calendar.getInstance();
                flightDate.setTime(df.parse(parameters[2]));
                passport = parameters[3];
                citizenship = parameters[4];
            }
            passenger = dao.findPassengerByPassportNumberAndCitizenship(passport, citizenship);
            if (passenger == null) {
                throw new IllegalArgumentException("wrong passport number or citizenship or passenger haven't registered");
            }
            if (from == null || to == null) {
                logger.warn("illegal cities");
                return 1;
            }
            tickets = buyTicket(passenger, from, to, flightDate);
            if (tickets == null) {
                logger.warn("Sorry, can't buy tickets");
                return 1;
            }

            for (Ticket it : tickets) {
                logger.info(it.toString());
            }
            return 0;
        } catch (ParseException e) {
            logger.error("illegal flightDate format");
            return 1;
        }catch (SQLException sqle){
            return -1;
        }
    }

    //return LinkedList with bought tickets or null if no tickets available
    public LinkedList<Ticket> buyTicket(Passenger passenger, City from, City to, Calendar flightDate) throws SQLException {
        if (passenger == null || from == null || to == null) {
            throw new IllegalArgumentException();
        }
        FindRoutesCommand findRoutesCommand = (FindRoutesCommand) CommandsEngine.getInstance().getCommand("find_routes");
        LinkedList<Flight> path = findRoutesCommand.getPath(from.getName(), to.getName());
        LinkedList<Ticket> currentTickets = new LinkedList<>();
        List<Calendar> flightDates = new LinkedList<>();
        Calendar currentDate = (Calendar) flightDate.clone();
        Flight temp = path.getFirst();
        synchronized (this) {

            for (Flight it : path) {
                if (temp.getArrivalTime().compareTo(it.getDepartureTime()) > 0) {
                    flightDate.add(Calendar.DATE, 1);
                    currentDate = (Calendar) flightDate.clone();
                }
                flightDates.add(currentDate);
                int airplaneCapacity = dao.findAirplaneByName(it.getAirplaneName()).getCapacity();
                int numberOfSoldTickets = dao.getNumberOfSoldTicketsInFlight(it.getId(), currentDate);
                if (airplaneCapacity - numberOfSoldTickets < 1) {
                    logger.warn("No available tickets in flight, id = " + it.getId() +
                            ", from: " + it.getDepartureAirportName() +
                            ", to: " + it.getArrivalAirportName() +
                            " at: " + currentDate.get(Calendar.YEAR) + " " + currentDate.get(Calendar.MONTH) + " " + currentDate.get(Calendar.DATE));
                    return null;
                }
                temp = it;
            }
            int i = 0;
            for (Flight it : path) {
                Ticket ticket = new Ticket(IdGenerator.getInstance().getId(), passenger.getId(), it.getId(), flightDates.get(i++),Calendar.getInstance());
                logger.trace("Ticket created,  id = " + ticket.getId());
                currentTickets.add(ticket);
                SecurityContextHolder.getLoggedHolder().addTicket(ticket.getId());
            }
            dao.addAllTickets(currentTickets);
            logger.trace("ticket saved");
       }
        return currentTickets;
    }

    @Override
    public String getHelp() {
        return "BuyTicketCommand usage: " + "\"" + getName() + "\"" + " or " + "\"" + getName() + "From To passportNumber citizenship" + "\"";
    }
}
