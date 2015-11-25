package com.netcracker.edu.console;

import com.netcracker.edu.commands.AbstractCommand;
import com.netcracker.edu.commands.CommandsEngine;
import com.netcracker.edu.session.SecurityContextHolder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Created by Zhassulan on 23.10.2015.
 */
public class Main {
    private static final Logger logger = LogManager.getLogger("Main");

    public static void main(String[] args) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                logger.info("write command");
                String[] splittedCommand = bufferedReader.readLine().split(" ");
                AbstractCommand command = CommandsEngine.getInstance().getCommand(splittedCommand[0].toLowerCase());
                if (command != null) {
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
