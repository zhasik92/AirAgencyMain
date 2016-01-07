package com.netcracker.edu.commands;

import com.netcracker.edu.bobjects.User;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by Zhassulan on 05.01.2016.
 */
public class ActualizeGraphCommand extends AbstractCommand {
    private static final Logger logger= LogManager.getLogger(ActualizeGraphCommand.class);
    public ActualizeGraphCommand() {
        super(User.Roles.ADMIN);
    }

    @Override
    public String getName() {
        return "actualize";
    }

    @Override
    protected int execute(String[] parameters) throws IOException {
        FindRoutesCommand findRoutesCommand=(FindRoutesCommand) CommandsEngine.getInstance().getCommand("find_routes");
        findRoutesCommand.actualizeGraph();
        FindShortestRoutesCommand findShortestRoutesCommand=(FindShortestRoutesCommand)CommandsEngine.getInstance().getCommand("find_shortest_route");
        findShortestRoutesCommand.actualizeGraph();
        logger.info("actualize command executed");
        return 0;
    }

    @Override
    public String getHelp() {
        return "updates cities and flights in searching graph";
    }
}
