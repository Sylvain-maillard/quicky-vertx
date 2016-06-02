package com.vsct.quicky.vertx.eventstore;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;
import com.vsct.quicky.vertx.aggregate.Brute;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * Created by Sylvain on 29/05/2016.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public abstract class BruteEvent {

    protected final String id;
    private final Date time = new Date();

    public BruteEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Date getTime() {
        return time;
    }

    @JsonIgnore
    public String getType() { return this.getClass().getSimpleName(); }

    public abstract void apply(Brute brute);

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("time", time)
                .toString();
    }
}
