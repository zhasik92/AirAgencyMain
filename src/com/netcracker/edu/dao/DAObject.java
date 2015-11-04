package com.netcracker.edu.dao;

import com.netcracker.edu.bobjects.*;
import com.netcracker.edu.persist.InMemoryStorage;

import java.io.*;
import java.math.BigInteger;
import java.util.Collection;

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

    public Passenger findPassengerByPassportNumberAndCitizenship(String passportNumber, String citizenship) {
        for (Passenger it : storage.getPassengers()) {
            if (passportNumber.equals(it.getPassportNumber()) && citizenship.equals(it.getCitizenship())) {
                return it;
            }
        }
        return null;
    }

    public Passenger findPassengerById(BigInteger id){
        for(Passenger it: storage.getPassengers()){
            if(id.equals(it.getId())){
                return it;
            }
        }
        return null;
    }

    public synchronized void addPassenger(Passenger passenger) {
        storage.getPassengers().add(passenger);
    }

    public Collection<Passenger> getAllPassengers() {
        return storage.getPassengers();
    }

    public City findCityByName(String city) {
        for (City it : storage.getCities()) {
            if (city.equals(it.getName())) {
                return it;
            }
        }
        return null;
    }

    public synchronized void addCity(City city) {
        storage.getCities().add(city);
    }

    public InMemoryStorage getStorage(){
        return storage;
    }

    public Airplane findAirplaneByName(String airplane){
        for(Airplane it:storage.getAirplanes()){
            if(airplane.equals(it.getName())){
                return it;
            }
        }
        return null;
    }
    public synchronized void addAirplane(Airplane airplane){
        storage.getAirplanes().add(airplane);
    }
    public Flight findFlightById(BigInteger id){
        for(Flight it:storage.getFlights()){
            if(id.equals(it.getId())){
                return it;
            }
        }
        return null;
    }
    public synchronized void addFlight(Flight flight){
        storage.getFlights().add(flight);
    }

    public Flight fingFlightById(BigInteger id){
        for (Flight it: storage.getFlights()){
            if(id.equals(it.getId())){
                return it;
            }
        }
        return null;
    }
    public synchronized void addTicket(Ticket ticket){
        storage.getTickets().add(ticket);
    }
}
