package com.netcracker.edu.commands;

import com.netcracker.edu.bobjects.Flight;
import com.netcracker.edu.dao.DAObject;
import com.netcracker.edu.util.IdGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Time;

/**
 * Created by Zhassulan on 03.11.2015.
 */
public class AddFlightCommand extends AbstractCommand {
    @Override
    public String getName() {
        return "add_flight";
    }

    @Override
    public int execute(String[] parameters) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("From:");
        String from = br.readLine();

        System.out.println("To:");
        String to=br.readLine();

        System.out.println("Departure time(hh:mm:ss):");
        Time departureTime=Time.valueOf(br.readLine());

        System.out.println("Arrival time(hh:mm:ss):");
        Time arrivalTime=Time.valueOf(br.readLine());

        System.out.println("Write Airplane type:");
        String airplane=br.readLine();

        DAObject dao=DAObject.getInstance();
        if (dao.findAirplaneByName(airplane)==null) {
            System.out.println("No such airplane type");
            return 0;
        }

        System.out.println("Set price:");
        double price=Double.parseDouble(br.readLine());

        Flight flight = new Flight(IdGenerator.getInstance().getId(),from,to,departureTime,arrivalTime,airplane,price);
        dao.addFlight(flight);
        return 0;
    }
}
