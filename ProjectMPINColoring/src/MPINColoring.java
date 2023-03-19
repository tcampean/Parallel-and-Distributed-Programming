import mpi.MPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MPINColoring {
    public static void root() {
        int size = MPI.COMM_WORLD.Size();

        Graph graph = new Graph(size);
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
        long start = System.currentTimeMillis();

        for (int k = 1; k < size; k++) {
            Graph[] send = new Graph[1];
            send[0] = graph;
            MPI.COMM_WORLD.Send(send, 0, 1, MPI.OBJECT, k, 0);
        }
        compute(0, graph);
        for (int i = 1; i < size; i++) {
            int[] col = new int[1];
            MPI.COMM_WORLD.Recv(col, 0, 1, MPI.INT, i, 2);
            graph.getNode(i).setColor(col[0]);
        }

        long end = System.currentTimeMillis();
        System.out.println(graph);
        if(graph.checkColor())
            System.out.println("Coloring is correct");
        else
            System.out.println("Coloring is incorrect");
        System.out.println("Time : " + (end - start) + " ms");
    }

    public static void compute(int node, Graph graph) {
        List<Integer> adjacent = graph.getNode(node).getNeighbours();
        List<Integer> availableColors = IntStream.rangeClosed(0, adjacent.size())
                .boxed().collect(Collectors.toList());

        Random random = new Random();
        int[] col = new int[1];
        int color = -1;
        boolean chosen = false;
        int idx = 0;
        while (idx < graph.getNrNodes()) {
            idx++;
            if (!chosen) {
                color = availableColors.get(random.nextInt(availableColors.size()));
            }
            List<Integer> neighbourColours = new ArrayList<>();
            col[0] = color;
            for (int neighbour : adjacent) {
                MPI.COMM_WORLD.Isend(col, 0, 1, MPI.INT, neighbour, 0);
            }
            for (int neighbour : adjacent) {
                MPI.COMM_WORLD.Recv(col, 0, 1, MPI.INT, neighbour, 0);
                neighbourColours.add(col[0]);
            }

            if (!neighbourColours.contains(color)) {
                col[0] = color;
                chosen = true;
            } else {
                col[0] = -1;
            }

            for (int neighbour : adjacent) {
                MPI.COMM_WORLD.Isend(col, 0, 1, MPI.INT, neighbour, 1);
            }

            for (int neighbour : adjacent) {
                MPI.COMM_WORLD.Recv(col, 0, 1, MPI.INT, neighbour, 1);
                int neighbourColor = col[0];
                if (neighbourColor != -1) {
                    availableColors.remove(Integer.valueOf(neighbourColor));
                }
            }
        }
        if (node == 0) {
            graph.getNode(0).setColor(color);
        } else {
            col[0] = color;
            MPI.COMM_WORLD.Isend(col, 0, 1, MPI.INT, 0, 2);
        }
    }

    public static void child() {
        Graph graph;
        Graph[] recv = new Graph[1];
        MPI.COMM_WORLD.Recv(recv, 0, 1, MPI.OBJECT, 0, 0);
        graph = recv[0];

        int node = MPI.COMM_WORLD.Rank();
        compute(node, graph);

    }
}
