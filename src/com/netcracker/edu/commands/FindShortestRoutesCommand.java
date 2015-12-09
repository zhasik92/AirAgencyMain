package com.netcracker.edu.commands;

import com.netcracker.edu.bobjects.City;
import com.netcracker.edu.bobjects.User;
import com.netcracker.edu.dao.DAObject;
import com.netcracker.edu.dao.DAObjectFromSerializedStorage;
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
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Zhassulan on 07.12.2015.
 */
public class FindShortestRoutesCommand extends AbstractCommand {
    private static final Logger logger = LogManager.getLogger(FindShortestRoutesCommand.class);
    private static DAObject dao = DAObjectFromSerializedStorage.getInstance();
    private static HashComparator comp;
    private static Dictionary airportToVertex;
    private static Graph graph;

    public FindShortestRoutesCommand() {
        super(User.Roles.USER);
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

        List<com.netcracker.edu.bobjects.Flight> path = getPath(parameters[0], parameters[1], parameters[2]);
/*
        DijkstraAlgorithm2 alg = new DijkstraAlgorithm2();

        alg.execute(initializeGraph(),
                (Vertex) airportToVertex.find(parameters[0]).element(),
                (Vertex) airportToVertex.find(parameters[1]).element(), TimeTable.parseTime(parameters[2]));

        EdgeIterator pathIterator = alg.reportPath();
        while (pathIterator.hasNext()) {
            Edge it = pathIterator.nextEdge();

            logger.info(((Flight) it.element()).srcAirport + " " + " "
                    + ((Flight) it.element()).depTime + ((Flight) it.element()).destAirport +
                    " " + ((Flight) it.element()).arrTime);
        }*/
        for (com.netcracker.edu.bobjects.Flight it : path) {
            logger.info(it.getId() + " " + it.getDepartureAirportName()
                    + " " + it.getArrivalAirportName() + " " + it.getDepartureTime() +
                    " " + it.getArrivalTime() + ", price =" + it.getPrice());
        }
        return 0;
    }

    public synchronized List<com.netcracker.edu.bobjects.Flight> getPath(String from, String to, String time) {
        logger.trace("getPath()");
        DijkstraAlgorithm2 alg = new DijkstraAlgorithm2();

            if (graph == null || airportToVertex == null || comp == null) {
                graph = initializeGraph();
            }

        alg.execute(graph,
                (Vertex) airportToVertex.find(from).element(),
                (Vertex) airportToVertex.find(to).element(),
                TimeTable.parseTime(time));
        EdgeIterator pathIterator = alg.reportPath();
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
        comp = new jdsl.core.ref.ObjectHashComparator();
        airportToVertex = new jdsl.core.ref.HashtableDictionary(comp);

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
    }
}
