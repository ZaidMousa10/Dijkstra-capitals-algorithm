package com.example.algo_proj3;

public class Hash {
    private int tableSize;
    private Vertex[] hash;
    private int elements;

    public Hash(int tableSize) {
        this.tableSize = tableSize;
        hash = new Vertex[tableSize];
        elements = 0;
    }

    public int getTableSize() {
        return tableSize;
    }

    public void setTableSize(int tableSize) {
        this.tableSize = tableSize;
    }

    public Vertex[] getHash() {
        return hash;
    }

    public void setHash(Vertex[] hash) {
        this.hash = hash;
    }

    private int hashFunction(String capital) {
        int index = capital.hashCode() % tableSize;
        if (index < 0) {
            index += tableSize;
        }
        return index;
    }

    public void add(Vertex v) {
        int index = hashFunction(v.getCapital().getCapitalName());
        while (hash[index] != null) {
            index = (index + 1) % tableSize;
        }
        hash[index] = v;
        elements++;
    }

    public boolean contains(Vertex v) {
        int index = hashFunction(v.getCapital().getCapitalName());
        while (hash[index] != null) {
            if (hash[index].equals(v)) {
                return true;
            }
            index = (index + 1) % tableSize;
        }
        return false;
    }

    public Vertex get(Vertex v) {
        int index = hashFunction(v.getCapital().getCapitalName());
        while (hash[index] != null) {
            if (hash[index].equals(v)) {
                return hash[index];
            }
            index = (index + 1) % tableSize;
        }
        return null;
    }

    public Vertex get(String name) {
        int index = hashFunction(name);
        while (hash[index] != null) {
            if (hash[index].getCapital().getCapitalName().equals(name)) {
                return hash[index];
            }
            index = (index + 1) % tableSize;
        }
        return null;
    }

    public void print() {
        for (int i = 0; i < tableSize; i++) {
            System.out.println(hash[i]);
        }
    }

    public void setVerticesUnknown() {
        for (int i = 0; i < tableSize; i++) {
            if (hash[i] != null) {
                hash[i].setKnown(false);
            }
        }
    }
}