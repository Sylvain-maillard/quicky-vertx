package com.vsct.quicky.vertx.projections;

import com.vsct.quicky.vertx.events.BruteJoined;
import com.vsct.quicky.vertx.events.BruteLooseFight;
import com.vsct.quicky.vertx.events.BruteQuit;
import com.vsct.quicky.vertx.events.BruteWinFight;
import com.vsct.quicky.vertx.eventstore.BruteEvent;
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
        vertx.eventBus().consumer(BruteQuit.class.getName(), this::removeBrute);
        vertx.eventBus().consumer(BruteJoined.class.getName(), this::initBrute);
        vertx.eventBus().consumer(BruteWinFight.class.getName(), this::updateHallOfFameForWinner);
        vertx.eventBus().consumer(BruteLooseFight.class.getName(), this::updateHallOfFameForLooser);
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

    private void removeBrute(Message<String> tMessage) {
        BruteEvent event = Json.decodeValue(tMessage.body(), BruteEvent.class);
        hallOfFame.remove(event.getId());
    }

    private void updateHallOfFameForLooser(Message<String> tMessage) {
        BruteLooseFight event = Json.decodeValue(tMessage.body(), BruteLooseFight.class);
        hallOfFame.compute(event.getId(), (s, row) -> row.notifyLoose());
    }

    private void updateHallOfFameForWinner(Message<String> tMessage) {
        BruteWinFight event = Json.decodeValue(tMessage.body(), BruteWinFight.class);
        hallOfFame.compute(event.getId(), (s, row) -> row.notifyWin());
    }

    private void initBrute(Message<String> tMessage) {
        BruteEvent event = Json.decodeValue(tMessage.body(), BruteEvent.class);
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

        BruteRow notifyLoose() {
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
