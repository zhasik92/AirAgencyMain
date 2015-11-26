package com.netcracker.edu.console;

import com.netcracker.edu.commands.AbstractCommand;
import com.netcracker.edu.commands.CommandsEngine;
import com.netcracker.edu.session.SecurityContextHolder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Arrays;

/**
 *
 * Created by Zhassulan on 25.11.2015.
 */

//// TODO: 26.11.2015 Rename class
public class Config {
    private final static Logger logger = LogManager.getLogger(Config.class);

    public static void execute() throws IOException {
        InputStreamReader inputStreamReader;
        File fileWithCommands = new File("buying_ticket_scenario.txt");

        if (fileWithCommands.exists() && !fileWithCommands.isDirectory()) {
            inputStreamReader = new FileReader(fileWithCommands);
        } else {
            inputStreamReader = new InputStreamReader(System.in);
        }
        try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            //noinspection InfiniteLoopStatement
            while (true) {
                logger.info("write command");
                String[] splittedCommand = bufferedReader.readLine().split(" ");
                AbstractCommand command = CommandsEngine.getInstance().getCommand(splittedCommand[0].toLowerCase());
                if (command != null) {
                    logger.info(Arrays.toString(splittedCommand));
                    command.execute(Arrays.copyOfRange(splittedCommand, 1, splittedCommand.length), SecurityContextHolder.getLoggedHolder());
                    continue;
                }
                if (splittedCommand[0].equals("help")) {
                    CommandsEngine.getInstance().getHelp();
                    continue;
                }
                logger.warn("Unsupported command");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }
    }
}

