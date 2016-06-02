package com.vsct.quicky.vertx.labrute.fwk;

import com.vsct.quicky.vertx.labrute.Main;
import io.vertx.core.json.Json;

import java.util.List;

/**
 * Created by Sylvain on 02/06/2016.
 */
public abstract class Aggregate {

    public void applyEvents(List<Event> events) {
        events.forEach(event -> event.apply(this));
    }

    public <T extends Aggregate> void processCommand(Command<T> command) {
        command.execute((T) this, events -> events.forEach(event -> Main.vertx.eventBus().send("saveEvents", Json.encodePrettily(event))));
    }


}
