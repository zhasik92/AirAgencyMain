package com.netcracker.edu.bobjects;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by Zhassulan on 20.10.2015.
 */
public class Ticket extends HasIdObject implements Serializable {
    private BigInteger passenger;
    private BigInteger flight;
    private boolean status;

    public Ticket(BigInteger id, BigInteger passenger, BigInteger flight, boolean status) {
        super(id);
        setPassengerId(passenger);
        setFlightId(flight);
        this.status = status;
    }

    public BigInteger getPassengerId() {
        return passenger;
    }

    public void setPassengerId(BigInteger passenger) {
        if (passenger == null||passenger.compareTo(BigInteger.ZERO)<0) {
            throw new IllegalArgumentException("passenger's id can't be null or negative");
        }
        this.passenger = passenger;
    }

    public BigInteger getFlightId() {
        return flight;
    }

    public void setFlightId(BigInteger flight) {
        if (flight == null||passenger.compareTo(BigInteger.ZERO)<0) {
            throw new IllegalArgumentException("flight id can't be null or negative");
        }
        this.flight = flight;
    }

    public boolean isCanceled() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
