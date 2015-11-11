package com.netcracker.edu.util;

/**
 * Created by Zhassulan on 11.11.2015.
 */
public class Edge {
    private final String id;
    private final Vertex from;
    private final Vertex to;
    private final double weight;

    public Edge(String id, Vertex from, Vertex to, double weight) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public String getId() {
        return id;
    }
    public Vertex getTo() {
        return to;
    }

    public Vertex getFrom() {
        return from;
    }
    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return from + " " + to;
    }
}
