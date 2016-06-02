package com.vsct.quicky.vertx.labrute.aggregate;

import com.google.common.base.MoreObjects;
import com.vsct.quicky.vertx.labrute.commands.Fight;
import com.vsct.quicky.vertx.labrute.commands.JoinArena;
import com.vsct.quicky.vertx.labrute.commands.QuitArena;
import com.vsct.quicky.vertx.labrute.fwk.Aggregate;

/**
 * Created by Sylvain on 29/05/2016.
 */
public class Brute extends Aggregate {

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

    public void fight(String opponentId) {
        processCommand(new Fight(opponentId));
    }

    public void join() {
        processCommand(new JoinArena());
    }

    public void quit() {
        processCommand(new QuitArena());
    }
}
