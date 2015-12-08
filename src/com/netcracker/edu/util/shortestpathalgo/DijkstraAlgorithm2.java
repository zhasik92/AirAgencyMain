package com.netcracker.edu.util.shortestpathalgo;

import jdsl.graph.algo.IntegerDijkstraPathfinder;
import jdsl.graph.api.*;

/**
 * Created by Zhassulan on 05.12.2015.
 */
public class DijkstraAlgorithm2 extends IntegerDijkstraPathfinder {

    private int startTime;
    @Override
    protected int weight(Edge e) {
        // the flightspecs for the flight along Edge e
        Flight eFS = (Flight) e.element();
        int connectingTime = TimeTable.diff(eFS.depTime, startTime + distance(g_.origin(e)));
        return connectingTime + eFS.flightDuration();
    }
    public void execute(InspectableGraph g, Vertex source, Vertex dest, int startTime) throws InvalidVertexException {
        this.startTime= startTime;
        super.execute(g, source, dest);
    }
    //e7.7

  /* ************************************ */
  /* Members not described in the lesson. */
  /* ************************************ */

    protected EdgeIterator incidentEdges(Vertex v) {
        return g_.incidentEdges(v, EdgeDirection.OUT);
    }

}
