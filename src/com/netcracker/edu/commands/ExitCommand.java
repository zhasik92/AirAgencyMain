package com.netcracker.edu.commands;

import com.netcracker.edu.dao.DAObject;
import com.netcracker.edu.persist.InMemoryStorage;
import com.netcracker.edu.util.IdGenerator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;

/**
 * Created by Zhassulan on 23.10.2015.
 */
public class ExitCommand extends AbstractCommand {
    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public int execute(String[] parameters) throws IOException {
        try {
            InMemoryStorage storage=DAObject.getInstance().getStorage();
            FileOutputStream fos = new FileOutputStream("InMemoryStorage.out");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(storage);
            oos.flush();
            oos.close();

            BigInteger id = IdGenerator.getInstance().getId();
            fos = new FileOutputStream("idGen.out");
            oos = new ObjectOutputStream(fos);
            oos.writeObject(id);
            oos.flush();
            oos.close();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public String getHelp() {
        return "ExitCommand usage: "+"exit";
    }
}
