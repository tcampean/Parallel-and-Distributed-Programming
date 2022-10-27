package org.example;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Node {
    private ArrayList<Node> parents = new ArrayList<>();
    private ArrayList<Node> children = new ArrayList<>();
    private int value = 0;
    public ReentrantLock mutex = new ReentrantLock();

    public Node() {}
    public Node(int value) {
        this.value = value;
    }

    public ArrayList<Node> getParents() {
        return parents;
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public int getValue() {
        return value;
    }

    public void addChild(Node child) {
        this.children.add(child);
        child.addParent(this);
        child.addValue(value);
    }

    public void addParent(Node input) {
        this.parents.add(input);
    }

    public void addValue(int value) {
        mutex.lock();
        this.value += value;
        for(Node child: this.getChildren())
            child.addValue(value);
        mutex.unlock();
    }

    public void updateParent(int value) {
        mutex.lock();
        Integer difference = value - this.value;
        this.value = value;
        for(Node child: this.getChildren())
            child.addValue(difference);
        mutex.unlock();
    }
}