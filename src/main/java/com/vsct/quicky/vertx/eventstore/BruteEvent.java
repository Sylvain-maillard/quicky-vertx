package com.vsct.quicky.vertx.eventstore;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.oracle.webservices.internal.api.databinding.Databinding;
import com.vsct.quicky.vertx.aggregate.Brute;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;

/**
 * Created by Sylvain on 29/05/2016.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public abstract class BruteEvent {

    private final String id;

    public BruteEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public abstract void apply(Brute brute);
}
