package com.netcracker.edu.commands;

import com.netcracker.edu.bobjects.Ticket;
import com.netcracker.edu.bobjects.User;
import com.netcracker.edu.dao.DAOFactory;
import com.netcracker.edu.dao.DAObject;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;

/**
 * Command
 * Created by Zhassulan on 15.11.2015.
 */
public class ReturnTicketCommand extends AbstractCommand {
    private static final Logger logger = LogManager.getLogger(ReturnTicketCommand.class);
    private static DAObject dao = DAOFactory.getDAObject();

    public ReturnTicketCommand() {
        super(User.Roles.USER);
    }

    @Override
    public String getName() {
        return "return";
    }

    @Override
    protected int execute(String[] parameters) throws IOException {
        logger.trace("return ticket execute() started");
        if (parameters == null || parameters.length != 1) {
            logger.error("illegal argument");
            throw new IllegalArgumentException();
        }
        BigInteger ticketId = BigInteger.valueOf(Long.parseLong(parameters[0]));
        try {
            return returnTicket(ticketId);
        } catch (SQLException sqle) {
            logger.error(sqle);
            return -1;
        }
    }

    public int returnTicket(BigInteger ticketId) throws SQLException {
        if (ticketId == null || ticketId.compareTo(BigInteger.ZERO) < 0) {
            logger.warn("illegal argument");
            throw new IllegalArgumentException();
        }
        Ticket ticket = dao.findTicketById(ticketId);
        if (ticket == null || ticket.isCanceled()) {
            logger.warn("ticket not found or already returned");
            return 1;
        }
        ticket.setStatus(true);
        dao.updateTicket(ticket);

        logger.info("ticket returned");
        return 0;
    }

    @Override
    public String getHelp() {
        return getName() + " id(long)";
    }
}
