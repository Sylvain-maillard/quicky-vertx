package com.vsct.quicky.vertx.aggregate;

import com.google.common.base.MoreObjects;
import com.vsct.quicky.vertx.Main;
import com.vsct.quicky.vertx.eventstore.BruteCommand;
import com.vsct.quicky.vertx.eventstore.BruteEvent;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * Created by Sylvain on 29/05/2016.
 */
public class Brute {

    private String id;
    private int xp;
    private int fightCount;

    public Brute(){}

    public void applyEvents(List<BruteEvent> events) {
        events.forEach(event -> event.apply(this));
    }

    public void processCommand(BruteCommand command) {
        List<BruteEvent> events = command.execute(id);
        events.forEach(event -> Main.vertx.eventBus().send("events", Json.encodePrettily(event)));
    }

    public static Brute fromStore(String id) {
        Brute brute = new Brute();
        List<BruteEvent> pastEvents = Main.bruteEventStore.getPastEvents(id);
        brute.applyEvents(pastEvents);
        return brute;
    }

    public void incFightCount() {
        this.fightCount++;
    }

    public Brute setXp(int xp) {
        this.xp = xp;
        return this;
    }

    public void incXp(int inc) {
        this.xp += inc;
    }

    public Brute setId(String id) {
        this.id = id;
        return this;
    }

    public boolean sameXp(Brute currentBrute) {
        return this.xp == currentBrute.xp;
    }

    public String getId() {
        return id;
    }

    public int getFightCount() {
        return fightCount;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("xp", xp)
                .add("fightCount", fightCount)
                .toString();
    }

    public void setFightCount(int fightCount) {
        this.fightCount = fightCount;
    }
}
