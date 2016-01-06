package com.netcracker.edu.commands;

import com.netcracker.edu.bobjects.User;
import com.netcracker.edu.dao.InMemoryDAO;
import com.netcracker.edu.persist.InMemoryStorage;
import com.netcracker.edu.util.IdGenerator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;

/**
 * Command
 * Created by Zhassulan on 23.10.2015.
 */
public class ExitCommand extends AbstractCommand {
    private static final Logger logger = LogManager.getLogger(ExitCommand.class);

    public ExitCommand() {
        super(User.Roles.USER);
    }

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    protected int execute(String[] parameters) throws IOException {
        InMemoryStorage storage = InMemoryDAO.getInstance().getStorage();
        FileOutputStream fos = new FileOutputStream("InMemoryStorage.out");
        logger.trace("FOS created(InMemoryStorage.out)");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        logger.trace("OOS created");
        oos.writeObject(storage);
        oos.flush();
        logger.trace("storage wrote to file");
        oos.close();
        logger.trace("FOS closed");

        BigInteger id = IdGenerator.getInstance().getId();
        fos = new FileOutputStream("idGen.out");
        oos = new ObjectOutputStream(fos);
        logger.trace("OOS created (idGen.out)");
        oos.writeObject(id);
        oos.flush();
        logger.trace("idGen wrote to file");
        oos.close();
        logger.trace("exit");
        System.exit(0);
        return 0;
    }

    @Override
    public String getHelp() {
        return "ExitCommand usage: " + getName();
    }
}
