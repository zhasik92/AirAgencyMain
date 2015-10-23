package com.netcracker.edu.bobjects;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Time;

/**
 * Created by Zhassulan on 20.10.2015.
 */
public class Flight extends HasIdObject implements Serializable{
    private City from;
    private City to;
    private Time departureTime;
    private Time arrivalTime;
    private Airplane airplane;

    public Flight(BigInteger id, City from, City to, Time departureTime, Time arrivalTime, Airplane airplane) {
        super(id);
        setFrom(from);
        setTo(to);
        setDepartureTime(departureTime);
        setArrivalTime(arrivalTime);
        setAirplane(airplane);
    }

    public City getFrom() {
        return from;
    }

    public void setFrom(City from) {
        if (from == null) {
            throw new NullPointerException();
        }
        this.from = from;
    }

    public City getTo() {
        return to;
    }

    public void setTo(City to) {
        if (to == null) {
            throw new NullPointerException();
        }
        this.to = to;
    }

    public Time getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Time departureTime) {
        if (departureTime == null) {
            throw new NullPointerException();
        }
        this.departureTime = departureTime;
    }

    public Time getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Time arrivalTime) {
        if (arrivalTime == null) {
            throw new NullPointerException();
        }
        this.arrivalTime = arrivalTime;
    }

    public Airplane getAirplane() {
        return airplane;
    }

    public void setAirplane(Airplane airplane) {
        if (airplane == null) {
            throw new NullPointerException();
        }
        this.airplane = airplane;
    }
}
