package com.vsct.quicky.vertx.labrute.services;

import com.vsct.quicky.vertx.labrute.aggregate.Arena;
import com.vsct.quicky.vertx.labrute.aggregate.Brute;
import com.vsct.quicky.vertx.labrute.events.*;
import com.vsct.quicky.vertx.labrute.fwk.Event;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;

import java.util.Iterator;

/**
 * Created by Sylvain on 30/05/2016.
 */
public class ArenaService extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        vertx.eventBus().consumer(BruteWinFight.class.getName(), this::selectNextFight);
        vertx.eventBus().consumer(BruteLoseFight.class.getName(), this::selectNextFight);
        vertx.eventBus().consumer(BruteJoined.class.getName(), this::selectNextFight);
        vertx.eventBus().consumer(BruteQuit.class.getName(), this::removeBrute);
        vertx.eventBus().consumer(BruteShouldRest.class.getName(), this::removeBrute);
    }

    private void removeBrute(Message<String> tMessage) {
        Event event = Json.decodeValue(tMessage.body(), Event.class);
        Iterator<Brute> iterator = Arena.readyToFightBrute.iterator();
        while (iterator.hasNext()) {
            Brute next = iterator.next();
            if (next.getId().equals(event.getId())) {
                iterator.remove();
            }
        }
    }

    private void selectNextFight(Message<String> handler) {
        Event event = Json.decodeValue(handler.body(), Event.class);
        vertx.eventBus().send("applyEvents", event.getId(), (AsyncResult<Message<String>> bruteRebuilt) -> {
            if (bruteRebuilt.succeeded()) {
                String bruteAsJson = bruteRebuilt.result().body();
                Brute brute = Json.decodeValue(bruteAsJson, Brute.class);
                // create arena aggregate
                Arena arena = new Arena();
                // should get arena from event store, etc.
                arena.findOpponent(brute);
            }
        });
    }
}
