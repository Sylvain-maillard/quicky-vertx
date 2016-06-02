package com.vsct.quicky.vertx.labrute.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vsct.quicky.vertx.labrute.aggregate.Brute;
import com.vsct.quicky.vertx.labrute.eventstore.BruteEvent;

/**
 * Created by Sylvain on 30/05/2016.
 */
public class BruteQuit extends BruteEvent {
    public BruteQuit(@JsonProperty("id") String id) {
        super(id);
    }

    @Override
    public void apply(Brute brute) {
        brute.setHasJoined(false);
    }
}
