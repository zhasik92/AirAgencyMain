package com.netcracker.edu.commands;

import com.netcracker.edu.bobjects.City;
import com.netcracker.edu.bobjects.Flight;
import com.netcracker.edu.dao.DAObject;
import com.netcracker.edu.util.DijkstraAlgorithm;
import com.netcracker.edu.util.Edge;
import com.netcracker.edu.util.Graph;
import com.netcracker.edu.util.Vertex;

import java.io.IOException;
import java.util.*;

/**
 * Created by Zhassulan on 05.11.2015.
 */
public class FindRoutesCommand extends AbstractCommand {
    private List<Vertex> nodes;
    private List<Edge> edges;

    @Override
    public String getName() {
        return "find_routes";
    }

    @Override
    public int execute(String[] parameters) throws IOException {
        LinkedList<Flight> path = getPath(parameters[0], parameters[1]);
        path.forEach(System.out::println);
        return 0;

    }

    private void initializeNodesAndEdges() {
        DAObject dao = DAObject.getInstance();
        nodes = new LinkedList<>();
        edges = new ArrayList<>();
        Map<String, Vertex> tempNodes = new HashMap<>();

        //initializing nodes
        for (City it : dao.getAllCities()) {
            Vertex location = new Vertex(it.getName(), it.getName());
            tempNodes.put(location.getId(), location);
            nodes.add(location);
        }

        //initializing edges
        for (Flight it : dao.getAllFlights()) {
            Edge edge = new Edge(it.getId(), tempNodes.get(it.getDepartureAirportName()), tempNodes.get(it.getArrivalAirportName()), it.getPrice());
            edges.add(edge);
        }
    }

    public LinkedList<Flight> getPath(String from, String to) {
        LinkedList<Flight> flights = new LinkedList<>();
        DAObject dao = DAObject.getInstance();
        Map<String, Vertex> tempNodes = new HashMap<>();
        if (nodes == null || edges == null) {
            initializeNodesAndEdges();
        }
        for (Vertex it : nodes) {
            tempNodes.put(it.getId(), it);
        }
        Graph graph = new Graph(nodes, edges);
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        dijkstra.execute(tempNodes.get(from));
        LinkedList<Edge> path = dijkstra.getPathInEdgesRepresentation(tempNodes.get(to));
        for (Edge it : path) {
            flights.add(dao.findFlightById(it.getId()));
        }
        return flights;
    }

    @Override
    public String getHelp() {
        return "FindRoutesCommand usage: " + "find_routes DepartureCity ArrivalCity";
    }

}
