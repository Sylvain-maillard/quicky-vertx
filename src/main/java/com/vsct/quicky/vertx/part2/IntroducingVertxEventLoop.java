package com.vsct.quicky.vertx.part2;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;

public class IntroducingVertxEventLoop {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.runOnContext(action -> {
            System.out.println("Printing on event loop: " + Thread.currentThread().getName());
        });
        System.out.println("Printing on main thread: " + Thread.currentThread().getName());


        vertx.eventBus().consumer("addr", h ->  {
            System.out.println("consuming message");
        });

        vertx.setTimer(200, hhh -> {
                    vertx.eventBus().consumer("addr", h2 -> {
                        System.out.println("consuming message2");
                    });
                });

        vertx.eventBus().publish("addr","msg", new DeliveryOptions().setSendTimeout(100));

        vertx.close();
    }
}
