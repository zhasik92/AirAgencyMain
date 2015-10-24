package com.netcracker.edu.dao;

import com.netcracker.edu.bobjects.Passenger;
import com.netcracker.edu.persist.InMemoryStorage;

import java.io.*;
import java.util.Collection;

/**
 * Created by Zhassulan on 23.10.2015.
 */
public class DAObject {
    private static DAObject instance;
    private static InMemoryStorage storage;

    private DAObject() {
        {
            try {
                File file = new File("InMemoryStorage.out");
                if (file.exists()) {
                    FileInputStream fis = new FileInputStream(file);
                    ObjectInputStream oin = new ObjectInputStream(fis);
                    storage = (InMemoryStorage) oin.readObject();
                } else {
                    storage = new InMemoryStorage();
                }
            } catch (ClassNotFoundException | IOException fnfe) {
                fnfe.printStackTrace();
            }
        }
    }

    public static synchronized DAObject getInstance() {
        if (instance == null) {
            instance = new DAObject();
        }
        return instance;
    }

    public Passenger findPassenger(String passportNumber, String citizenship) {
        for (Passenger it : storage.getPassengers()) {
            if (passportNumber.equals(it.getPassportNumber()) && citizenship.equals(it.getCitizenship())) {
                return it;
            }
        }
        return null;
    }

    public synchronized void addPassenger(Passenger passenger) {
        storage.getPassengers().add(passenger);
        try {
            FileOutputStream fos = new FileOutputStream("InMemoryStorage.out");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(storage);
            oos.flush();
            oos.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    //нужно либо удалить этот метод либо возвращать копию коллекции,
    // чтобы избежать изменени€ хранилища напр€мую. ѕозже исправлю
    public  Collection<Passenger> getAllPassengers(){
        return storage.getPassengers();
    }
}
