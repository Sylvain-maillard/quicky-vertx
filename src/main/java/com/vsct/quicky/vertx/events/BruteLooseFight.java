package com.vsct.quicky.vertx.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vsct.quicky.vertx.aggregate.Brute;
import com.vsct.quicky.vertx.eventstore.BruteEvent;
import io.vertx.core.json.JsonObject;

/**
 * Created by Sylvain on 29/05/2016.
 */
public class BruteLooseFight extends BruteEvent {
    public BruteLooseFight(@JsonProperty("id") String id) {
        super(id);
    }

    @Override
    public void apply(Brute brute) {
        brute.incXp(1);
        brute.incFightCount();
    }

}
