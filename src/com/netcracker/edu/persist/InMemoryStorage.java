package com.netcracker.edu.persist;

import com.netcracker.edu.bobjects.Passenger;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Created by Zhassulan on 23.10.2015.
 */
public class InMemoryStorage implements Serializable{
    private static HashSet<Passenger> passengers;
    private InMemoryStorage(){
    }
    public static synchronized HashSet<Passenger> getPassengers(){
        if(passengers==null){
            passengers=new HashSet<>();
        }
        return passengers;
    }
}
