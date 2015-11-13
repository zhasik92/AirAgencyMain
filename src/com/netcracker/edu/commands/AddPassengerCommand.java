package com.netcracker.edu.commands;

import com.netcracker.edu.bobjects.Passenger;
import com.netcracker.edu.dao.DAObject;
import com.netcracker.edu.util.IdGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;

/**
 * Created by Zhassulan on 23.10.2015.
 */
public class AddPassengerCommand extends AbstractCommand {

    @Override
    public String getName() {
        return "add_passenger";
    }

    @Override
    public int execute(String[] parameters) throws IOException {
        createPassenger();
        return 0;
    }

    public Passenger createPassenger() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter passport number:");
        String passportNumber = br.readLine();

        System.out.println("Citizenship:");
        String citizenship = br.readLine();

        DAObject dao =DAObject.getInstance();
        Passenger passenger = dao.findPassengerByPassportNumberAndCitizenship(passportNumber, citizenship);
        if (passenger != null) {
            System.out.println("passenger already exist");
            return passenger;
        }
        System.out.println("first name:");
        String firstName = br.readLine();

        System.out.println("last name:");
        String lastName = br.readLine();

        System.out.println("date of birth(yyyy-[m]m-[d]d):");
        Date dateOfBirth = Date.valueOf(br.readLine());

        System.out.println("email: ");
        String email = br.readLine();

        passenger = new Passenger(IdGenerator.getInstance().getId(), email,firstName,lastName,dateOfBirth, passportNumber, citizenship);
        dao.addPassenger(passenger);
        return passenger;

    }
    @Override
    public String getHelp() {
        return "AddPassengerCommand usage: "+"add_passenger";
    }
}
