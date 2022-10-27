package org.example;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static ArrayList<Node> inputNodes = new ArrayList<>();
    public static Integer inputModifyCount = 0;
    public static Integer checkCount = 0;

    public static void main(String[] args) {
        createNodes();
        modifyInputNodes();
        runChecker();
    }

    private static void modifyInputNodes(){
        for(int i = 0; i < 3; ++i){
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    int index = ThreadLocalRandom.current().nextInt(0,Main.inputNodes.size());
                    Node node = Main.inputNodes.get(index);
                    int value = ThreadLocalRandom.current().nextInt(0, 10);
                    System.out.println("Changing the value of node " + index + " to value " + value);
                    node.updateParent(value);
                    if(inputModifyCount > 11) {
                        timer.cancel();
                    }
                    inputModifyCount++;
                }
            };
            timer.schedule(timerTask,0,3000);
        }
    }

    private static void runChecker(){
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Checker checker = new Checker(Main.inputNodes);
                System.out.println("Running checker...");
                boolean result = checker.run();
                System.out.println("Checker returned result " + result + ".");
                if(checkCount > 3) {
                    timer.cancel();
                }
                checkCount++;
            }
        };
        timer.schedule(timerTask,200,3000);
    }

    private static void createNodes(){
        Node input1 = new Node(1);
        Node input2 = new Node(1);
        Node input3 = new Node(1);
        Node input4 = new Node(1);
        Node input5 = new Node(1);

        Node secondSecondary1 = new Node();
        Node secondSecondary2 = new Node();

        Node thirdSecondary = new Node();

        inputNodes.add(input1);
        inputNodes.add(input2);
        inputNodes.add(input3);
        inputNodes.add(input4);

        input1.addChild(secondSecondary1);
        input2.addChild(secondSecondary1);
        input3.addChild(secondSecondary1);
        input4.addChild(secondSecondary2);
        input5.addChild(secondSecondary2);
        input5.addChild(thirdSecondary);
        secondSecondary1.addChild(thirdSecondary);
        secondSecondary2.addChild(thirdSecondary);

    }
}