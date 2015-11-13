package com.netcracker.edu.console;

import com.netcracker.edu.commands.AbstractCommand;
import com.netcracker.edu.commands.CommandsEngine;

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
            while (true) {
                System.out.println("write command");
                String[] splittedCommand = bufferedReader.readLine().toLowerCase().split(" ");
                AbstractCommand command=CommandsEngine.getInstance().getCommand(splittedCommand[0]);
                if(command!=null){
                    command.execute(Arrays.copyOfRange(splittedCommand, 1, splittedCommand.length));
                    continue;
                }
                if(splittedCommand[0].equals("help")){
                    CommandsEngine.getInstance().getHelp();
                    continue;
                }
                System.out.println("Unsupported command");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
