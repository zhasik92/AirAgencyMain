package com.netcracker.edu.commands;

import java.io.IOException;

/**
 * Created by Zhassulan on 23.10.2015.
 */
public class ExitCommand extends AbstractCommand {
    @Override
    public int execute(String[] parameters) throws IOException {
        System.exit(0);
        return 0;
    }
}
