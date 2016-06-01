package com.vsct.quicky.vertx.aggregate;

import com.google.common.base.MoreObjects;
import com.vsct.quicky.vertx.Main;
import com.vsct.quicky.vertx.eventstore.BruteCommand;
import com.vsct.quicky.vertx.eventstore.BruteEvent;
import io.vertx.core.json.Json;

import java.util.List;

/**
 * Created by Sylvain on 29/05/2016.
 */
public class Brute {

    private String id;
    private int xp;
    private int fightCount;
    private boolean hasJoined;
    private boolean isFighting;
    public Brute() {
    }

    public int getXp() {
        return xp;
    }

    public Brute setXp(int xp) {
        this.xp = xp;
        return this;
    }

    public boolean isHasJoined() {
        return hasJoined;
    }

    public Brute setHasJoined(boolean hasJoined) {
        this.hasJoined = hasJoined;
        return this;
    }

    public boolean isFighting() {
        return isFighting;
    }

    public Brute setFighting(boolean fighting) {
        isFighting = fighting;
        return this;
    }

    public void applyEvents(List<BruteEvent> events) {
        events.forEach(event -> event.apply(this));
    }

    public void processCommand(BruteCommand command) {
        command.execute(this, events -> events.forEach(event -> Main.vertx.eventBus().send("events", Json.encodePrettily(event))));
    }

    public void incFightCount() {
        this.fightCount++;
    }

    public void incXp(int inc) {
        this.xp += inc;
    }

    public boolean sameXp(Brute currentBrute) {
        return this.xp == currentBrute.xp;
    }

    public String getId() {
        return id;
    }

    public Brute setId(String id) {
        this.id = id;
        return this;
    }

    public int getFightCount() {
        return fightCount;
    }

    public void setFightCount(int fightCount) {
        this.fightCount = fightCount;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("xp", xp)
                .add("fightCount", fightCount)
                .add("hasJoined", hasJoined)
                .add("isFighting", isFighting)
                .toString();
    }
}
