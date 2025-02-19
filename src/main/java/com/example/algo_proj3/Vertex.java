package com.example.algo_proj3;

public class Vertex {
    private Capitals capital;
    private LinkedList edges;
    private boolean known;
    private int id;


    public Vertex() {
        capital = null;
        edges = new LinkedList();
        known = false;
    }

    public Vertex(Capitals capital) {
        this.capital = capital;
        edges = new LinkedList();
        known = false;
    }

    public Capitals getCapital() {
        return capital;
    }

    public LinkedList getEdge() {
        return edges;
    }

    public boolean isKnown() {
        return known;
    }

    public void setKnown(boolean known) {
        this.known = known;
    }

    public void addEdge(Edge edge) {
        edges.add(new Node(edge));
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return capital.getCapitalName().hashCode();
    }

    @Override
    public String toString() {
        return capital.getCapitalName();
    }
}