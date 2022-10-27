package org.example;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Checker {
    private ArrayList<Node> primaryNodes;

    public Checker(ArrayList<Node> inputNodes) {
        this.primaryNodes = inputNodes;
    }

    public boolean run() {
        lockNodes();
        boolean isValid = checkNodes();
        unlockNodes();

        return isValid;
    }

    public void lockNodes() {
        for (Node node : primaryNodes) {
            node.mutex.lock();
            lockChildren(node);
        }
    }

    public void lockChildren(Node node) {
        for(Node child : node.getChildren()) {
            child.mutex.lock();
            lockChildren(child);
        }
    }

    public boolean checkNodes() {
        AtomicBoolean isValid = new AtomicBoolean(true);

        for(Node node : primaryNodes)
            if(!check(node))
                isValid.set(false);

        return isValid.get();
    }

    public boolean check(Node node) {
        AtomicBoolean isValid = new AtomicBoolean(true);

        if(node.getChildren().size() != 0) {
            if (node.getParents().size() != 0) {
                int sumValue = 0;
                for(Node parent : node.getParents()) {
                    sumValue += parent.getValue();
                }
                isValid.set(sumValue == node.getValue());
            }
            if(isValid.get()) {
                for(Node child : node.getChildren()) {
                    if (check(child))
                        isValid.set(false);
                }
            }
        }
        return isValid.get();
    }

    public void unlockNodes() {
        primaryNodes.forEach(node -> {
            node.mutex.unlock();
            unlockChildren(node);
        });
    }

    public void unlockChildren(Node node) {
        node.getChildren().forEach(secondary -> {
            secondary.mutex.unlock();
            unlockChildren(secondary);
        });
    }
}