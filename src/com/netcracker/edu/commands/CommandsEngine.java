package com.netcracker.edu.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zhassulan on 23.10.2015.
 */
public final class CommandsEngine {
    private static CommandsEngine instance;
    private Map<String, AbstractCommand> mapWithCommands;

    private CommandsEngine() {
        {
            List<AbstractCommand> listOfCommands = new ArrayList<>();
            listOfCommands.add(new AddAirplaneCommand());
            listOfCommands.add(new AddCityCommand());
            listOfCommands.add(new AddFlightCommand());
            listOfCommands.add(new BuyTicketCommand());
            listOfCommands.add(new AddPassengerCommand());
            listOfCommands.add(new ExitCommand());
            listOfCommands.add(new ViewCommand());

            mapWithCommands = new HashMap<>();
            for (AbstractCommand it : listOfCommands) {
                mapWithCommands.put(it.getName().toLowerCase(), it);
            }
        }
    }

    public synchronized static CommandsEngine getInstance() {
        if (instance == null) {
            instance = new CommandsEngine();
        }
        return instance;
    }

    public AbstractCommand getCommand(String commandName) {
        return mapWithCommands.get(commandName);

    }
}
