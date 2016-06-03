package com.vsct.quicky.vertx.part2;

import io.vertx.core.Vertx;

public class A_EventLoopExemple {
    public static void main(String[] args) {
        // initialisation
        Vertx vertx = Vertx.vertx();

        // runnable dans l'event loop
        vertx.runOnContext(action -> {
            System.out.println("Printing on event loop: " + Thread.currentThread().getName());
        });
        System.out.println("Printing on main thread: " + Thread.currentThread().getName());

        // finalisation
        vertx.setTimer(1000, h -> vertx.close());
    }
}
