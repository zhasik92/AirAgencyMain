package com.netcracker.edu.dao;

import com.netcracker.edu.bobjects.*;
import com.netcracker.edu.persist.InMemoryStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Zhassulan on 23.10.2015.
 */
public class InMemoryDAO implements DAObject {
    private static InMemoryDAO instance;
    private static InMemoryStorage storage;

    private ReentrantReadWriteLock airplanesRWL = new ReentrantReadWriteLock();
    private Lock airplanesRL = airplanesRWL.readLock();
    private Lock airplanesWL = airplanesRWL.writeLock();

    private ReentrantReadWriteLock citiesRWL = new ReentrantReadWriteLock();
    private Lock citiesRL = citiesRWL.readLock();
    private Lock citiesWL = citiesRWL.writeLock();

    private ReentrantReadWriteLock flightsRWL = new ReentrantReadWriteLock();
    private Lock flightsRL = flightsRWL.readLock();
    private Lock flightsWL = flightsRWL.writeLock();

    private ReentrantReadWriteLock passengersRWL = new ReentrantReadWriteLock();
    private Lock passengersRL = passengersRWL.readLock();
    private Lock passengersWL = passengersRWL.writeLock();

    private ReentrantReadWriteLock ticketsRWL = new ReentrantReadWriteLock();
    private Lock ticketsRL = ticketsRWL.readLock();
    private Lock ticketWL = ticketsRWL.writeLock();

    private ReentrantReadWriteLock usersRWL = new ReentrantReadWriteLock();
    private Lock usersRL = usersRWL.readLock();
    private Lock usersWL = usersRWL.writeLock();

