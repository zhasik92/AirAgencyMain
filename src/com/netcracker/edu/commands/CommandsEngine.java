package com.netcracker.edu.commands;

import com.netcracker.edu.commands.JustForTestingCommand.ReturnLastBoughtTicket;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  Created by Zhassulan on 23.10.2015.
 */
public final class CommandsEngine {
    private static final Logger logger = LogManager.getLogger(CommandsEngine.class);
    private static CommandsEngine instance;
    private Map<String, AbstractCommand> mapWithCommands;

    private CommandsEngine() {
        {
            List<AbstractCommand> listOfCommands = new ArrayList<>();
            listOfCommands.add(new AddAirplaneCommand());
            listOfCommands.add(new AddCityCommand());
            listOfCommands.add(new AddFlightCommand());
            listOfCommands.add(new AddPassengerCommand());
            listOfCommands.add(new BuyTicketCommand());
            listOfCommands.add(new BuyTicketToShortestRouteCommand());
            listOfCommands.add(new ExitCommand());
            listOfCommands.add(new FindRoutesCommand());
            listOfCommands.add(new FindShortestRoutesCommand());
            listOfCommands.add(new RegisterCommand());
            listOfCommands.add(new ReturnTicketCommand());
            listOfCommands.add(new SignInCommand());
            listOfCommands.add(new ViewCommand());
            listOfCommands.add(new ReturnLastBoughtTicket());
            listOfCommands.add(new QuitCommand());
            listOfCommands.add(new ActualizeGraphCommand());

            mapWithCommands = new HashMap<>();
            for (AbstractCommand it : listOfCommands) {
                mapWithCommands.put(it.getName().toLowerCase(), it);
            }
            logger.trace("commands engine constructed");
        }
    }

    // TODO: 04.01.2016 check singleton
    public synchronized static CommandsEngine getInstance() {
        if (instance == null) {
            instance = new CommandsEngine();
        }
        return instance;
    }

    public AbstractCommand getCommand(String commandName) {
        return mapWithCommands.get(commandName);
    }
    public void getHelp(){
        mapWithCommands.forEach((k,v)->logger.info(v.getHelp()));
    }
}
