package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NColoring {
    public static void firstIteration(Graph graph, int threadCount) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        int size = graph.getNrNodes() / threadCount;
        RunColoring[] sums = new RunColoring[threadCount];
        for (int i = 0; i < threadCount; i++) {
            int end;
            if(i == threadCount - 1)
                end = graph.getNrNodes();
            else
                end = (i + 1) * size;
            sums[i] = new RunColoring(graph, i * size, end);
            executorService.execute(sums[i]);
        }
        executorService.shutdown();
        executorService.awaitTermination(60, TimeUnit.SECONDS);

    }

    public static void coloring(Graph graph, int threadCount, List<Integer> r) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        int size = graph.getNrNodes() / threadCount;
        RunRecoloring[] sums = new RunRecoloring[threadCount];
        for (int i = 0; i < threadCount; i++) {
            int end;
            if(i == threadCount - 1)
                end = graph.getNrNodes();
            else
                end = (i + 1) * size;
            sums[i] = new RunRecoloring(graph, r, i * size, end);
            executorService.execute(sums[i]);
        }
        executorService.shutdown();
        executorService.awaitTermination(60, TimeUnit.SECONDS);
    }

    public static void colorVertices(Graph graph, int threadCount) throws InterruptedException {
        List<Integer> vertices = IntStream.rangeClosed(0, graph.getNrNodes()).boxed().collect(Collectors.toList());
        while (!vertices.isEmpty()) {
            List<Integer> r = new ArrayList<>();
            firstIteration(graph, threadCount);
            coloring(graph, threadCount, r);
            vertices = r;
        }
    }
}
