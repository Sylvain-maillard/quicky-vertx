package com.vsct.quicky.vertx.eventstore;

import com.google.common.collect.ImmutableList;
import com.vsct.quicky.vertx.aggregate.Brute;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Sylvain on 29/05/2016.
 */
public class BruteEventStore extends AbstractVerticle {

    private static final Logger LOGGER = getLogger(BruteEventStore.class);

    private ConcurrentMap<String, List<BruteEvent>> store = new ConcurrentHashMap<>();

    @Override
    public void start() throws Exception {

        vertx.eventBus().consumer("events", (Message<String> handler) -> {
            BruteEvent event = Json.decodeValue(handler.body(), BruteEvent.class);
            String id = event.getId();
            // get event for brute:
            List<BruteEvent> events = store.getOrDefault(id, new ArrayList<>());
            events.add(event);
            store.put(id, events);
            // publish event
            vertx.eventBus().publish(event.getClass().getName(), handler.body());
            LOGGER.info("Store event: " + handler.body());
        });

        vertx.eventBus().consumer("applyEvents", this::applyEvents);
    }

    private void applyEvents(Message<String> tMessage) {
        Brute brute = new Brute();
        brute.applyEvents(getPastEvents(tMessage.body()));
        tMessage.reply(Json.encode(brute));
    }

    private List<BruteEvent> getPastEvents(String bruteId) {
        return store.getOrDefault(bruteId, ImmutableList.of());
    }

    public void displayAllBrutes() {
        StringBuilder sb = new StringBuilder().append("-----------brutes------------");
        store.entrySet().forEach(bruteEvents -> {
            sb.append("id: " + bruteEvents.getKey());
            sb.append("--> events:");
            bruteEvents.getValue().forEach(sb::append);
            sb.append("--> brute rebuilded from store:");
            Brute brute = new Brute();
            brute.applyEvents(bruteEvents.getValue());
            sb.append(brute);
        });
        LOGGER.info(sb.toString());
    }

    public void displayAllEventByTime() {
        StringBuilder sb = new StringBuilder().append("-----------brutes by datetime------------");
        store.entrySet().forEach(bruteEvents -> {
            sb.append("id: " + bruteEvents.getKey());
            sb.append("--> events:");
            bruteEvents.getValue().stream().sorted((o1, o2) -> o1.getTime().compareTo(o2.getTime())).forEach(sb::append);
            sb.append("--> brute rebuilded from store:");
            Brute brute = new Brute();
            brute.applyEvents(bruteEvents.getValue());
            sb.append(brute);
        });
        LOGGER.info(sb.toString());
    }

}
