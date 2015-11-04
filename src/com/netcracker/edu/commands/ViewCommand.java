package com.netcracker.edu.commands;


import com.netcracker.edu.bobjects.Passenger;
import com.netcracker.edu.dao.DAObject;

import java.io.IOException;


/**
 * Created by Zhassulan on 24.10.2015.
 */

//!!! This class is only for testing, i'll remove it later
public class ViewCommand extends AbstractCommand {
    @Override
    public String getName() {
        return "view";
    }

    @Override
    public int execute(String[] parameters) throws IOException {
        DAObject dao=DAObject.getInstance();
        for (Passenger it: dao.getAllPassengers()) {
            System.out.println(it);
        }
        return 0;
    }
}
