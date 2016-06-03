package com.vsct.quicky.vertx.part2;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class D_VerticleExemple {

    private static final Logger LOGGER = LoggerFactory.getLogger(D_VerticleExemple.class);

    public static void main(String[] args) {
        // initialisation
        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(new Service2());
        vertx.deployVerticle(new Service1());

        vertx.runOnContext(h -> {
            LOGGER.debug("sending message at startup");
            vertx.eventBus().send("addr", "helloFromService1");
        });

        vertx.setTimer(1000, h -> vertx.close());
    }

    static class Service1 extends AbstractVerticle {
        @Override
        public void start() throws Exception {
            vertx.eventBus().consumer("service1/addr", handler -> {
                LOGGER.debug("Service 1 received message ! {}", handler.body());
            });
        }
    }

    static class Service2 extends AbstractVerticle {
        @Override
        public void start() throws Exception {
            vertx.eventBus().consumer("addr", handler -> {
                LOGGER.debug("Service 2 received message ! {}, tell service 1", handler.body());
                vertx.eventBus().send("service1/addr", "got message");
            });
        }
    }
}
