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
 * Created by Zhassulan on 07.12.2015.
 */
public class BuyTicketToShortestRouteCommand extends AbstractCommand {
    private static final Logger logger = LogManager.getLogger(FindRoutesCommand.class);
    private static DAObject dao = DAOFactory.getDAObject();

    public BuyTicketToShortestRouteCommand() {
        super(User.Roles.USER);
    }

    @Override
    public String getName() {
        return "buy_short_ticket";
    }

    @Override
    protected int execute(String[] parameters) throws IOException {
        String passport;
        String citizenship;
        City from;
        City to;
        Passenger passenger;
        Calendar flightDate = Calendar.getInstance();
        List<Ticket> tickets;
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
                logger.info("passport number:");
                passport = br.readLine();
                logger.info("citizenship:");
                citizenship = br.readLine();
                logger.info("FlightDate(yyyy-MM-dd):");

                flightDate.setTime(df.parse(br.readLine()));

            } else {
                if (parameters.length != 5) {
                    throw new IllegalArgumentException("required 5 parameters");
                }
                from = dao.findCityByName(parameters[0]);
                to = dao.findCityByName(parameters[1]);

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
                logger.warn("Sorry, all tickets have been sold");
                return 1;
            }
            logger.info("ticket bought");
            return 0;
        } catch (ParseException e) {
            logger.error(e);
            return -1;
        } catch (SQLException e) {
            logger.error(e);
            return -1;
        }

    }

    public List<Ticket> buyTicket(Passenger passenger, City from, City to, Calendar flightDate) throws SQLException {
        if (passenger == null || from == null || to == null || flightDate == null) {
            throw new IllegalArgumentException();
        }
        FindShortestRoutesCommand findShortestRoute = (FindShortestRoutesCommand) CommandsEngine.getInstance().getCommand("find_short_route");
        List<Flight> path = findShortestRoute.getPath(from.getName(), to.getName(), "00:00");
        LinkedList<Ticket> currentTickets = new LinkedList<>();
        List<Calendar> flightDates = new LinkedList<>();
        Calendar currentDate = (Calendar) flightDate.clone();
        Flight temp = path.get(0);
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
                Ticket ticket = new Ticket(IdGenerator.getInstance().getId(), passenger.getId(), it.getId(), flightDates.get(i++), Calendar.getInstance());
                logger.trace("Ticket created,  id = " + ticket.getId());
                currentTickets.add(ticket);
                SecurityContextHolder.getLoggedHolder().addTicket(ticket.getId());
            }
            dao.addAllTickets(currentTickets);
            logger.trace("tickets saved");
            return currentTickets;
        }

    }

    @Override
    public String getHelp() {
        return null;
    }
}
