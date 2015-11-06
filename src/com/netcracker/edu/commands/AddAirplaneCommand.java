package com.netcracker.edu.commands;

import com.netcracker.edu.bobjects.Airplane;
import com.netcracker.edu.dao.DAObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Zhassulan on 03.11.2015.
 */
public class AddAirplaneCommand extends AbstractCommand {
    @Override
    public String getName() {
        return "add_airplane";
    }

    @Override
    public int execute(String[] parameters) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter name of Airplane:");
        String airplaneName = br.readLine();

        DAObject dao = DAObject.getInstance();
        Airplane airplane = dao.findAirplaneByName(airplaneName);
        if (airplane != null) {
            System.out.println("Airplane already exist");
            return 0;
        }
        System.out.println("Write capacity:");
        Integer capacity=Integer.parseInt(br.readLine());

        airplane = new Airplane(airplaneName,capacity);
        dao.addAirplane(airplane);
        return 0;
    }
}
