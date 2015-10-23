package com.netcracker.edu.commands;

import java.io.IOException;

/**
 * Created by Zhassulan on 23.10.2015.
 */
public class ExitCommand extends AbstractCommand {
    @Override
    public int execute(String[] parameters) throws IOException {
      //  FileOutputStream fos = new FileOutputStream("InMemoryStorage.out");
        //ObjectOutputStream oos = new ObjectOutputStream(fos);
        //oos.writeObject();
       // oos.flush();
        //oos.close();
        System.exit(0);
        return 0;
    }
}
