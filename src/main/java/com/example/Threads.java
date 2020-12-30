package com.example;

public class Threads {

    public static void main(String[] args) {

        Thread thread = new Thread(() -> {
            while (true) {

                //Co do zrobienia

                try {
                    Thread.sleep(1000);

                } catch (InterruptedException aE) {
                    aE.printStackTrace();
                }
            }
        });

        thread.start();

    }

}
