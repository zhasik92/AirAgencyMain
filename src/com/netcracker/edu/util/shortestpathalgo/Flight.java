package com.netcracker.edu.util.shortestpathalgo;

import java.sql.Time;

/**
 * Created by Zhassulan on 06.12.2015.
 */
public class Flight {
    public String name;
    public String srcAirport;
    public String destAirport;
    public int arrTime;
    public int depTime;

    public Flight(String name, String srcAirport, String destAirport, int depTime, int arrTime) {
        this.name = name;
        this.srcAirport = srcAirport;
        this.destAirport = destAirport;
        this.arrTime = arrTime;
        this.depTime = depTime;
    }

    public Flight(String name, String srcAirport, String destAirport, Time depTime, Time arrTime){
        this.name = name;
        this.srcAirport = srcAirport;
        this.destAirport = destAirport;
        this.depTime = TimeTable.convertJavaSqlTimeToInt(depTime);
        this.arrTime = TimeTable.convertJavaSqlTimeToInt(arrTime);
    }

    public int flightDuration() {
        return TimeTable.diff(arrTime,depTime);
    }
}
