package com.vsct.quicky.vertx.part2;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class B_HttpClientExemple {

    private static final Logger LOGGER = LoggerFactory.getLogger(B_HttpClientExemple.class);

    public static void main(String[] args) {
        // initialisation
        Vertx vertx = Vertx.vertx();

        vertx.createHttpClient().get(80, "www.google.com", "/").handler(h -> {
            LOGGER.debug("got response: " + h.statusMessage());
// ceci bloque l'event loop et déclenche des stacktraces...
//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
            h.bodyHandler(bodyHandler -> {
                LOGGER.debug(bodyHandler.toString());
            });
        }).end();

        vertx.createHttpClient().get(80, "www.google.com", "/").handler(h -> {
            LOGGER.debug("got response: " + h.statusMessage());
            h.bodyHandler(bodyHandler -> {
                LOGGER.debug(bodyHandler.toString());
            });
        }).end();
    }
}
