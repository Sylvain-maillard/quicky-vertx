package com.vsct.quicky.vertx.projections;

import com.vsct.quicky.vertx.events.BruteJoined;
import com.vsct.quicky.vertx.events.BruteLooseFight;
import com.vsct.quicky.vertx.events.BruteQuit;
import com.vsct.quicky.vertx.events.BruteWinFight;
import com.vsct.quicky.vertx.eventstore.BruteEvent;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sylvain on 29/05/2016.
 */
public class HallOfFame extends AbstractVerticle {

    private static class BruteRow implements Comparable {
        final int xp;
        final int fightCount;

        private BruteRow(int xp, int fightCount) {
            this.xp = xp;
            this.fightCount = fightCount;
        }
        BruteRow notifyWin() {
            return new BruteRow(xp+2, fightCount+1);
        }
        BruteRow notifyLoose() {
            return new BruteRow(xp+1, fightCount+1);
        }

        @Override
        public String toString() {
            return "| " + xp + "|" + fightCount + "|";
        }

        @Override
        public int compareTo(Object o) {
            return ((BruteRow)o).xp - xp;
        }
    }

    private Map<String, BruteRow> hallOfFame = new HashMap<>();

    @Override
    public void start() throws Exception {
        vertx.eventBus().consumer(BruteQuit.class.getName(), this::removeBrute);
        vertx.eventBus().consumer(BruteJoined.class.getName(), this::initBrute);
        vertx.eventBus().consumer(BruteWinFight.class.getName(), this::updateHallOfFameForWinner);
        vertx.eventBus().consumer(BruteLooseFight.class.getName(), this::updateHallOfFameForLooser);
    }

    private void removeBrute(Message<String> tMessage) {
        BruteEvent event = Json.decodeValue(tMessage.body(), BruteEvent.class);
        hallOfFame.remove(event.getId());
    }

    private void updateHallOfFameForLooser(Message<String> tMessage) {
        BruteLooseFight event = Json.decodeValue(tMessage.body(), BruteLooseFight.class);
        hallOfFame.compute(event.getId(), (s, row) ->  row.notifyLoose());
    }

    private void updateHallOfFameForWinner(Message<String> tMessage) {
        BruteWinFight event = Json.decodeValue(tMessage.body(), BruteWinFight.class);
        hallOfFame.compute(event.getId(), (s, row) -> row.notifyWin());
    }

    private void initBrute(Message<String> tMessage) {
        BruteEvent event = Json.decodeValue(tMessage.body(), BruteEvent.class);
        hallOfFame.put(event.getId(), new BruteRow(0,0));
    }

    public void displayHallOfFame() {
        System.out.println("------- Hall of Fame ----------");
        hallOfFame.entrySet().stream().sorted((o1, o2) -> o1.getValue().compareTo(o2.getValue())).forEach(stringIntegerEntry -> {
            System.out.println("| " + stringIntegerEntry.getKey() + stringIntegerEntry.getValue()
            );
        });
    }
}
