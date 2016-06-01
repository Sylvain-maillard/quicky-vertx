package com.vsct.quicky.vertx.part2;

import io.vertx.core.Vertx;

public class IntroducingVertxEventLoop {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.runOnContext(action -> {
            System.out.println("Printing on event loop: " + Thread.currentThread().getName());
        });
        System.out.println("Printing on main thread: " + Thread.currentThread().getName());
        vertx.close();
    }
}
