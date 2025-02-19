package com.example.algo_proj3;

public class TableEntry {
    private Vertex vertex;
    private Vertex path;
    private int cost;
    private int time;
    private int distance;
    private int currCost;
    private LinkedList route;

    public TableEntry(Vertex vertex, Vertex path, int cost, int time, int distance, int currCost) {
        this.vertex = vertex;
        this.path = path;
        this.cost = cost;
        this.time = time;
        this.distance = distance;
        this.currCost = currCost;
    }

    public TableEntry(Vertex vertex, Vertex path, int cost, int time, int distance, int currCost, LinkedList route) {
        this.vertex = vertex;
        this.path = path;
        this.cost = cost;
        this.time = time;
        this.distance = distance;
        this.currCost = currCost;
        this.route = route;
    }

    public LinkedList getRoute() {
        return route;
    }

    public void setRoute(LinkedList route) {
        this.route = route;
    }

    public Vertex getVertex() {
        return vertex;
    }

    public Vertex getPath() {
        return path;
    }

    public int getCost() {
        return cost;
    }

    public int getTime() {
        return time;
    }

    public int getDistance() {
        return distance;
    }

    public int getCurrCost() {
        return currCost;
    }

    public void setVertex(Vertex vertex) {
        this.vertex = vertex;
    }

    public void setPath(Vertex path) {
        this.path = path;
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

    public void setCurrCost(int currCost) {
        this.currCost = currCost;
    }
}
