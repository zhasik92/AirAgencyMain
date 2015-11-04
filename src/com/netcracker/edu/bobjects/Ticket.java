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
        if (passenger == null) {
            throw new IllegalArgumentException();
        }
        this.passenger = passenger;
    }

    public BigInteger getFlightId() {
        return flight;
    }

    public void setFlightId(BigInteger flight) {
        if (flight == null) {
            throw new IllegalArgumentException();
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
