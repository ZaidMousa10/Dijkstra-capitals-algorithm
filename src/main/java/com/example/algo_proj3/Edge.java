package com.example.algo_proj3;

public class Edge {
    private Vertex source;
    private Vertex destination;
    private int time;
    private int cost;
    private double distance;

    public Edge(Vertex source, Vertex destination, int time, int cost, double distance) {
        this.source = source;
        this.destination = destination;
        this.time = time;
        this.cost = cost;
        this.distance = distance;
    }

    public Vertex getSource() {
        return source;
    }

    public Vertex getDestination() {
        return destination;
    }

    public int getTime() {
        return time;
    }

    public int getCost() {
        return cost;
    }

    public double getDistance() {
        return distance;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return source.getCapital().getCapitalName() + " -> " + destination.getCapital().getCapitalName() + " Time: " + time + " Cost: " + cost;
    }
}