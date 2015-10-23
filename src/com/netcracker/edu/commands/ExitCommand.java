package com.netcracker.edu.commands;

import com.netcracker.edu.persist.InMemoryStorageManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by Zhassulan on 23.10.2015.
 */
public class ExitCommand extends AbstractCommand {
    @Override
    public int execute(String[] parameters) throws IOException {
        FileOutputStream fos = new FileOutputStream("InMemoryStorage.out");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(InMemoryStorageManager.getInstance().getStorage());
        oos.flush();
        oos.close();
        System.exit(0);
        return 0;
    }
}
