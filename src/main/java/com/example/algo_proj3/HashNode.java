package com.example.algo_proj3;

public class HashNode {
    private Vertex data;
    private char flag;

    public HashNode(Vertex data, char flag) {
        this.data = data;
        this.flag = flag;
    }

    public Vertex getData() {
        return data;
    }

    public char getFlag() {
        return flag;
    }

    public void setData(Vertex data) {
        this.data = data;
    }

    public void setFlag(char flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return data.getCapital().getCapitalName();
    }
}