    private InMemoryDAO() {
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

    // TODO: 04.01.2016 check singleton
    public static synchronized InMemoryDAO getInstance() {
        if (instance == null) {
            instance = new InMemoryDAO();
        }
        return instance;
    }

    public InMemoryStorage getStorage() {
        return storage;
    }

    @Override
    public void addAirplane(Airplane airplane) {
        airplanesWL.lock();
        try {
            //synchronized (storage.getAirplanes()) {
            storage.getAirplanes().add(airplane);
            //}
        } finally {
            airplanesWL.unlock();
        }
    }

    @Override
    public void addCity(City city) {
        citiesWL.lock();
        try {
            //synchronized (storage.getCities()) {
            storage.getCities().add(city);
            //}
        } finally {
            citiesWL.unlock();
        }
    }

    @Override
    public void addFlight(Flight flight) {
        //  synchronized (storage.getFlights()) {
        flightsWL.lock();
        try {
            storage.getFlights().add(flight);
            //   }
        } finally {
            flightsWL.unlock();
        }
    }

    @Override
    public void addPassenger(Passenger passenger) {
        // synchronized (storage.getPassengers()) {
        passengersWL.lock();
        try {
            storage.getPassengers().add(passenger);
            //  }
        } finally {
            passengersWL.unlock();
        }
    }

    @Override
    public void addTicket(Ticket ticket) {
        //synchronized (storage.getTickets()) {
        ticketWL.lock();
        try {
            storage.getTickets().add(ticket);
            //}
        } finally {
            ticketWL.unlock();
        }

    }

    public void addAllTickets(Collection<Ticket> tickets) {
        // synchronized (storage.getTickets()) {
        ticketWL.lock();
        try {
            storage.getTickets().addAll(tickets);
            //  }
        } finally {
            ticketWL.unlock();
        }
    }

    @Override
    public void addUser(User user) {
        //synchronized (storage.getUsers()) {
        usersWL.lock();
        try {
            storage.getUsers().add(user);
            //}
        } finally {
            usersWL.unlock();
        }
    }

    @Override
    public Collection<Airplane> getAllAirplanes() {
        // synchronized (storage.getAirplanes()) {
        airplanesRL.lock();
        try {
            return storage.getAirplanes();
            //}
        } finally {
            airplanesRL.unlock();
        }
    }

    @Override
    public Collection<City> getAllCities() {
        //    synchronized (storage.getCities()) {
        citiesRL.lock();
        try {
            return storage.getCities();
            //}
        } finally {
            citiesRL.unlock();
        }
    }

    @Override
    public Collection<Flight> getAllFlights() {
        // synchronized (storage.getFlights()) {
        flightsRL.lock();
        try {
            return storage.getFlights();
            // }
        } finally {
            flightsRL.unlock();
        }
    }

    @Override
    public Collection<Passenger> getAllPassengers() {
        //  synchronized (storage.getPassengers()) {
        passengersRL.lock();
        try {
            return storage.getPassengers();
        } finally {
            passengersRL.unlock();
        }
        //  }
    }

    @Override
    public Collection<Ticket> getAllTickets() {
        ticketsRL.lock();
        //  synchronized (storage.getTickets()) {
        try {
            return storage.getTickets();
            // }
        } finally {
            ticketsRL.unlock();
        }
    }

    @Override
    public Collection<Ticket> getAllCanceledTicketsInFlight(BigInteger flightId, Calendar date) {
        HashSet<Ticket> result = new HashSet<>();
        Collection<Ticket> tickets = storage.getTickets();
        //  synchronized (tickets) {
        ticketsRL.lock();
        try {
            for (Ticket it : tickets) {
                if (it.getFlightId().equals(flightId) &&
                        it.getFlightDate().get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
                        it.getFlightDate().get(Calendar.MONTH) == date.get(Calendar.MONTH) &&
                        it.getFlightDate().get(Calendar.DATE) == date.get(Calendar.DATE) &&
                        it.isCanceled()) {
                    result.add(it);
                }
            }
        } finally {
            ticketsRL.unlock();
        }
        //}
        return result;
    }

    @Override
    public int getNumberOfSoldTicketsInFlight(BigInteger flightId, Calendar date) {
        Collection<Ticket> tickets = storage.getTickets();
        int result = 0;
        ticketsRL.lock();
        try {
            for (Ticket it : tickets) {
                if (it.getFlightId().equals(flightId) &&
                        it.getFlightDate().get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
                        it.getFlightDate().get(Calendar.MONTH) == date.get(Calendar.MONTH) &&
                        it.getFlightDate().get(Calendar.DATE) == date.get(Calendar.DATE) &&
                        !it.isCanceled()) {
                    result++;
                }
            }
        } finally {
            ticketsRL.unlock();
        }
        return result;
    }

    @Override
    public Collection<User> getAllUsers() {
        //  synchronized (storage.getUsers()) {
        usersRL.lock();
        try {
            return storage.getUsers();
            //  }
        } finally {
            usersRL.unlock();
        }
    }

    @Override
    public Airplane findAirplaneByName(String airplane) {
        String lowerCaseAirplane = airplane.toLowerCase();
        //  synchronized (storage.getAirplanes()) {
        airplanesRL.lock();
        try {
            for (Airplane it : storage.getAirplanes()) {
                if (lowerCaseAirplane.equals(it.getName().toLowerCase())) {
                    return it;
                }
            }
        } finally {
            airplanesRL.unlock();
        }
        //}
        return null;
    }

    @Override
    public City findCityByName(String city) {
        String lowerCaseCity = city.toLowerCase();
        //synchronized (storage.getCities()) {
        citiesRL.lock();
        try {
            for (City it : storage.getCities()) {
                if (lowerCaseCity.equals(it.getName().toLowerCase())) {
                    return it;
                }
            }
        } finally {
            citiesRL.unlock();
        }
        //}
        return null;
    }

    @Override
    public Flight findFlightById(BigInteger id) {
        //synchronized (storage.getFlights()) {
        flightsRL.lock();
        try {
            for (Flight it : storage.getFlights()) {
                if (id.equals(it.getId())) {
                    return it;
                }
            }
        } finally {
            flightsRL.unlock();
        }
        // }
        return null;
    }

    @Override
    public Passenger findPassengerById(BigInteger id) {
        // synchronized (storage.getPassengers()) {
        passengersRL.lock();
        try {
            for (Passenger it : storage.getPassengers()) {
                if (id.equals(it.getId())) {
                    return it;
                }
            }
        } finally {
            passengersRL.unlock();
        }
        // }
        return null;
    }

    @Override
    public Passenger findPassengerByPassportNumberAndCitizenship(String passportNumber, String citizenship) {
        //    synchronized (storage.getPassengers()) {
        passengersRL.lock();
        try {
            for (Passenger it : storage.getPassengers()) {
                if (passportNumber.equals(it.getPassportNumber()) && citizenship.toLowerCase().equals(it.getCitizenship().toLowerCase())) {
                    return it;
                }
            }
        } finally {
            passengersRL.unlock();
        }
        //   }
        return null;
    }

    @Override
    public Ticket findTicketById(BigInteger id) {
        //    synchronized (storage.getTickets()) {
        ticketsRL.lock();
        try {
            for (Ticket it : storage.getTickets()) {
                if (id.equals(it.getId())) {
                    return it;
                }
            }
        } finally {
            ticketsRL.unlock();
        }
        //   }
        return null;
    }

    @Override
    public User findUserByLogin(String login) {
        String loginInLowerCase = login.toLowerCase();
        //      synchronized (getAllUsers()) {
        usersRL.lock();
        try {
            for (User it : getAllUsers()) {
                if (it.getLogin().toLowerCase().equals(loginInLowerCase)) {
                    return it;
                }
            }
        } finally {
            usersRL.unlock();
        }
        //      }
        return null;
    }

    // TODO: 04.01.2016 mark as stubs
    @Override
    public void updateAirplane(Airplane airplane) throws SQLException {

    }

    @Override
    public void updateCity(City city) throws SQLException {

    }

    @Override
    public void updateFlight(Flight flight) throws SQLException {

    }

    @Override
    public void updatePassenger(Passenger passenger) throws SQLException {

    }

    @Override
    public void updateTicket(Ticket ticket) throws SQLException {

    }

    @Override
    public void updateUser(User user) throws SQLException {

    }
}
