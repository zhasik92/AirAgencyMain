package com.netcracker.edu.commands.JustForTestingCommand;

import com.netcracker.edu.bobjects.User;
import com.netcracker.edu.commands.AbstractCommand;
import com.netcracker.edu.commands.CommandsEngine;
import com.netcracker.edu.dao.DAObject;
import com.netcracker.edu.dao.DAObjectFromSerializedStorage;

import java.io.IOException;
import java.math.BigInteger;
import java.security.AccessControlException;
import java.util.Set;

/**
 * Created by Zhassulan on 04.12.2015.
 */
public class ReturnLastBoughtTicket extends AbstractCommand {
    private static DAObject dao = DAObjectFromSerializedStorage.getInstance();

    public ReturnLastBoughtTicket() {
        super(User.Roles.USER);
    }

    @Override
    public String getName() {
        return "return_last";
    }

    @Override
    public int execute(String[] parameters, User user) throws IOException {
        if (user == null) {
            throw new AccessControlException("access denied");
        }
        if (parameters[0].equals("all")) {
            BigInteger max = BigInteger.ZERO;
            synchronized (this) {
                Set<BigInteger> tickets = user.getTickets();
                for (BigInteger it : tickets) {
                    String[] strings = {it.toString()};
                    CommandsEngine.getInstance().getCommand("return").execute(strings, user);
                }
            }
        } else {
            BigInteger max = BigInteger.ZERO;
            synchronized (this) {
                Set<BigInteger> tickets = user.getTickets();
                for (BigInteger it : tickets) {
                    if (it.compareTo(max) > 0) {
                        max = it;
                    }
                }
            }
            String[] strings = {max.toString()};

            return CommandsEngine.getInstance().getCommand("return").execute(strings, user);
        }
        return -1;
    }

    @Override
    protected int execute(String[] parameters) throws IOException {
        return 0;
    }

    @Override
    public String getHelp() {
        return null;
    }
}
