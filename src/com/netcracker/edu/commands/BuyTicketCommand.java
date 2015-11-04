package com.netcracker.edu.commands;

import com.netcracker.edu.bobjects.Ticket;
import com.netcracker.edu.dao.DAObject;
import com.netcracker.edu.util.IdGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;

/**
 * Created by Zhassulan on 30.10.2015.
 */
public class BuyTicketCommand extends AbstractCommand {
    @Override
    public String getName() {
        return "buy_ticket";
    }

    @Override
    public int execute(String[] parameters) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        DAObject dao =DAObject.getInstance();

        System.out.println("Write passenger's id:");
        BigInteger passenger =new BigInteger(br.readLine());
        if(dao.findPassengerById(passenger)==null){
            System.out.println("Oops, Passenger not found");
            return 1;
        }
        System.out.println("Flight id:");
        BigInteger flight=new BigInteger(br.readLine());
        if(dao.findFlightById(flight)==null){
            System.out.println("Oops, Flight not found");
            return 1;
        }

        Ticket ticket = new Ticket(IdGenerator.getInstance().getId(),passenger,flight,false);
        dao.addTicket(ticket);
        return 0;
    }
}
