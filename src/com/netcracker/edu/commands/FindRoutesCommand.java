package com.netcracker.edu.commands;

import com.netcracker.edu.bobjects.City;
import com.netcracker.edu.bobjects.Flight;
import com.netcracker.edu.bobjects.User;
import com.netcracker.edu.dao.DAOFactory;
import com.netcracker.edu.dao.DAObject;
import com.netcracker.edu.util.cheapestpathalgo.DijkstraAlgorithm;
import com.netcracker.edu.util.cheapestpathalgo.Edge;
import com.netcracker.edu.util.cheapestpathalgo.Graph;
import com.netcracker.edu.util.cheapestpathalgo.Vertex;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * Command
 * Created by Zhassulan on 05.11.2015.
 */
public class FindRoutesCommand extends AbstractCommand {
    private static final Logger logger = LogManager.getLogger(FindRoutesCommand.class);
    private static DAObject dao = DAOFactory.getDAObject();
    private List<Vertex> nodes;
    private List<Edge> edges;

    public FindRoutesCommand() {
        super(User.Roles.USER);
        initializeNodesAndEdges();
    }

    @Override
    public String getName() {
        return "find_routes";
    }

    @Override
    protected int execute(String[] parameters) throws IOException {
        if (parameters == null || parameters.length != 2) {
            logger.error("illegal arguments");
            throw new IllegalArgumentException("required 2 arguments");
        }
        try {
            LinkedList<Flight> path = getPath(parameters[0].toLowerCase(), parameters[1].toLowerCase());
            path.forEach(logger::info);
            return 0;
        } catch (SQLException sqle) {
            logger.error(sqle);
            return -1;
        }

    }

    private synchronized void initializeNodesAndEdges() {
        logger.trace("initializeNodesAndEdges()");
        System.out.println("initializeNodesAndEdges()");
        nodes = new LinkedList<>();
        edges = new ArrayList<>();
        Map<String, Vertex> tempNodes = new HashMap<>();

        //initializing nodes
        try {
            for (City it : dao.getAllCities()) {
                Vertex location = new Vertex(it.getName().toLowerCase(), it.getName().toLowerCase());
                tempNodes.put(location.getId(), location);
                nodes.add(location);
                logger.trace("location has been added to nodes, id = " + location.getId());
            }

            //initializing edges
            for (Flight it : dao.getAllFlights()) {
                Edge edge = new Edge(it.getId(), tempNodes.get(it.getDepartureAirportName().toLowerCase()), tempNodes.get(it.getArrivalAirportName().toLowerCase()), it.getPrice());
                edges.add(edge);
                logger.trace("edge has been added to edges, id = " + edge.getId());
            }
        } catch (SQLException sqle) {
            logger.error(sqle);
        }
    }

    public LinkedList<Flight> getPath(String from, String to) throws SQLException {
        logger.trace("getPath()");
        if ((dao.findCityByName(from)) == null || (dao.findCityByName(to)) == null) {
            throw new IllegalArgumentException("Cities have not found");
        }
        LinkedList<Flight> flights = new LinkedList<>();
        Map<String, Vertex> tempNodes = new HashMap<>();

        for (Vertex it : nodes) {
            tempNodes.put(it.getId(), it);
        }
        Graph graph = new Graph(nodes, edges);
        logger.trace("Graph constructed");
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        dijkstra.execute(tempNodes.get(from.toLowerCase()));
        logger.trace("Dijkstra executed");
        LinkedList<Edge> path = dijkstra.getPathInEdgesRepresentation(tempNodes.get(to.toLowerCase()));
        for (Edge it : path) {
            flights.add(dao.findFlightById(it.getId()));
            logger.trace("added flight to path, id = " + it.getId());
        }
        return flights;
    }

    public void actualizeGraph() {
        initializeNodesAndEdges();
    }

    @Override
    public String getHelp() {
        return "FindRoutesCommand usage: " + getName() + " departureCity arrivalCity";
    }

}
