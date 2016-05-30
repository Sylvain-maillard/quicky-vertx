package com.vsct.quicky.vertx.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vsct.quicky.vertx.aggregate.Brute;
import com.vsct.quicky.vertx.eventstore.BruteEvent;

/**
 * Created by Sylvain on 30/05/2016.
 */
public class BruteShouldRest extends BruteEvent {
    public BruteShouldRest(@JsonProperty("id") String id) {
        super(id);
    }

    @Override
    public void apply(Brute brute) {

    }
}
