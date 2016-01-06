package com.netcracker.edu.dao;

import com.netcracker.edu.bobjects.*;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;

/**
 * Created by Zhassulan on 04.12.2015.
 */
public interface DAObject {

    void addAirplane(Airplane airplane) throws SQLException;

    void addCity(City city) throws SQLException;

    void addFlight(Flight flight) throws SQLException;

    void addPassenger(Passenger passenger) throws SQLException;

    void addTicket(Ticket ticket) throws SQLException;

    void addAllTickets(Collection<Ticket> tickets) throws SQLException;

    void addUser(User user) throws SQLException;

    Collection<Airplane> getAllAirplanes() throws SQLException;

    Collection<City> getAllCities() throws SQLException;

    Collection<Flight> getAllFlights() throws SQLException;

    Collection<Passenger> getAllPassengers() throws SQLException;

    Collection<Ticket> getAllTickets() throws SQLException;

    // TODO: 05.01.2016 remove it?
    Collection<Ticket> getAllCanceledTicketsInFlight(BigInteger flightId, Calendar flightDate) throws SQLException;

    int getNumberOfSoldTicketsInFlight(BigInteger flightId, Calendar flightDate) throws SQLException;

    Collection<User> getAllUsers() throws SQLException;

    Airplane findAirplaneByName(String airplane) throws SQLException;

    City findCityByName(String city) throws SQLException;

    Flight findFlightById(BigInteger id) throws SQLException;

    Passenger findPassengerById(BigInteger id) throws SQLException;

    Passenger findPassengerByPassportNumberAndCitizenship(String passportNumber, String citizenship) throws SQLException;

    Ticket findTicketById(BigInteger id) throws SQLException;

    User findUserByLogin(String login) throws SQLException;

    void updateAirplane(Airplane airplane) throws SQLException;

    void updateCity(City city) throws SQLException;

    void updateFlight(Flight flight) throws SQLException;

    void updatePassenger(Passenger passenger) throws SQLException;

    void updateTicket(Ticket ticket) throws SQLException;

    void updateUser(User user) throws SQLException;
}
