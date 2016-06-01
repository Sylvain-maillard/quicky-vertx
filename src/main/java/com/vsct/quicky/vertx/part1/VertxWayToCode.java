package com.vsct.quicky.vertx.part1;

import io.vertx.core.Handler;

@SuppressWarnings("ALL")
public class VertxWayToCode {

    public static void main(String[] args) {
        new VertxWayToCode().runClassic();
        new VertxWayToCode().runVertx_lambda();
        new VertxWayToCode().runVertx_method_reference();
    }

    void getHelloWorld_vertx_way(Handler<String> resultHandler) {
        resultHandler.handle("Hello vertx world");
    }

    void runVertx_lambda() {
        getHelloWorld_vertx_way(result -> {
            // do something with result
        });
    }

    void runVertx_method_reference() {
        getHelloWorld_vertx_way(this::doWithResult);
    }

    void doWithResult(String result) {
        // do something with result
    }

    String getHelloWorld_classic_way() {
        return "Hello classic world.";
    }

    void runClassic() {
        String result = getHelloWorld_classic_way();
        // do something with result:
    }

}
