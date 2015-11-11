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
        DAObject dao = DAObject.getInstance();
        nodes=new LinkedList<>();
        Map<String, Vertex> tempNodes = new HashMap<>();
        edges = new ArrayList<>();
        for (City it : dao.getAllCities()) {
            Vertex location = new Vertex(it.getName(), it.getName());
            tempNodes.put(location.getId(), location);
            nodes.add(location);
        }
        for (Flight it : dao.getAllFlights()) {
            Edge edge = new Edge(it.getId().toString(), tempNodes.get(it.getDepartureAirportName()), tempNodes.get(it.getArrivalAirportName()), it.getPrice());
            edges.add(edge);
        }

        Graph graph = new Graph(nodes, edges);
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        dijkstra.execute(tempNodes.get("Almaty"));
        LinkedList<Vertex> path = dijkstra.getPath(tempNodes.get("New York"));
        for (Vertex vertex : path) {
            System.out.println(vertex);
        }
        return 0;

    }
}
