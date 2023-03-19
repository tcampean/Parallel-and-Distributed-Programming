package org.example;

import java.util.List;

public class RunRecoloring implements Runnable {
    private Graph graph;
    private int start;
    private int end;
    private List<Integer> r;

    public RunRecoloring(Graph graph, List<Integer> r, int start, int end) {
        this.graph = graph;
        this.start = start;
        this.end = end;
        this.r = r;
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            Node node = graph.getNode(i);
            List<Integer> adjList = node.getNeighbours();

            for (int adjNode : adjList) {
                if (graph.getNode(adjNode).getColor() == node.getColor())
                    r.add(Math.max(adjNode, i));
            }
        }
    }
}

