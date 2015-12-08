package com.netcracker.edu.util.cheapestpathalgo;

import java.math.BigInteger;

/**
 * Created by Zhassulan on 11.11.2015.
 */
public class Edge {
    private final BigInteger id;
    private final Vertex from;
    private final Vertex to;
    private final double weight;

    public Edge(BigInteger id, Vertex from, Vertex to, double weight) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public BigInteger getId() {
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
        return from + " " + to +" : "+weight;
    }
}
