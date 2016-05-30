package com.vsct.quicky.vertx.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vsct.quicky.vertx.aggregate.Brute;
import com.vsct.quicky.vertx.eventstore.BruteEvent;
import io.vertx.core.json.JsonObject;

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
    }
}
