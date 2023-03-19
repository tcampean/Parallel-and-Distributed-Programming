package org.example;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<Integer> vector1 = new ArrayList<>();
        ArrayList<Integer> vector2 = new ArrayList<>();
        Controller c = new Controller();

        vector1.add(1);
        vector1.add(2);
        vector1.add(3);
        vector1.add(4);
        vector1.add(5);

        vector2.add(1);
        vector2.add(2);
        vector2.add(3);
        vector2.add(4);
        vector2.add(5);

        Thread producerThread = new Thread(() -> {
            for (int i = 0; i < vector1.size(); ++i) {
                try {
                    Thread.sleep(3000);
                    Integer val = vector1.get(i) * vector2.get(i);
                    System.out.printf("Producer is putting " + val + "\n");
                    c.feed(val);
                } catch (InterruptedException e) {
                }
            }
        });
        producerThread.start();

        Thread consumerThread = new Thread(() -> {
            int sum = 0;
            for (int i = 0; i < vector1.size(); ++i) {
                try {
                    Thread.sleep(1000);
                    sum += c.consume();
                    System.out.printf("Consumer current sum is " + sum + "\n");
                } catch (InterruptedException e) {
                }
            }
            System.out.printf("Final sum " + sum + "\n");
        });
        consumerThread.start();

    }
}
