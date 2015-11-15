package com.netcracker.edu.commands;

import com.netcracker.edu.bobjects.Ticket;
import com.netcracker.edu.dao.DAObject;

import java.io.IOException;
import java.math.BigInteger;

/**
 * Created by Zhassulan on 15.11.2015.
 */
public class ReturnTicketCommand extends AbstractCommand {

    @Override
    public String getName() {
        return "return";
    }

    @Override
    public int execute(String[] parameters) throws IOException {
        if(parameters==null||parameters.length!=1){
            throw new IllegalArgumentException();
        }
        BigInteger ticketId=BigInteger.valueOf(Long.parseLong(parameters[0]));
        returnTicket(ticketId);
        return 0;
    }

    public void returnTicket(BigInteger ticketId){
        if(ticketId==null||ticketId.compareTo(BigInteger.ZERO)<0){
            throw new IllegalArgumentException();
        }
        DAObject dao=DAObject.getInstance();
        Ticket ticket=dao.findTicketById(ticketId);
        if(ticket==null){
            System.out.println("ticket not found");
            return;
        }
        ticket.setStatus(true);
    }

    @Override
    public String getHelp() {
        return null;
    }
}
