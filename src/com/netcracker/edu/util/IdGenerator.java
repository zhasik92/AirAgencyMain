package com.netcracker.edu.util;

import java.io.*;
import java.math.BigInteger;

/**
 * Created by Zhassulan on 20.10.2015.
 */
public class IdGenerator {
    private static IdGenerator instance;
    private static volatile BigInteger idCounter;

    private IdGenerator() {
        {
            File file = new File("idGen.out");
            if (file.exists()) {
                try (FileInputStream fis = new FileInputStream(file); ObjectInputStream oin = new ObjectInputStream(fis)) {
                    idCounter = (BigInteger) oin.readObject();
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            } else {
                idCounter = BigInteger.ZERO;
            }

        }
    }

    public static synchronized IdGenerator getInstance() {
        if (instance == null) {
            instance = new IdGenerator();
        }
        return instance;
    }

    public synchronized BigInteger getId() {
        idCounter = idCounter.add(BigInteger.ONE);
        return idCounter;
    }
}
