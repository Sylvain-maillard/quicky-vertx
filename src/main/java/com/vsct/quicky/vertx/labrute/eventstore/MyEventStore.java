package com.vsct.quicky.vertx.labrute.eventstore;

import com.google.common.collect.ImmutableList;
import com.vsct.quicky.vertx.labrute.aggregate.Brute;
import com.vsct.quicky.vertx.labrute.fwk.Event;
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
public class MyEventStore extends AbstractVerticle {

    private static final Logger LOGGER = getLogger(MyEventStore.class);

    private ConcurrentMap<String, List<Event>> store = new ConcurrentHashMap<>();

    @Override
    public void start() throws Exception {
        vertx.eventBus().consumer("createNew", this::createNew);
        vertx.eventBus().consumer("saveEvents", this::saveEvent);
        vertx.eventBus().consumer("applyEvents", this::applyEvents);
        vertx.eventBus().consumer("listEvents", this::listEvents);
    }

    private void createNew(Message<String> tMessage) {
        String newId = tMessage.body();
        if (store.containsKey(newId)) {
            tMessage.fail(-1, "allready present in store.");
        } else {
            store.put(newId, new ArrayList<>());
            tMessage.reply("");
        }
    }

    private void listEvents(Message<String> tMessage) {
        String bruteId = tMessage.body();
        if (!store.containsKey(bruteId)){
            tMessage.fail(-1,bruteId + " could not be found");
        } else {
            tMessage.reply(Json.encode(getPastEvents(bruteId)));
        }
    }

    private void saveEvent(Message<String> handler) {
        Event event = Json.decodeValue(handler.body(), Event.class);
        String id = event.getId();
        // get event for brute:
        List<Event> events = store.getOrDefault(id, new ArrayList<>());
        events.add(event);
        store.put(id, events);
        // publish event
        vertx.eventBus().publish(event.getClass().getName(), handler.body());
        LOGGER.info("Store event: " + handler.body());
    }

    private void applyEvents(Message<String> tMessage) {
        Brute brute = new Brute();
        String bruteId = tMessage.body();
        brute.setId(bruteId);
        if (!store.containsKey(bruteId)){
            tMessage.fail(-1,bruteId + " could not be found");
        } else {
            brute.applyEvents(getPastEvents(bruteId));
            tMessage.reply(Json.encode(brute));
        }
    }

    private List<Event> getPastEvents(String bruteId) {
        return store.getOrDefault(bruteId, ImmutableList.of());
    }
}
