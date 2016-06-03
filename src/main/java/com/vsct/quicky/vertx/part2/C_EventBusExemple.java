package com.vsct.quicky.vertx.part2;

import io.vertx.core.Vertx;

public class C_EventBusExemple {

    public static void main(String[] args) {
        // initialisation
        Vertx vertx = Vertx.vertx();

        // eventbus : publish/subscriber
        vertx.eventBus().consumer("addr", h -> {
            System.out.println("consuming message");
        });
        vertx.eventBus().consumer("addr", h2 -> {
            System.out.println("consuming message2");
        });
        vertx.eventBus().publish("addr", "msg");

        // eventbus: request, reply
        vertx.eventBus().consumer("requestReply", handler -> {
            System.out.println("FromClient: " + handler.body());
            handler.reply("pong", h2 -> {
                System.out.println("FromClient2: " + h2.result().body());
                h2.result().reply("pong");
            });
        });
        vertx.eventBus().send("requestReply", "ping", replyHandler -> {
            System.out.println("FromServer: " + replyHandler.result().body());
            replyHandler.result().reply("ping", replyHandler2 -> {
                System.out.println("FromServer2: " +replyHandler2.result().body());
                replyHandler2.result().reply("ping");
            });
        });

        vertx.setTimer(1000, h -> vertx.close());
    }
}
