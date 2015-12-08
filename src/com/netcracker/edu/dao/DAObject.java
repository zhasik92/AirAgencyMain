package com.netcracker.edu.dao;

import com.netcracker.edu.bobjects.*;
import com.netcracker.edu.persist.InMemoryStorage;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collection;

/**
 * Created by Zhassulan on 04.12.2015.
 */
public interface DAObject {
    InMemoryStorage getStorage();

    void addAirplane(Airplane airplane);

    void addCity(City city);

    void addFlight(Flight flight);

    void addPassenger(Passenger passenger);

    void addTicket(Ticket ticket);

    void addAllTickets(Collection<Ticket> tickets);

    void addUser(User user);

    Collection<Airplane> getAllAirplanes();

    Collection<City> getAllCities();

    Collection<Flight> getAllFlights();

    Collection<Passenger> getAllPassengers();

    Collection<Ticket> getAllTickets();

    Collection<Ticket> getAllCanceledTicketsInFlight(BigInteger flightId, Calendar flightDate);

    Collection<Ticket> getAllActualTicketsInFlight(BigInteger flightId, Calendar flightDate);

    Collection<User> getAllUsers();

    Airplane findAirplaneByName(String airplane);

    City findCityByName(String city);

    Flight findFlightById(BigInteger id);

    Passenger findPassengerById(BigInteger id);

    Passenger findPassengerByPassportNumberAndCitizenship(String passportNumber, String citizenship);

    Ticket findTicketById(BigInteger id);

    User findUserByLogin(String login);
}
