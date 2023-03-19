package org.example;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Controller {
    private final ArrayList<Integer> feeds = new ArrayList<>();
    private final Lock lock = new ReentrantLock();
    private final Condition cond = lock.newCondition();

    public Controller() {}

    public void feed(int val) throws InterruptedException {
        lock.lock();
        try {
            feeds.add(val);
            cond.signal();
            System.out.println("Producer put " + val + " into list\n");
        } finally {
            lock.unlock();
        }
    }
    public int consume() throws InterruptedException {
        lock.lock();
        try {
            while (feeds.size() == 0) {
                System.out.println("Consumer found nothing in the list\n");
                cond.await();
            }
            Integer val = feeds.remove(0);
            if (val != null) {
                System.out.printf("Consumer got " + val + " out of the list\n");
                cond.signal();
            }
            return val;
        } finally {
            lock.unlock();
        }
    }
}
