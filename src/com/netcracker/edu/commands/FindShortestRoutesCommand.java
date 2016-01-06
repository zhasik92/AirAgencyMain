package com.netcracker.edu.commands;

import com.netcracker.edu.bobjects.City;
import com.netcracker.edu.bobjects.User;
import com.netcracker.edu.dao.DAOFactory;
import com.netcracker.edu.dao.DAObject;
import com.netcracker.edu.util.shortestpathalgo.DijkstraAlgorithm2;
import com.netcracker.edu.util.shortestpathalgo.Flight;
import com.netcracker.edu.util.shortestpathalgo.TimeTable;
import jdsl.core.api.Dictionary;
import jdsl.core.api.HashComparator;
import jdsl.graph.api.EdgeIterator;
import jdsl.graph.api.Graph;
import jdsl.graph.api.Vertex;
import jdsl.graph.ref.IncidenceListGraph;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Command
 * Created by Zhassulan on 07.12.2015.
 */
public class FindShortestRoutesCommand extends AbstractCommand {
    private static final Logger logger = LogManager.getLogger(FindShortestRoutesCommand.class);
    private static DAObject dao = DAOFactory.getDAObject();
    private /*static*/ Dictionary airportToVertex;
    private  /*static*/ Graph graph;

    public FindShortestRoutesCommand() {
        super(User.Roles.USER);
        graph = initializeGraph();
    }

    @Override
    public String getName() {
        return "find_short_route";
    }

    @Override
    protected int execute(String[] parameters) throws IOException {
        if (parameters == null || parameters.length != 3) {
            logger.error("illegal arguments");
            throw new IllegalArgumentException("required 3 arguments");
        }
        try {
            List<com.netcracker.edu.bobjects.Flight> path = getPath(parameters[0], parameters[1], parameters[2]);
            for (com.netcracker.edu.bobjects.Flight it : path) {
                logger.info(it.getId() + " " + it.getDepartureAirportName()
                        + " " + it.getArrivalAirportName() + " " + it.getDepartureTime() +
                        " " + it.getArrivalTime() + ", price =" + it.getPrice());
            }
            return 0;
        } catch (SQLException sqle) {
            logger.error(sqle);
            return -1;
        }
    }

    public List<com.netcracker.edu.bobjects.Flight> getPath(String from, String to, String time) throws SQLException {
        logger.trace("getPath()");
        int parsedTime = TimeTable.parseTime(time);
        DijkstraAlgorithm2 alg = new DijkstraAlgorithm2();
        EdgeIterator pathIterator;

        Vertex fromVertex;
        Vertex toVertex;
        //synchronized (airportToVertex) {
        fromVertex = (Vertex) airportToVertex.find(from).element();
        toVertex = (Vertex) airportToVertex.find(to).element();
        //   }
        synchronized (this) {
            alg.execute(graph, fromVertex, toVertex, parsedTime);
        }
        pathIterator = alg.reportPath();
        List<com.netcracker.edu.bobjects.Flight> result = new LinkedList<>();
        while (pathIterator.hasNext()) {
            result.add(dao.findFlightById(BigInteger.valueOf(Long.parseLong(((Flight) pathIterator.nextEdge().element()).name))));
        }
        return result;
    }

    @Override
    public String getHelp() {
        return "Usage: " + getName() + " departureAirport" + " arrivalAirport" + " startTime(\"hh:mm\")";
    }

    private synchronized Graph initializeGraph() {
        System.out.println("shortest initializeGraph()");

        graph = new IncidenceListGraph();
        HashComparator comp = new jdsl.core.ref.ObjectHashComparator();
        airportToVertex = new jdsl.core.ref.HashtableDictionary(comp);
        try {
            for (City it : dao.getAllCities()) {
                airportToVertex.insert(it.getName(), graph.insertVertex(it));
            }
            for (com.netcracker.edu.bobjects.Flight it : dao.getAllFlights()) {
                Vertex from, to;
                Flight edge = new Flight(it.getId().toString(), it.getDepartureAirportName(), it.getArrivalAirportName(), it.getDepartureTime(), it.getArrivalTime());
                from = (Vertex) airportToVertex.find(it.getDepartureAirportName()).element();
                to = (Vertex) airportToVertex.find(it.getArrivalAirportName()).element();
                graph.insertDirectedEdge(from, to, edge);
            }
            return graph;
        } catch (SQLException sqle) {
            logger.error(sqle);
            return null;
        }
    }

    public synchronized void actualizeGraph(){
        graph=initializeGraph();
    }
}
