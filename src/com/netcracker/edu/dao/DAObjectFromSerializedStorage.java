package com.netcracker.edu.dao;

import com.netcracker.edu.bobjects.*;
import com.netcracker.edu.persist.InMemoryStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Zhassulan on 23.10.2015.
 */
public class DAObjectFromSerializedStorage implements DAObject {
    private static DAObject instance;
    private static InMemoryStorage storage;

    private DAObjectFromSerializedStorage() {
        {
            try {
                File file = new File("InMemoryStorage.out");
                if (file.exists()) {
                    FileInputStream fis = new FileInputStream(file);
                    ObjectInputStream oin = new ObjectInputStream(fis);
                    storage = (InMemoryStorage) oin.readObject();
                    oin.close();
                } else {
                    storage = new InMemoryStorage();
                }
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized DAObject getInstance() {
        if (instance == null) {
            instance = new DAObjectFromSerializedStorage();
        }
        return instance;
    }

    @Override
    public InMemoryStorage getStorage() {
        return storage;
    }

    @Override
    public synchronized void addAirplane(Airplane airplane) {
        storage.getAirplanes().add(airplane);
    }

    @Override
    public synchronized void addCity(City city) {
        storage.getCities().add(city);
    }

    @Override
    public synchronized void addFlight(Flight flight) {
        storage.getFlights().add(flight);
    }

    @Override
    public synchronized void addPassenger(Passenger passenger) {
        storage.getPassengers().add(passenger);
    }

    @Override
    public synchronized void addTicket(Ticket ticket) {
        storage.getTickets().add(ticket);
    }

    public synchronized void addAllTickets(Collection<Ticket> tickets) {
        storage.getTickets().addAll(tickets);
    }

    @Override
    public synchronized void addUser(User user) {
        storage.getUsers().add(user);
    }

    @Override
    public synchronized Collection<Airplane> getAllAirplanes() {
        return storage.getAirplanes();
    }

    @Override
    public synchronized Collection<City> getAllCities() {
        return storage.getCities();
    }

    @Override
    public synchronized Collection<Flight> getAllFlights() {
        return storage.getFlights();
    }

    @Override
    public synchronized Collection<Passenger> getAllPassengers() {
        return storage.getPassengers();
    }

    @Override
    public synchronized Collection<Ticket> getAllTickets() {
        return storage.getTickets();
    }

    @Override
    public synchronized Collection<Ticket> getAllCanceledTicketsInFlight(BigInteger flightId, Calendar date) {
        Collection<Ticket> tickets = storage.getTickets();
        HashSet<Ticket> result = new HashSet<>();
        for (Ticket it : tickets) {
            if (it.getFlightId().equals(flightId) &&
                    it.getFlightDate().get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
                    it.getFlightDate().get(Calendar.MONTH) == date.get(Calendar.MONTH) &&
                    it.getFlightDate().get(Calendar.DATE) == date.get(Calendar.DATE) &&
                    it.isCanceled()) {
                result.add(it);
            }
        }
        return result;
    }

    @Override
    public synchronized Collection<Ticket> getAllActualTicketsInFlight(BigInteger flightId, Calendar date) {
        Collection<Ticket> tickets = storage.getTickets();
        HashSet<Ticket> result = new HashSet<>();
        for (Ticket it : tickets) {
            if (it.getFlightId().equals(flightId) &&
                    it.getFlightDate().get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
                    it.getFlightDate().get(Calendar.MONTH) == date.get(Calendar.MONTH) &&
                    it.getFlightDate().get(Calendar.DATE) == date.get(Calendar.DATE) &&
                    !it.isCanceled()) {
                result.add(it);
            }
        }
        return result;
    }

    @Override
    public synchronized Collection<User> getAllUsers() {
        return storage.getUsers();
    }

    @Override
    public synchronized Airplane findAirplaneByName(String airplane) {
        for (Airplane it : storage.getAirplanes()) {
            if (airplane.toLowerCase().equals(it.getName().toLowerCase())) {
                return it;
            }
        }
        return null;
    }

    @Override
    public synchronized City findCityByName(String city) {
        for (City it : storage.getCities()) {
            if (city.toLowerCase().equals(it.getName().toLowerCase())) {
                return it;
            }
        }
        return null;
    }

    @Override
    public synchronized Flight findFlightById(BigInteger id) {
        for (Flight it : storage.getFlights()) {
            if (id.equals(it.getId())) {
                return it;
            }
        }
        return null;
    }

    @Override
    public synchronized Passenger findPassengerById(BigInteger id) {
        for (Passenger it : storage.getPassengers()) {
            if (id.equals(it.getId())) {
                return it;
            }
        }
        return null;
    }

    @Override
    public synchronized Passenger findPassengerByPassportNumberAndCitizenship(String passportNumber, String citizenship) {
        for (Passenger it : storage.getPassengers()) {
            if (passportNumber.equals(it.getPassportNumber()) && citizenship.toLowerCase().equals(it.getCitizenship().toLowerCase())) {
                return it;
            }
        }
        return null;
    }

    @Override
    public synchronized Ticket findTicketById(BigInteger id) {
        for (Ticket it : storage.getTickets()) {
            if (id.equals(it.getId())) {
                return it;
            }
        }
        return null;
    }

    @Override
    public synchronized User findUserByLogin(String login) {
        for (User it : getAllUsers()) {
            if (it.getLogin().toLowerCase().equals(login.toLowerCase())) {
                return it;
            }
        }
        return null;
    }
}
