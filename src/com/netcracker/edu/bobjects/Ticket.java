package com.netcracker.edu.bobjects;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Zhassulan on 20.10.2015.
 */
public class Ticket extends HasIdObject implements Serializable {
    private static final long serialVersionUID=-9112295186131870382L;
    private BigInteger passenger;
    private BigInteger flight;
    private boolean status;
    private Calendar flightDate;
    private Calendar ticketBoughtDate;

    public Calendar getFlightDate() {
        return flightDate;
    }

    public Ticket(BigInteger id, BigInteger passenger, BigInteger flight,Calendar flightDate) {
        super(id);
        setPassengerId(passenger);
        setFlightId(flight);
        this.status = false;
        this.flightDate=flightDate;
        ticketBoughtDate= GregorianCalendar.getInstance();
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
