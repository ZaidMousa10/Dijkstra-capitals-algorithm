package com.example.algo_proj3;



public class Graph {
    private int numVertices;
    private Hash hash;

    public Graph(int numVertices) {
        this.numVertices = numVertices;
        hash = new Hash(numVertices);
    }

    public void addVertex(Vertex v) {
        hash.add(v);
    }

    public Vertex getVertex(Vertex v) {
        return hash.get(v);
    }

    public Vertex getVertix(String capital) {
        return hash.get(capital);
    }

    public boolean contains(Vertex v) {
        return hash.contains(v);
    }

    public int getNumVertices() {
        return numVertices;
    }

    public void setNumVertices(int numVertices) {
        this.numVertices = numVertices;
    }

    public Hash getHash() {
        return hash;
    }

    public void setHash(Hash hash) {
        this.hash = hash;
    }

    public void printGraph() {
        for (int i = 0; i < numVertices; i++) {
            if (hash.getHash()[i] != null) {
                System.out.println(hash.getHash()[i]);
            }
        }
    }

    /*
     * This method returns the shortest path between two vertices based on the filter
     * provided. The filter can be "Time", "Cost" or "Distance".
     * */
    public TableEntry getResult(Vertex src, Vertex dest, String filter) {
        if (filter.equals("Time")) {
            return timeDijkstra(src, dest);
        } else if (filter.equals("Cost")) {
            return costDijkstra(src, dest);
        } else {
            return distanceDijkstra(src, dest);
        }
    }

    private Vertex smallestUnknownDistanceVertex(TableEntry[] table) {
        int minDist = Integer.MAX_VALUE;
        Vertex minVertex = null;

        for (int i = 1; i <= numVertices; i++) {
            if (table[i] != null && !table[i].getVertex().isKnown() && table[i].getCurrCost() < minDist) {
                minDist = table[i].getCurrCost();
                minVertex = table[i].getVertex();
            }
        }

        return minVertex;
    }

    /*
     * This method returns the shortest path between two vertices based on the time taken to travel
     * between the two vertices.
     * */
    TableEntry timeDijkstra(Vertex src, Vertex dest) {
        // Create an array to store table entries for all vertices.
        TableEntry[] table = new TableEntry[numVertices + 1];

        // Initialize the table with default values for all vertices.
        for (int i = 0; i < numVertices; i++) {
            if (hash.getHash()[i] != null) {
                table[i] = new TableEntry(
                        hash.getHash()[i],  // The vertex being initialized.
                        new Vertex(),       // Default path is a new, unlinked vertex.
                        Integer.MAX_VALUE,  // Default cost set to a large number (infinity).
                        Integer.MAX_VALUE,  // Default time set to a large number (infinity).
                        Integer.MAX_VALUE,  // Default distance set to a large number (infinity).
                        Integer.MAX_VALUE   // Default current cost set to a large number (infinity).
                );
                hash.getHash()[i].setKnown(false); // Mark all vertices as "unknown" (not processed).
                hash.getHash()[i].setId(i);        // Assign a unique ID for table indexing.
            }
        }

        // Initialize the source vertex in the table.
        table[src.getId()].setCost(0);           // Cost to reach source is 0.
        table[src.getId()].setTime(0);           // Time to reach source is 0.
        table[src.getId()].setDistance(0);       // Distance to reach source is 0.
        table[src.getId()].setCurrCost(0);       // Current cost (time) to reach source is 0.
        table[src.getId()].setPath(null);        // Path to source is null (no predecessor).

        // Main loop for Dijkstra's algorithm.
        while (true) {
            // Find the vertex with the smallest unknown cost (time).
            Vertex v = smallestUnknownDistanceVertex(table);
            if (v == null) { // If no such vertex exists, exit the loop.
                break;
            }

            // Mark the vertex `v` as known (processed).
            v.setKnown(true);

            // Retrieve the list of edges connected to `v`.
            LinkedList edges = v.getEdge();
            Node node = edges.getFront();

            // Loop through all edges connected to vertex `v`.
            while (node != null) {
                // Get the edge object.
                Edge e = node.getElement();

                // Get the destination vertex of the edge.
                Vertex w = e.getDestination();

                // If the destination vertex `w` is still unknown:
                if (!w.isKnown()) {
                    // Retrieve the time cost of traveling along this edge.
                    int cvw = e.getTime();

                    // Check if the current path to `w` via `v` is shorter than the previously known path.
                    if (table[v.getId()].getCurrCost() + cvw < table[w.getId()].getCurrCost()) {
                        // Update the current cost (time) to reach `w` via `v`.
                        table[w.getId()].setCurrCost(table[v.getId()].getCurrCost() + cvw);

                        // Update the total cost (monetary) to reach `w`.
                        table[w.getId()].setCost(table[v.getId()].getCost() + e.getCost());

                        // Update the total time to reach `w`.
                        table[w.getId()].setTime(table[v.getId()].getTime() + e.getTime());

                        // Update the total distance to reach `w`.
                        table[w.getId()].setDistance((int) (table[v.getId()].getDistance() + e.getDistance()));

                        // Update the path to `w` by setting its predecessor to `v`.
                        table[w.getId()].setPath(v);
                    }
                }

                // Move to the next edge in the adjacency list.
                node = node.getNext();
            }
        }

        // Reconstruct the path from source to destination using the table.
        LinkedList path = getPath(table, dest);

        // Reset all vertices to unknown for future computations.
        hash.setVerticesUnknown();

        // Return the final table entry for the destination vertex,
        // including the path, cost, time, distance, and current cost.
        return new TableEntry(
                dest,
                table[dest.getId()].getPath(),
                table[dest.getId()].getCost(),
                table[dest.getId()].getTime(),
                table[dest.getId()].getDistance(),
                table[dest.getId()].getCurrCost(),
                path
        );
    }


