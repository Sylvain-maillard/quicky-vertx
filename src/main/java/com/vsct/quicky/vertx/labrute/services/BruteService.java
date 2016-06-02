package com.vsct.quicky.vertx.labrute.services;

import com.vsct.quicky.vertx.labrute.aggregate.Brute;
import com.vsct.quicky.vertx.labrute.events.OpponentFound;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;

/**
 * Created by Sylvain on 02/06/2016.
 */
public class BruteService extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        vertx.eventBus().consumer(OpponentFound.class.getName(), this::startFight);
    }

    private void startFight(Message<String> handler) {
        OpponentFound event = Json.decodeValue(handler.body(), OpponentFound.class);
        vertx.eventBus().send("applyEvents", event.getId(), (AsyncResult<Message<String>> bruteRebuilt) -> {
            if (bruteRebuilt.succeeded()) {
                String bruteAsJson = bruteRebuilt.result().body();
                Brute brute = Json.decodeValue(bruteAsJson, Brute.class);
                brute.fight(event.getOpponentId());
            }
        });
    }
}
