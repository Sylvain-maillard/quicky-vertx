package com.vsct.quicky.vertx.labrute.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vsct.quicky.vertx.labrute.aggregate.Brute;
import com.vsct.quicky.vertx.labrute.eventstore.BruteEvent;

/**
 * Created by Sylvain on 29/05/2016.
 */
public class BruteWinFight extends BruteEvent {
    public BruteWinFight(@JsonProperty("id") String id) {
        super(id);
    }

    @Override
    public void apply(Brute brute) {
        brute.incXp(2);
        brute.incFightCount();
        brute.setFighting(false);
    }
}
