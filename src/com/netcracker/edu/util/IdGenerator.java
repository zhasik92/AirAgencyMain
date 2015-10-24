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
            File file = new File("UtilData.out");
            if (file.exists()) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
                    idCounter = BigInteger.valueOf(Long.parseLong(br.readLine()));
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                } catch (NumberFormatException nfe) {
                    idCounter = BigInteger.ZERO;
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
        System.out.println(idCounter.intValue());
        return idCounter;
    }
}
