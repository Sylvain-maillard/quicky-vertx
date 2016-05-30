package com.vsct.quicky.vertx.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.vsct.quicky.vertx.aggregate.Brute;
import com.vsct.quicky.vertx.eventstore.BruteEvent;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;

/**
 * Created by Sylvain on 30/05/2016.
 */
public class OpponentFound extends BruteEvent {
    private final String opponentId;

    public OpponentFound(@JsonProperty("id") String id, @JsonProperty("opponentId") String opponentId) {
        super(id);
        this.opponentId = opponentId;
    }

    @Override
    public void apply(Brute brute) {
        brute.setFighting(true);
    }

    public String getOpponentId() {
        return opponentId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("opponentId", opponentId)
                .toString();
    }
}
