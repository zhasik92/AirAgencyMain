package com.netcracker.edu.console;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by Zhassulan on 23.10.2015.
 */
public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            Config.execute();
        } catch (IOException ioe) {
            logger.error(ioe);
            ioe.printStackTrace();
        }
    }
}
