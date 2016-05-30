package com.vsct.quicky.vertx.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vsct.quicky.vertx.aggregate.Brute;
import com.vsct.quicky.vertx.eventstore.BruteEvent;
import io.vertx.core.json.JsonObject;

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
    }

}
