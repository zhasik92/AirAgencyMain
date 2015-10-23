package com.netcracker.edu.util;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by Zhassulan on 20.10.2015.
 */
public class IdGenerator implements Serializable {
    private static IdGenerator instance;
    private static volatile BigInteger idCounter;

    private IdGenerator() {
        idCounter=BigInteger.ZERO;
    }

    public static synchronized IdGenerator getInstance() {
        if (instance == null) {
            instance=new IdGenerator();
        }
        return instance;
    }

    public synchronized BigInteger getId() {
        idCounter = idCounter.add(BigInteger.ONE);
        return idCounter;
    }
}
