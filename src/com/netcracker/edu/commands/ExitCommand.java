package com.netcracker.edu.commands;

import com.netcracker.edu.util.IdGenerator;

import java.io.*;

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
        BufferedWriter br=new BufferedWriter(new OutputStreamWriter(new FileOutputStream("UtilData.out")));
        br.write(IdGenerator.getInstance().getId().intValue()+"\n");
        br.close();
        System.exit(0);
        return 0;
    }
}
