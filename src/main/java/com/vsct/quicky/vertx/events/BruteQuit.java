package com.vsct.quicky.vertx.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vsct.quicky.vertx.aggregate.Brute;
import com.vsct.quicky.vertx.eventstore.BruteEvent;

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
