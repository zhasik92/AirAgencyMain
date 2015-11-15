package com.netcracker.edu.dao;

import com.netcracker.edu.bobjects.*;
import com.netcracker.edu.persist.InMemoryStorage;

import java.io.*;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Zhassulan on 23.10.2015.
 */
public class DAObject {
    private static DAObject instance;
    private static InMemoryStorage storage;

    private DAObject() {
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
            instance = new DAObject();
        }
        return instance;
    }

    public InMemoryStorage getStorage() {
        return storage;
    }

    public synchronized void addAirplane(Airplane airplane) {
        storage.getAirplanes().add(airplane);
    }

    public synchronized void addCity(City city) {
        storage.getCities().add(city);
    }

    public synchronized void addFlight(Flight flight) {
        storage.getFlights().add(flight);
    }

    public synchronized void addPassenger(Passenger passenger) {
        storage.getPassengers().add(passenger);
    }

    public synchronized void addTicket(Ticket ticket) {
        storage.getTickets().add(ticket);
    }

    public Collection<Airplane> getAllAirplanes() {
        return storage.getAirplanes();
    }

    public Collection<City> getAllCities() {
        return storage.getCities();
    }

    public Collection<Flight> getAllFlights() {
        return storage.getFlights();
    }

    public Collection<Passenger> getAllPassengers() {
        return storage.getPassengers();
    }

    public Collection<Ticket> getAllTickets() {
        return storage.getTickets();
    }

    public Collection<Ticket> getAllCanceledTicketsInFlight(BigInteger flightId) {
        Collection<Ticket> tickets = storage.getTickets();
        HashSet<Ticket> result = new HashSet<>();
        for (Ticket it : tickets) {
            if (it.isCanceled()) {
                result.add(it);
            }
        }
        return result;
    }

    public Collection<Ticket> getAllActualTicketsInFlight(BigInteger flightId) {
        Collection<Ticket> tickets = storage.getTickets();
        HashSet<Ticket> result = new HashSet<>();
        for (Ticket it : tickets) {
            if (!it.isCanceled()) {
                result.add(it);
            }
        }
        return result;
    }

    public Airplane findAirplaneByName(String airplane) {
        for (Airplane it : storage.getAirplanes()) {
            if (airplane.toLowerCase().equals(it.getName().toLowerCase())) {
                return it;
            }
        }
        return null;
    }

    public City findCityByName(String city) {
        for (City it : storage.getCities()) {
            if (city.toLowerCase().equals(it.getName().toLowerCase())) {
                return it;
            }
        }
        return null;
    }

    public Flight findFlightById(BigInteger id) {
        for (Flight it : storage.getFlights()) {
            if (id.equals(it.getId())) {
                return it;
            }
        }
        return null;
    }

    public Passenger findPassengerById(BigInteger id) {
        for (Passenger it : storage.getPassengers()) {
            if (id.equals(it.getId())) {
                return it;
            }
        }
        return null;
    }

    public Passenger findPassengerByPassportNumberAndCitizenship(String passportNumber, String citizenship) {
        for (Passenger it : storage.getPassengers()) {
            if (passportNumber.equals(it.getPassportNumber()) && citizenship.equals(it.getCitizenship())) {
                return it;
            }
        }
        return null;
    }

    public Ticket findTicketById(BigInteger id) {
        for (Ticket it : storage.getTickets()) {
            if (id.equals(it.getId())) {
                return it;
            }
        }
        return null;
    }
}
