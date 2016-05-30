package com.vsct.quicky.vertx.eventstore;

import com.google.common.collect.ImmutableList;
import com.vsct.quicky.vertx.aggregate.Brute;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * Created by Sylvain on 29/05/2016.
 */
public class BruteEventStore extends AbstractVerticle {

    private ConcurrentMap<String, List<BruteEvent>> store = new ConcurrentHashMap<>();

    @Override
    public void start() throws Exception {
        vertx.eventBus().consumer("events", (Message<String> handler) -> {

            System.out.println("Store event: " + handler.body());
            BruteEvent event = Json.decodeValue(handler.body(), BruteEvent.class);

            String id = event.getId();
           // get event for brute:
            List<BruteEvent> events = store.getOrDefault(id, new ArrayList<>());
            events.add(event);
            store.put(id, events);
            // publish event
            vertx.eventBus().publish(event.getClass().getName(), handler.body());
        });
    }

    public List<BruteEvent> getPastEvents(String bruteId) {
        return store.getOrDefault(bruteId, ImmutableList.of());
    }

    public void displayAllBrutes() {
        System.out.println("-----------brutes------------");
        store.entrySet().forEach(stringListEntry -> {
            System.out.println("id: " + stringListEntry.getKey());
            System.out.println("--> events:");
            stringListEntry.getValue().forEach(System.out::println);
            System.out.println("--> brute rebuilded from store:");
            Brute brute = new Brute();
            brute.applyEvents(stringListEntry.getValue());
            System.out.println(brute);
        });

    }
}
