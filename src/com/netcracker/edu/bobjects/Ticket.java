package com.netcracker.edu.bobjects;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by Zhassulan on 20.10.2015.
 */
public class Ticket extends HasIdObject implements Serializable {
    private double price;
    private Passenger passenger;
    private Flight flight;
    private boolean status;

    public Ticket(BigInteger id, Passenger passenger, double price, Flight flight, boolean status) {
        super(id);
        setPassenger(passenger);
        setPrice(price);
        setFlight(flight);
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException();
        }
        this.price = price;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        if (passenger == null) {
            throw new IllegalArgumentException();
        }
        this.passenger = passenger;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        if (flight == null) {
            throw new IllegalArgumentException();
        }
        this.flight = flight;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
