package com.netcracker.edu.bobjects;


import java.io.Serializable;

/**
 *  Created by Zhassulan on 20.10.2015.
 */
public class Airplane extends NamedObject implements Serializable {
    private int capacity;

    public Airplane(String name, int capacity) {
        super(name);
        setCapacity(capacity);
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Capasity can't be negative");
        }
        this.capacity = capacity;
    }
}
