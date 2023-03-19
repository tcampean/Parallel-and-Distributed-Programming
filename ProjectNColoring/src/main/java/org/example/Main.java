package org.example;


public class Main {
    public static void main(String[] args) throws InterruptedException {
        long start, end;
        Graph graph = new Graph(6);
        graph.add(0,2);
        graph.add(2,0);
        graph.add(2,3);
        graph.add(3,2);
        graph.add(3,4);
        graph.add(4,3);
        graph.add(4, 1);
        graph.add(1, 4);
        graph.add(4, 5);
        graph.add(5, 4);
        graph.add(4, 2);
        graph.add(2, 4);
        graph.add(1, 0);
        graph.add(0, 1);
        graph.add(5 ,3);
        graph.add(3 ,5);

        start = System.currentTimeMillis();
        NColoring.colorVertices(graph, 3);
        end = System.currentTimeMillis();
        System.out.println(graph);
        System.out.println("Time: " + (end - start) + " ms");
        if(graph.checkColor())
            System.out.println("Coloring is correct");
        else
            System.out.println("Coloring is incorrect");

    }
}
