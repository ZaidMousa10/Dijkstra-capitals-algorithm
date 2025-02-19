package com.example.algo_proj3;

public class Node {
    private Edge element;
    private Node next;

    public Node(Edge element) {
        this(element, null);
    }

    public Node(Edge element, Node next) {
        this.element = element;
        this.next = next;

    }

    public Edge getElement() {
        return element;
    }

    public void setElement(Edge element) {
        this.element = element;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

}