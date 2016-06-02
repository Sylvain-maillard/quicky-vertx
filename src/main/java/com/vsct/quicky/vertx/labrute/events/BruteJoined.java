package com.vsct.quicky.vertx.labrute.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vsct.quicky.vertx.labrute.aggregate.Brute;
import com.vsct.quicky.vertx.labrute.eventstore.BruteEvent;

/**
 * Created by Sylvain on 29/05/2016.
 */
public class BruteJoined extends BruteEvent {
    public BruteJoined(@JsonProperty("id") String id) {
        super(id);
    }

    @Override
    public void apply(Brute brute) {
        brute.setXp(0);
        brute.setFightCount(0);
        brute.setId(getId());
        brute.setHasJoined(true);
    }


}
