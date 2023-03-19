package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {
    public static Integer rowFirst, columnFirst, columnSecond;
    public static Integer functionType;
    public static Integer computationType;
    public static Integer threadCount;
    public static Matrix matrix1;
    public static Matrix matrix2;

    public static void getParams() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Row Count for first matrix: ");
        rowFirst = scanner.nextInt();
        System.out.print("Column Count for first matrix and Row Count for second matrix: ");
        columnFirst = scanner.nextInt();
        System.out.print("Column Count for second matrix: ");
        columnSecond = scanner.nextInt();
        System.out.print("Execution Method (0-threads/1-threadpool): ");
        functionType = scanner.nextInt();
        System.out.print("Scan Type (0-row/1-column/2-kth): ");
        computationType = scanner.nextInt();
        System.out.print("Number of threads: ");
        threadCount = scanner.nextInt();
        matrix1 = new Matrix(rowFirst, columnFirst);
        matrix2 = new Matrix(columnFirst, columnSecond);
    }

    public static Matrix threadVariant() throws InterruptedException {
        Integer[][] result = new Integer[rowFirst][columnSecond];
        List<Thread> threads = new ArrayList<>();
        int iterations = rowFirst * columnSecond / threadCount;
        for (int i = 0; i < threadCount; ++i) {
            int a = i * iterations;
            int b = Math.min((i+1) * iterations, rowFirst * columnSecond);
            switch (functionType) {
                case 0 -> threads.add(new Thread(new RowThread(result, a, b)));
                case 1 -> threads.add(new Thread(new ColumnThread(result, a, b)));
                default -> threads.add(new Thread(new KthThread(result, i, threadCount)));
            }
        }
        for (Thread thread : threads)
            thread.start();
        for (Thread thread : threads)
            thread.join();
        return new Matrix(result);
    }

    public static Matrix threadpoolVariant() throws InterruptedException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadCount);
        Integer[][] result = new Integer[rowFirst][columnSecond];
        List<Runnable> threads = new ArrayList<>();
        int iterations = rowFirst * columnSecond / threadCount;
        for (int i = 0; i < threadCount; ++i) {
            int a = i * iterations;
            int b = Math.min((i+1) * iterations, rowFirst * columnSecond);
            switch (functionType) {
                case 0 -> threads.add(new Thread(new RowThread(result, a, b)));
                case 1 -> threads.add(new Thread(new ColumnThread(result, a, b)));
                default -> threads.add(new Thread(new KthThread(result, i, threadCount)));
            }
        }
        for (Runnable thread : threads)
            executor.execute(thread);
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.DAYS);
        return new Matrix(result);
    }

    public static void main(String[] args) throws InterruptedException {
        getParams();
        Matrix trueProduct = Matrix.getProductSequentially(matrix1, matrix2);
        System.out.println("Product computed sequentially ");
        trueProduct.print();
        Matrix computedProduct = null;
        Long initialValue = System.currentTimeMillis();
        if (functionType == 0)
            computedProduct = threadVariant();
        else computedProduct = threadpoolVariant();
        if (trueProduct.equals(computedProduct))
            System.out.println("Computed correct product");
        else System.out.println("Computation failed");
        Long finalValue = System.currentTimeMillis() - initialValue;
        System.out.println("Task computed in " + finalValue + " miliseconds");
    }
}
