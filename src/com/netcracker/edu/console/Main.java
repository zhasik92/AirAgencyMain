package com.netcracker.edu.console;

import com.netcracker.edu.commands.AbstractCommand;
import com.netcracker.edu.commands.Commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by Zhassulan on 23.10.2015.
 */
public class Main {
    public static void main(String[] args) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Please enter login/email or skip by tapping enter");
            String command = bufferedReader.readLine();
            String[] splittedCommand;
            if ("".equals(command)) {
                while (true) {
                    System.out.println("write command");
                    Map<String, String> mapWithCommands = Commands.getAllCommands();
                    command = bufferedReader.readLine();
                    splittedCommand = command.split(" ");
                    if (mapWithCommands.containsKey(splittedCommand[0])) {
                        Class c = Class.forName(mapWithCommands.get(splittedCommand[0]));
                        AbstractCommand commandToExecute = (AbstractCommand) c.newInstance();
                        commandToExecute.execute(Arrays.copyOfRange(splittedCommand, 1, splittedCommand.length));
                    }
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | IOException e) {
            e.printStackTrace();
        }
    }
}
