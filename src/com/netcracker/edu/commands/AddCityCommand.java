package com.netcracker.edu.commands;

import com.netcracker.edu.bobjects.City;
import com.netcracker.edu.dao.DAObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Zhassulan on 03.11.2015.
 */
public class AddCityCommand extends AbstractCommand {
    @Override
    public String getName() {
        return "addcity";
    }

    @Override
    public int execute(String[] parameters) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter name of City:");
        String cityName = br.readLine();

        DAObject dao = DAObject.getInstance();
        City city = dao.findCityByName(cityName);
        if (city != null) {
            System.out.println("City already exist");
            return 0;
        }

        city = new City(cityName);
        dao.addCity(city);
        return 0;
    }
}
