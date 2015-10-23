package com.netcracker.edu.commands;

import java.io.IOException;

/**
 * Created by Zhassulan on 23.10.2015.
 */
public class HelloWorldCommand extends AbstractCommand {

    @Override
    public int execute(String[] parameters) throws IOException{
        System.out.println("started execute HelloWorldCommand");
        for (int i = 0; i <parameters.length ; i++) {
            System.out.println("Parametr["+i+"]= "+parameters[i]);
        }
        return 0;
    }
}
