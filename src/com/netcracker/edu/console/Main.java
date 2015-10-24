package com.netcracker.edu.console;

import com.netcracker.edu.commands.AbstractCommand;
import com.netcracker.edu.commands.Commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Created by Zhassulan on 23.10.2015.
 */
public class Main {
    public static void main(String[] args) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            String command;
            String[] splittedCommand;
            while (true) {
                System.out.println("write command");
                command = bufferedReader.readLine();
                splittedCommand = command.split(" ");
                if (Commands.getAllCommands().containsKey(splittedCommand[0])) {
                    Class c = Class.forName(Commands.getAllCommands().get(splittedCommand[0]));
                    AbstractCommand commandToExecute = (AbstractCommand) c.newInstance();
                    commandToExecute.execute(Arrays.copyOfRange(splittedCommand, 1, splittedCommand.length));
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | IOException e) {
            e.printStackTrace();
        }
    }
}
