package com.netcracker.edu.bobjects;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Time;

/**
 *  Created by Zhassulan on 20.10.2015.
 */
public class Flight extends HasIdObject implements Serializable{
    private static final long serialVersionUID = -9167207054530317344L;
    private String from;
    private String to;
    private Time departureTime;
    private Time arrivalTime;
    private String airplane;
    private double price;

    public Flight(BigInteger id, String from, String to, Time departureTime, Time arrivalTime, String airplane, double price) {
        super(id);
        setDepartureAirportName(from);
        setArrivalAirportName(to);
        setDepartureTime(departureTime);
        setArrivalTime(arrivalTime);
        setAirplane(airplane);
        setPrice(price);
    }

    public String getDepartureAirportName() {
        return from;
    }

    public void setDepartureAirportName(String from) {
        if (from == null) {
            throw new IllegalArgumentException("Departure city can't be null");
        }
        this.from = from.toLowerCase();
    }

    public String getArrivalAirportName() {
        return to;
    }

    public void setArrivalAirportName(String to) {
        if (to == null) {
            throw new IllegalArgumentException("arrival city can't be null");
        }
        this.to = to.toLowerCase();
    }

    public Time getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Time departureTime) {
        if (departureTime == null) {
            throw new IllegalArgumentException("departure time can't be null");
        }
        this.departureTime = departureTime;
    }

    public Time getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Time arrivalTime) {
        if (arrivalTime == null) {
            throw new IllegalArgumentException("arrival time can't be null");
        }
        this.arrivalTime = arrivalTime;
    }

    public String getAirplaneName() {
        return airplane;
    }

    public void setAirplane(String airplane) {
        if (airplane == null) {
            throw new IllegalArgumentException("airplane can't be null");
        }
        this.airplane = airplane.toLowerCase();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if(price<0){
            throw new IllegalArgumentException("price can't be negative");
        }
        this.price = price;
    }
}
