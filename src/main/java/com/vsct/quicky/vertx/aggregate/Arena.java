package com.vsct.quicky.vertx.aggregate;

import com.vsct.quicky.vertx.commands.Fight;
import com.vsct.quicky.vertx.commands.SearchForMatch;
import com.vsct.quicky.vertx.events.BruteJoined;
import com.vsct.quicky.vertx.events.BruteLooseFight;
import com.vsct.quicky.vertx.events.BruteQuit;
import com.vsct.quicky.vertx.events.BruteWinFight;
import com.vsct.quicky.vertx.eventstore.BruteEvent;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Sylvain on 30/05/2016.
 */
public class Arena extends AbstractVerticle {

    private List<Brute> readyToFightBrute = new LinkedList<>();

    @Override
    public void start() throws Exception {
        vertx.eventBus().consumer(BruteWinFight.class.getName(), this::selectNextFight);
        vertx.eventBus().consumer(BruteLooseFight.class.getName(), this::selectNextFight);
        vertx.eventBus().consumer(BruteJoined.class.getName(), this::selectNextFight);
        vertx.eventBus().consumer(BruteQuit.class.getName(), this::bruteQuit);
    }

    private void bruteQuit(Message<String> tMessage) {
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
        Brute currentBrute = Brute.fromStore(event.getId());
        //
        // currentBrute.processCommand(new SearchForMatch());
        // select les brutes qui n'ont pas atteint le max de combat.
        if (currentBrute.getFightCount() >= 3) {
            System.out.println("brute " + currentBrute + " should rest for this day ! ");
            return;
        }
        System.out.println("check fight for brute " + currentBrute);
        // select les brutes qui ont le même niveau d'xp
        Optional<Brute> opponent = readyToFightBrute.stream().filter(brute -> brute.sameXp(currentBrute))
                .findFirst();

        if (opponent.isPresent()) {
            readyToFightBrute.remove(opponent.get());
            currentBrute.processCommand(new Fight(opponent.get().getId()));
        } else {
            readyToFightBrute.add(currentBrute);
        }
    }
}
