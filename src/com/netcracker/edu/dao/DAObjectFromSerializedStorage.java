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
    public void addAirplane(Airplane airplane) {
        synchronized (storage.getAirplanes()) {
            storage.getAirplanes().add(airplane);
        }
    }

    @Override
    public  void addCity(City city) {
        synchronized (storage.getCities()) {
            storage.getCities().add(city);
        }
    }

    @Override
    public  void addFlight(Flight flight) {
        synchronized (storage.getFlights()) {
            storage.getFlights().add(flight);
        }
    }

    @Override
    public  void addPassenger(Passenger passenger) {
        synchronized (storage.getPassengers()) {
            storage.getPassengers().add(passenger);
        }
    }

    @Override
    public  void addTicket(Ticket ticket) {
        synchronized (storage.getTickets()) {
            storage.getTickets().add(ticket);
        }
    }

    public  void addAllTickets(Collection<Ticket> tickets) {
        synchronized (storage.getTickets()) {
            storage.getTickets().addAll(tickets);
        }
    }

    @Override
    public  void addUser(User user) {
        synchronized (storage.getUsers()) {
            storage.getUsers().add(user);
        }
    }

    @Override
    public  Collection<Airplane> getAllAirplanes() {
        synchronized (storage.getAirplanes()) {
            return storage.getAirplanes();
        }
    }

    @Override
    public  Collection<City> getAllCities() {
        synchronized (storage.getCities()) {
            return storage.getCities();
        }
    }

    @Override
    public  Collection<Flight> getAllFlights() {
        synchronized (storage.getFlights()) {
            return storage.getFlights();
        }
    }

    @Override
    public  Collection<Passenger> getAllPassengers() {
        synchronized (storage.getPassengers()) {
            return storage.getPassengers();
        }
    }

    @Override
    public  Collection<Ticket> getAllTickets() {
        synchronized (storage.getTickets()) {
            return storage.getTickets();
        }
    }

    @Override
    public  Collection<Ticket> getAllCanceledTicketsInFlight(BigInteger flightId, Calendar date) {
        HashSet<Ticket> result = new HashSet<>();
        Collection<Ticket> tickets = storage.getTickets();
        synchronized (tickets) {
            for (Ticket it : tickets) {
                if (it.getFlightId().equals(flightId) &&
                        it.getFlightDate().get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
                        it.getFlightDate().get(Calendar.MONTH) == date.get(Calendar.MONTH) &&
                        it.getFlightDate().get(Calendar.DATE) == date.get(Calendar.DATE) &&
                        it.isCanceled()) {
                    result.add(it);
                }
            }
        }
        return result;
    }

    @Override
    public  Collection<Ticket> getAllActualTicketsInFlight(BigInteger flightId, Calendar date) {
        HashSet<Ticket> result = new HashSet<>();
        Collection<Ticket> tickets = storage.getTickets();
        synchronized (storage.getTickets()) {
            for (Ticket it : tickets) {
                if (it.getFlightId().equals(flightId) &&
                        it.getFlightDate().get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
                        it.getFlightDate().get(Calendar.MONTH) == date.get(Calendar.MONTH) &&
                        it.getFlightDate().get(Calendar.DATE) == date.get(Calendar.DATE) &&
                        !it.isCanceled()) {
                    result.add(it);
                }
            }
        }
        return result;
    }

    @Override
    public  Collection<User> getAllUsers() {
        synchronized (storage.getUsers()) {
            return storage.getUsers();
        }
    }

    @Override
    public  Airplane findAirplaneByName(String airplane) {
        String lowerCaseAirplane = airplane.toLowerCase();
        synchronized (storage.getAirplanes()) {
            for (Airplane it : storage.getAirplanes()) {
                if (lowerCaseAirplane.equals(it.getName().toLowerCase())) {
                    return it;
                }
            }
        }
        return null;
    }

    @Override
    public  City findCityByName(String city) {
        String lowerCaseCity = city.toLowerCase();
        synchronized (storage.getCities()) {
            for (City it : storage.getCities()) {
                if (lowerCaseCity.equals(it.getName().toLowerCase())) {
                    return it;
                }
            }
        }
        return null;
    }

    @Override
    public  Flight findFlightById(BigInteger id) {
        synchronized (storage.getFlights()) {
            for (Flight it : storage.getFlights()) {
                if (id.equals(it.getId())) {
                    return it;
                }
            }
        }
        return null;
    }

    @Override
    public  Passenger findPassengerById(BigInteger id) {
        synchronized (storage.getPassengers()) {
            for (Passenger it : storage.getPassengers()) {
                if (id.equals(it.getId())) {
                    return it;
                }
            }
        }
        return null;
    }

    @Override
    public  Passenger findPassengerByPassportNumberAndCitizenship(String passportNumber, String citizenship) {
        synchronized (storage.getPassengers()) {
            for (Passenger it : storage.getPassengers()) {
                if (passportNumber.equals(it.getPassportNumber()) && citizenship.toLowerCase().equals(it.getCitizenship().toLowerCase())) {
                    return it;
                }
            }
        }
        return null;
    }

    @Override
    public  Ticket findTicketById(BigInteger id) {
        synchronized (storage.getTickets()) {
            for (Ticket it : storage.getTickets()) {
                if (id.equals(it.getId())) {
                    return it;
                }
            }
        }
        return null;
    }

    @Override
    public User findUserByLogin(String login) {
        synchronized (getAllUsers()) {
            for (User it : getAllUsers()) {
                if (it.getLogin().toLowerCase().equals(login.toLowerCase())) {
                    return it;
                }
            }
        }
        return null;
    }
}
