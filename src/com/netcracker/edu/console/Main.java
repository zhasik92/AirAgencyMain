package com.netcracker.edu.console;

import com.netcracker.edu.connections.Server;
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
            switch (args[0].toLowerCase()) {
                case "server":
                    logger.info("server mode started");
                    Server.execute();
                    break;
                case "test":
                    logger.info("test mode started");
                    CommandsReader.readCommandsFromTestFileAndExecute();
                    break;
                case "console":
                    logger.info("Console mode started");
                    CommandsReader.readCommandsFromConsoleAndExecute();
                    break;
            }
        } catch (IOException ioe) {
            logger.error(ioe);
            ioe.printStackTrace();
        }
    }
}
