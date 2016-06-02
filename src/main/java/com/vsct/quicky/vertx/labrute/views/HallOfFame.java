package com.vsct.quicky.vertx.labrute.views;

import com.vsct.quicky.vertx.labrute.events.BruteJoined;
import com.vsct.quicky.vertx.labrute.events.BruteLoseFight;
import com.vsct.quicky.vertx.labrute.events.BruteQuit;
import com.vsct.quicky.vertx.labrute.events.BruteWinFight;
import com.vsct.quicky.vertx.labrute.fwk.Event;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.center;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Sylvain on 29/05/2016.
 */
public class HallOfFame extends AbstractVerticle {

    private static final Logger LOGGER = getLogger(HallOfFame.class);

    private Map<String, BruteRow> hallOfFame = new HashMap<>();

    @Override
    public void start() throws Exception {
        vertx.eventBus().consumer(BruteQuit.class.getName(), this::bruteQuit);
        vertx.eventBus().consumer(BruteJoined.class.getName(), this::bruteJoined);
        vertx.eventBus().consumer(BruteWinFight.class.getName(), this::updateHallOfFameForWinner);
        vertx.eventBus().consumer(BruteLoseFight.class.getName(), this::updateHallOfFameForLoser);
        vertx.createHttpServer()
                .requestHandler(h ->  {
                    LOGGER.info("got query {}", h.uri());
                    if (h.uri().equals("/hallOfFame")) {

                        StringBuilder sb = new StringBuilder();
                        hallOfFame.entrySet()
                                .stream()
                                .sorted((o1, o2) -> o1.getValue().compareTo(o2.getValue()))
                                .forEach(entry -> sb.append(entry.getValue().toHtml()));

                        h.response().end("<html><body><h1>Hall of Fame</h1>" +
                                "<table>" +
                                "<tr><th width=50>Nom</th><th>XP</th><th>Fight count</th></tr>" +
                                sb.toString() +
                                "</table>" +
                                "</body></html>");
                    }
                })
                .listen(9080);
    }

    private void bruteQuit(Message<String> tMessage) {
        Event event = Json.decodeValue(tMessage.body(), Event.class);
        hallOfFame.remove(event.getId());
    }

    private void updateHallOfFameForLoser(Message<String> tMessage) {
        BruteLoseFight event = Json.decodeValue(tMessage.body(), BruteLoseFight.class);
        hallOfFame.compute(event.getId(), (s, row) -> row.notifyLose());
    }

    private void updateHallOfFameForWinner(Message<String> tMessage) {
        BruteWinFight event = Json.decodeValue(tMessage.body(), BruteWinFight.class);
        hallOfFame.compute(event.getId(), (s, row) -> row.notifyWin());
    }

    private void bruteJoined(Message<String> tMessage) {
        Event event = Json.decodeValue(tMessage.body(), Event.class);
        hallOfFame.put(event.getId(), new BruteRow(event.getId(), 0, 0));
    }

    public void displayHallOfFame() {
        StringBuilder sb = new StringBuilder("\n------- Hall of Fame ----------\n");
        hallOfFame.entrySet()
                .stream()
                .sorted((o1, o2) -> o1.getValue().compareTo(o2.getValue()))
                .forEach(entry -> sb.append("| ").append(center(entry.getKey(), 10)).append(entry.getValue()).append("\n"));
        LOGGER.info(sb.toString());
    }

    private static class BruteRow implements Comparable {
        final String id;
        final int xp;
        final int fightCount;

        private BruteRow(String id, int xp, int fightCount) {
            this.id = id;
            this.xp = xp;
            this.fightCount = fightCount;
        }

        BruteRow notifyWin() {
            return new BruteRow(id, xp + 2, fightCount + 1);
        }

        BruteRow notifyLose() {
            return new BruteRow(id, xp + 1, fightCount + 1);
        }

        @Override
        public String toString() {
            return "| " + center(""+xp,5) + "|" + center(""+fightCount,5) + "|";
        }
        public String toHtml() {
            return "<tr><td>" + id+ "</td><td>"  + xp + "</td><td>" + fightCount + "</td></tr>";
        }

        @Override
        public int compareTo(Object o) {
            return ((BruteRow) o).xp - xp;
        }
    }
}
