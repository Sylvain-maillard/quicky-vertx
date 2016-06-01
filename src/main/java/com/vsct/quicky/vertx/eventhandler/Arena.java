package com.vsct.quicky.vertx.eventhandler;

import com.vsct.quicky.vertx.aggregate.Brute;
import com.vsct.quicky.vertx.commands.Fight;
import com.vsct.quicky.vertx.commands.FindOpponent;
import com.vsct.quicky.vertx.events.*;
import com.vsct.quicky.vertx.eventstore.BruteEvent;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Sylvain on 30/05/2016.
 */
public class Arena extends AbstractVerticle {

    private List<Brute> readyToFightBrute = new LinkedList<>();

    private final AtomicInteger fightCount = new AtomicInteger(0);

    @Override
    public void start() throws Exception {
        vertx.eventBus().consumer(BruteWinFight.class.getName(), this::afterFight);
        vertx.eventBus().consumer(BruteLooseFight.class.getName(), this::selectNextFight);
        vertx.eventBus().consumer(BruteJoined.class.getName(), this::selectNextFight);
        vertx.eventBus().consumer(BruteQuit.class.getName(), this::removeBrute);
        vertx.eventBus().consumer(OpponentFound.class.getName(), this::startFight);
        vertx.eventBus().consumer(BruteShouldRest.class.getName(), this::removeBrute);
    }

    private void afterFight(Message<String> tMessage) {
        if (fightCount.decrementAndGet() == 0) {
          vertx.eventBus().send(ArenaEmptyEvent.class.getName(),"");
        }
        selectNextFight(tMessage);
    }

    private void startFight(Message<String> handler) {
        fightCount.incrementAndGet();
        OpponentFound event = Json.decodeValue(handler.body(), OpponentFound.class);
        vertx.eventBus().send("applyEvents", event.getId(), (AsyncResult<Message<String>> bruteRebuilt) -> {
            if (bruteRebuilt.succeeded()) {
                String bruteAsJson = bruteRebuilt.result().body();
                Brute brute = Json.decodeValue(bruteAsJson, Brute.class);
                brute.processCommand(new Fight(event.getOpponentId()));
            }
        });
    }

    private void removeBrute(Message<String> tMessage) {
        BruteEvent event = Json.decodeValue(tMessage.body(), BruteEvent.class);
        Iterator<Brute> iterator = readyToFightBrute.iterator();
        while (iterator.hasNext()) {
            Brute next = iterator.next();
            if (next.getId().equals(event.getId())) {
                iterator.remove();
            }
        }
    }

    private void selectNextFight(Message<String> handler) {
        BruteEvent event = Json.decodeValue(handler.body(), BruteEvent.class);
        vertx.eventBus().send("applyEvents", event.getId(), (AsyncResult<Message<String>> bruteRebuilt) -> {
            if (bruteRebuilt.succeeded()) {
                String bruteAsJson = bruteRebuilt.result().body();
                Brute brute = Json.decodeValue(bruteAsJson, Brute.class);
                brute.processCommand(new FindOpponent(this));
            }
        });
    }

    public Optional<Brute> lockABruteWithSameXpAs(Brute currentBrute) {
        Optional<Brute> first = readyToFightBrute.stream().filter(brute -> brute.sameXp(currentBrute) && !brute.getId().equals(currentBrute.getId()))
                .findFirst();
        if (first.isPresent()) {
            readyToFightBrute.remove(first.get());
        }
        return first;
    }

    public void registerAvailableBrute(Brute currentBrute) {
        readyToFightBrute.add(currentBrute);
    }
}
