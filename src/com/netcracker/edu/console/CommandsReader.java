package com.netcracker.edu.console;

import com.netcracker.edu.commands.AbstractCommand;
import com.netcracker.edu.commands.CommandsEngine;
import com.netcracker.edu.session.SecurityContextHolder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Arrays;

/**
 * Created by Zhassulan on 25.11.2015.
 */

//// TODO: 26.11.2015 Rename class
public class CommandsReader {
    private final static Logger logger = LogManager.getLogger(CommandsReader.class);

    public static void readCommandsFromTestFileAndExecute() throws IOException {
        File fileWithCommands = new File("buying_ticket_scenario.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(fileWithCommands))) {
            logger.info("started reading commands from file");
            //noinspection InfiniteLoopStatement
            while (true) {
                String[] command = br.readLine().split(":");
                int executionCode, expectedValue = Integer.parseInt(command[1]);
                if (expectedValue != (executionCode = parseAndExecuteCommand(command[0]))) {
                    logger.error("expected value <" + expectedValue + ">, but was <" + executionCode + ">");
                }
                logger.info("execution code = " + executionCode);
            }
        }
    }

    public static void readCommandsFromConsoleAndExecute() throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            //noinspection InfiniteLoopStatement
            while (true) {
                logger.info("execution code = " + parseAndExecuteCommand(bufferedReader.readLine()));
            }
        }
    }

    private static int parseAndExecuteCommand(String string) throws IOException {
        try {
            String[] splittedCommand = string.toLowerCase().split(" ");
            AbstractCommand command = CommandsEngine.getInstance().getCommand(splittedCommand[0].toLowerCase());
            if (command != null) {
                logger.info(command);
                return command.execute(Arrays.copyOfRange(splittedCommand, 1, splittedCommand.length), SecurityContextHolder.getLoggedHolder());
            }
            if (splittedCommand[0].equals("help")) {
                CommandsEngine.getInstance().getHelp();
                return 0;
            }
            logger.warn("Unsupported command");
            return -5;
        } catch (IllegalArgumentException iae) {
            logger.error(iae);
            return 1;
        }
    }
}

