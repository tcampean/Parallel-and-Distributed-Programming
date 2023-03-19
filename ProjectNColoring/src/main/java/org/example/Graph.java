package org.example;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private List<Node> nodes;
    private int nrNodes;

    public Graph(int nrNodes) {
        this.nrNodes = nrNodes;
        this.nodes = new ArrayList<>(nrNodes);
        for (int i = 0; i < nrNodes; i++) {
            this.nodes.add(new Node());
        }
    }

    public Graph() {
    }

    public void add(int x, int y) {
        if (!this.nodes.get(x).getNeighbours().contains(y) && (x != y)) {
            this.nodes.get(x).getNeighbours().add(y);
        }
    }

    public int getNrNodes() {
        return nrNodes;
    }

    public Node getNode(int i) {
        return nodes.get(i);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < nodes.size(); i++) {
            s.append("node ").append(i).append(", color ").append(nodes.get(i).getColor()).append(": ").append(nodes.get(i).getNeighbours()).append("\n");
        }
        return s.toString();
    }

    public boolean checkColor() {
        for(int i=0; i<nrNodes; i++) {
            Node node = this.nodes.get(i);
            int color = node.getColor();
            for(int j=0; j<node.getNeighbours().size(); j++) {
                Node adj = this.nodes.get(node.getNeighbours().get(j));
                if(adj.getColor() == color)
                    return false;
            }
        }
        return true;
    }
}
