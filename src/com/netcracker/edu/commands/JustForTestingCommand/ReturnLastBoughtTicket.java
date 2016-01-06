package com.netcracker.edu.commands.JustForTestingCommand;

import com.netcracker.edu.bobjects.User;
import com.netcracker.edu.commands.AbstractCommand;
import com.netcracker.edu.dao.DAOFactory;
import com.netcracker.edu.dao.DAObject;

import java.io.IOException;

/**
 * Created by Zhassulan on 04.12.2015.
 */
public class ReturnLastBoughtTicket extends AbstractCommand {
    private static DAObject dao = DAOFactory.getDAObject();

    public ReturnLastBoughtTicket() {
        super(User.Roles.USER);
    }

    @Override
    public String getName() {
        return "return_last";
    }

    @Override
    public int execute(String[] parameters, User user) throws IOException {
        throw new IOException("return ticket command");
        /*if (user == null) {
            throw new AccessControlException("access denied");
        }
        if (parameters[0].equals("all")) {
            Set<BigInteger> tickets = user.getTickets();
            Ticket buf;
            for (BigInteger it : tickets) {
                try{
                if ((buf = dao.findTicketById(it)) != null) {
                    synchronized (this) {
                        buf.setStatus(true);
                    }
                }}catch (SQLException e){
                    e.printStackTrace();
                    return -1;
                }

                *//*for (BigInteger it : tickets) {
                    String[] strings = {it.toString()};
                    CommandsEngine.getInstance().getCommand("return").execute(strings, user);
                }*//*
            }
            return 0;
        } else {
            BigInteger max = BigInteger.ZERO;
            Set<BigInteger> tickets = user.getTickets();
            synchronized (user.getTickets()) {
                for (BigInteger it : tickets) {
                    if (it.compareTo(max) > 0) {
                        max = it;
                    }
                }
            }
            String[] strings = {max.toString()};

            return CommandsEngine.getInstance().getCommand("return").execute(strings, user);
        }*/
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
