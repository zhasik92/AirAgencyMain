package com.netcracker.edu.persist;

import com.netcracker.edu.bobjects.*;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Created by Zhassulan on 23.10.2015.
 */
public class InMemoryStorage implements Serializable {
    private static final long serialVersionUID= 1524210734632465536L;
    private HashSet<Passenger> passengers;
    private HashSet<City> cities;
    private HashSet<Airplane> airplanes;
    private HashSet<Ticket> tickets;
    private HashSet<Flight> flights;
    private HashSet<User> users;

    public HashSet<Airplane> getAirplanes() {
        if (airplanes == null) {
            airplanes = new HashSet<>();
        }
        return airplanes;
    }

    public HashSet<City> getCities() {
        if (cities == null) {
            cities = new HashSet<>();
        }
        return cities;
    }

    public HashSet<Flight> getFlights(){
        if(flights==null){
            flights=new HashSet<>();
        }
        return flights;
    }

    public HashSet<Passenger> getPassengers() {
        if (passengers == null) {
            passengers = new HashSet<>();
        }
        return passengers;
    }

    public HashSet<Ticket> getTickets(){
        if(tickets==null){
            tickets=new HashSet<>();
        }
        return tickets;
    }

    public HashSet<User> getUsers() {
        if(users==null){
            users=new HashSet<>();
        }
        return users;
    }
}
