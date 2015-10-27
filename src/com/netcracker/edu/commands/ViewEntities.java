package com.netcracker.edu.commands;


import com.netcracker.edu.bobjects.Passenger;
import com.netcracker.edu.dao.DAObject;

import java.io.IOException;


/**
 * Created by Zhassulan on 24.10.2015.
 */

//this class is only for testing, i'll remove it later
public class ViewEntities extends AbstractCommand {
    @Override
    public int execute(String[] parameters) throws IOException {
        DAObject dao=DAObject.getInstance();
        for (Passenger it: dao.getAllPassengers()) {
            System.out.println("Passenger id: "+it.getId());
            System.out.println("passport: "+it.getPassportNumber());
            System.out.println("citizenship: "+it.getCitizenship());
            System.out.println("dateOfBirth: "+it.getDateOfBirth());
            System.out.println("firstName: "+it.getFirstName());
            System.out.println("lastName: "+it.getLastName());
        }
        return 0;
    }
}