    /*
     * This method returns the shortest path between two vertices based on the cost to travel
     * between the two vertices.
     * */
    TableEntry costDijkstra(Vertex src, Vertex dest) {
        TableEntry[] table = new TableEntry[numVertices + 1];
        // Read the vertices from the hash table
        for (int i = 0; i < numVertices; i++) {
            if (hash.getHash()[i] != null) {
                table[i] = new TableEntry(hash.getHash()[i], new Vertex(), Integer.MAX_VALUE, Integer.MAX_VALUE,
                        Integer.MAX_VALUE, Integer.MAX_VALUE);
                hash.getHash()[i].setKnown(false);
                hash.getHash()[i].setId(i);
            }
        }

        table[src.getId()].setCost(0);
        table[src.getId()].setTime(0);
        table[src.getId()].setDistance(0);
        table[src.getId()].setCurrCost(0);
        table[src.getId()].setPath(null);

        while (true) {
            Vertex v = smallestUnknownDistanceVertex(table);
            if (v == null) {
                break;
            }

            v.setKnown(true);
            LinkedList edges = v.getEdge();
            Node node = edges.getFront();

            while (node != null) {
                Edge e = node.getElement();
                Vertex w = e.getDestination();
                if (!w.isKnown()) {
                    int cvw = e.getCost();
                    if (table[v.getId()].getCurrCost() + cvw < table[w.getId()].getCurrCost()) {
                        table[w.getId()].setCurrCost(table[v.getId()].getCurrCost() + cvw);
                        table[w.getId()].setCost(table[v.getId()].getCost() + e.getCost());
                        table[w.getId()].setTime(table[v.getId()].getTime() + e.getTime());
                        table[w.getId()].setDistance((int) (table[v.getId()].getDistance() + e.getDistance()));
                        table[w.getId()].setPath(v);
                    }
                }
                node = node.getNext();
            }
        }

        LinkedList path = getPath(table, dest);

        hash.setVerticesUnknown();
        return new TableEntry(dest, table[dest.getId()].getPath(), table[dest.getId()].getCost(),
                table[dest.getId()].getTime(), table[dest.getId()].getDistance(), table[dest.getId()].getCurrCost(), path);
    }

    /*
     * This method returns the shortest path between two vertices based on the distance to travel
     * between the two vertices.
     * */
    TableEntry distanceDijkstra(Vertex src, Vertex dest) {
        // Initialize the table
        TableEntry[] table = new TableEntry[numVertices + 1];
        // Read the vertices from the hash table
        for (int i = 0; i < numVertices; i++) {
            if (hash.getHash()[i] != null) {
                table[i] = new TableEntry(hash.getHash()[i], new Vertex(), Integer.MAX_VALUE, Integer.MAX_VALUE,
                        Integer.MAX_VALUE, Integer.MAX_VALUE);
                hash.getHash()[i].setKnown(false);
                hash.getHash()[i].setId(i);
            }
        }

        table[src.getId()].setCost(0);
        table[src.getId()].setTime(0);
        table[src.getId()].setDistance(0);
        table[src.getId()].setCurrCost(0);
        table[src.getId()].setPath(null);

        // Dijkstra's algorithm
        while (true) {
            Vertex v = smallestUnknownDistanceVertex(table);
            if (v == null) {
                break;
            }

            v.setKnown(true);
            LinkedList edges = v.getEdge();
            Node node = edges.getFront();

            while (node != null) {
                Edge e = node.getElement();
                Vertex w = e.getDestination();
                if (!w.isKnown()) {
                    int cvw = (int) e.getDistance();
                    if (table[v.getId()].getCurrCost() + cvw < table[w.getId()].getCurrCost()) {
                        table[w.getId()].setCurrCost(table[v.getId()].getCurrCost() + cvw);
                        table[w.getId()].setCost(table[v.getId()].getCost() + e.getCost());
                        table[w.getId()].setTime(table[v.getId()].getTime() + e.getTime());
                        table[w.getId()].setDistance((int) (table[v.getId()].getDistance() + e.getDistance()));
                        table[w.getId()].setPath(v);
                    }
                }
                node = node.getNext();
            }
        }
        LinkedList path = getPath(table, dest);


        hash.setVerticesUnknown();
        return new TableEntry(dest, table[dest.getId()].getPath(), table[dest.getId()].getCost(),
                table[dest.getId()].getTime(), table[dest.getId()].getDistance(), table[dest.getId()].getCurrCost(), path);
    }

    /*
    * This method returns the path from the source to the destination vertex.
    * */
    public LinkedList getPath(TableEntry[] table, Vertex dest) {
        LinkedList path = new LinkedList();
        Vertex curr = dest;
        while (curr != null && curr.getCapital() != null) {
            path.addFirst(new Node(new Edge(table[curr.getId()].getPath(), curr, table[curr.getId()].getCost(), table[curr.getId()].getTime(), table[curr.getId()].getDistance())));
            curr = table[curr.getId()].getPath();
        }
        return path;
    }
}