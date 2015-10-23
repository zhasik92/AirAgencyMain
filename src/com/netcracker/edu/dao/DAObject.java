package com.netcracker.edu.dao;

import com.netcracker.edu.bobjects.Passenger;
import com.netcracker.edu.persist.InMemoryStorage;

import java.util.Set;

/**
 * Created by Zhassulan on 23.10.2015.
 */
public class DAObject {
    public Passenger findPassenger(String passportNumber,String citizenship){
        Set<Passenger> passengers=InMemoryStorage.getPassengers();
        for (Passenger it:passengers) {
            if(passportNumber.equals(it.getPassportNumber())&&citizenship.equals(it.getCitizenship())){
                return it;
            }
        }
        return null;
    }
    public void persist(Passenger passenger){
        InMemoryStorage.getPassengers().add(passenger);
    }
}
