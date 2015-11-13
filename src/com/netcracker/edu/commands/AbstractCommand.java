package com.netcracker.edu.commands;

import java.io.IOException;

/**
 * Created by Zhassulan on 23.10.2015.
 */
public abstract class AbstractCommand {
    public abstract String getName();
    public abstract int execute(String[] parameters) throws IOException;
    public abstract String getHelp();
}
