package com.netcracker.edu.commands;

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
    public int execute(String[] parameters) throws IOException {
        BigInteger id= IdGenerator.getInstance().getId();
        FileOutputStream fos = new FileOutputStream("idGen.out");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(id);
        oos.flush();
        oos.close();
        System.exit(0);
        return 0;
    }
}
